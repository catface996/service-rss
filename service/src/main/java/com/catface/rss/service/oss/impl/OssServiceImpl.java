package com.catface.rss.service.oss.impl;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONObject;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.internal.OSSUtils;
import com.aliyun.oss.model.Callback;
import com.aliyun.oss.model.Callback.CalbackBodyType;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.GenericRequest;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PolicyConditions;
import com.catface.rss.common.enums.AccessStatusEnum;
import com.catface.rss.common.enums.AccessTermEnum;
import com.catface.rss.common.enums.AccessTypeEnum;
import com.catface.rss.common.enums.NeedCallbackEnum;
import com.catface.rss.common.enums.ResourceStatusEnum;
import com.catface.rss.repository.entity.Resource;
import com.catface.rss.repository.entity.ResourceType;
import com.catface.rss.repository.service.ResourceTypeRpService;
import com.catface.rss.service.convert.ResourceConvert;
import com.catface.rss.service.oss.OssService;
import com.catface.rss.service.resource.dto.ResourceDTO;
import com.catface.rss.service.signature.dto.SignatureDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * @author catface
 * @since 2018/8/2
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "oss")
public class OssServiceImpl implements OssService {

    /**
     * WARNING: 需要使用3个反斜杠,使用JavaBean json序列化后的字符串会使回调失效
     */
    private static final String CALL_BACK_BODY_PATTERN =
        "{\\\"bucket\\\":${bucket},\\\"object\\\":${object},"
            + "\\\"etag\\\":${etag},\\\"size\\\":${size},\\\"resourceId\\\":#resourceId}";
    private static final String RESOURCE_ID_REG = "#resourceId";
    private static final String HTTP = "http://";
    private static final String HTTPS = "https://";
    private static final String PERIOD = ".";
    private static final String PATH_SEP = "/";
    private static final String TEMP_URL = "{0}{1}{2}";
    private static final long URL_EXPIRE_TIME = 600 * 1000L;
    private String accessKey;
    private String accessSecret;
    private String priEndpoint;
    private String pubEndpoint;
    private String callbackUrl;
    private Boolean usePubEndpoint = false;

    private final ResourceTypeRpService resourceTypeRpService;

    /**
     * 生成资源的临时访问路径
     *
     * @param resourceStoreList 资源列表
     * @param accessTerm        访问资源的端位,前端公网,后端VPC
     * @param processString     oss媒体资源处理字符串（视频截图、图片处理）
     * @return 资源ID和访问路径关联关系
     */
    @Override
    public Map<Long, String> generateTempUrl(List<Resource> resourceStoreList, AccessTermEnum accessTerm,
                                             String processString) {
        if (CollectionUtils.isEmpty(resourceStoreList)) {
            return new HashMap<>(0);
        }

        Set<Long> resourceTypeIds = resourceStoreList.stream().map(Resource::getResourceTypeId).collect(
            Collectors.toSet());

        Collection<ResourceType> resourceConfigList = resourceTypeRpService.listByIds(resourceTypeIds);
        Map<Long, ResourceType> resourceTypeMap = resourceConfigList.stream().collect(
            Collectors.toMap(ResourceType::getId, v -> v));

        return batchGenerateAccessUrl(resourceStoreList, resourceTypeMap, accessTerm, processString);
    }

    /**
     * 生成资源的访问路径
     *
     * @param resource      资源对象
     * @param accessTerm    访问资源的端位,前端公网,后端VPC
     * @param processString oss媒体资源处理字符串（视频截图、图片处理）
     * @return 资源的访问路径
     */
    @Override
    public String generateTempUrl(Resource resource, AccessTermEnum accessTerm, String processString) {
        ResourceType resourceType = resourceTypeRpService.getById(resource.getResourceTypeId());
        return generateTmpUrl(accessTerm, resource, resourceType, processString);
    }

    /**
     * 生成临时访问oss的授权签名
     *
     * @param accessTerm   访问方式
     * @param bucket       bucket
     * @param resourcePath 访问文件路径
     * @param resourceId   资源ID
     * @return 临时访问权限的签名
     */
    @Override
    public SignatureDTO generateSignature(AccessTermEnum accessTerm, String bucket, String resourcePath,
                                          Long resourceId, NeedCallbackEnum needCallback) {
        String endPoint = getEndPoint(accessTerm);
        String host = buildHost(accessTerm, bucket, endPoint);
        OSS client = buildOssClient(accessTerm);
        String encodeCallback = null;
        try {
            if (needCallback == NeedCallbackEnum.NEED) {
                Callback callBack = new Callback();
                callBack.setCallbackUrl(callbackUrl);
                callBack.setCalbackBodyType(CalbackBodyType.JSON);
                callBack.setCallbackBody(CALL_BACK_BODY_PATTERN.replaceAll(RESOURCE_ID_REG,
                    resourceId.toString()));
                String callbackJsonString = OSSUtils.jsonizeCallback(callBack);
                encodeCallback = BinaryUtil
                    .toBase64String(callbackJsonString.getBytes(StandardCharsets.UTF_8));
            }

            long expireTime = 300;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConditions = new PolicyConditions();
            policyConditions.addConditionItem(PolicyConditions.COND_KEY, resourcePath);

            String postPolicy = client.generatePostPolicy(expiration, policyConditions);
            String encodedPolicy = BinaryUtil.toBase64String(postPolicy.getBytes(StandardCharsets.UTF_8));
            String postSignature = client.calculatePostSignature(postPolicy);
            SignatureDTO signatureDTO = new SignatureDTO();
            signatureDTO.setAccessId(accessKey);
            signatureDTO.setKey(resourcePath);
            signatureDTO.setExpire(String.valueOf(expireEndTime / 1000));
            signatureDTO.setPolicy(encodedPolicy);
            signatureDTO.setSignature(postSignature);
            signatureDTO.setHost(host);
            if (needCallback == NeedCallbackEnum.NEED) {
                signatureDTO.setCallback(encodeCallback);
            }
            signatureDTO.setResourceId(resourceId);
            return signatureDTO;
        } catch (Exception e) {
            log.error("获取oss临时签名异常!", e);
            return null;
        } finally {
            client.shutdown();
        }
    }

    /**
     * 获取资源信息
     *
     * @param resource   资源对象
     * @param accessTerm 访问资源的端位,前端公网,后端VPC
     * @return 资源信息
     */
    @Override
    public ResourceDTO getResource(Resource resource, AccessTermEnum accessTerm) {
        ResourceType resourceType = resourceTypeRpService.getById(resource.getResourceTypeId());
        OSS ossClient = buildOssClient(accessTerm);
        String url = generateTmpUrl(ossClient, accessTerm, resource, resourceType);
        ObjectMetadata metadata = getObjectMetadata(ossClient, resource);
        ossClient.shutdown();
        return ResourceConvert.convert(resource, url, metadata);
    }

    /**
     * 批量获取资源信息
     *
     * @param resourceList 资源列表
     * @param accessTerm   访问资源的端位,前端公网,后端VPC
     * @return 资源id->资源信息
     */
    @Override
    public Map<Long, ResourceDTO> getResources(List<Resource> resourceList, AccessTermEnum accessTerm) {
        Map<Long, ResourceDTO> resourceMap = new HashMap<>(resourceList.size());
        if (!CollectionUtils.isEmpty(resourceList)) {
            Resource oneResource = resourceList.get(0);
            ResourceType resourceType = resourceTypeRpService.getById(oneResource.getResourceTypeId());
            OSS ossClient = buildOssClient(accessTerm);
            for (Resource resource : resourceList) {
                String url = generateTmpUrl(ossClient, accessTerm, resource, resourceType);
                ObjectMetadata metadata = getObjectMetadata(ossClient, resource);
                resourceMap.put(resource.getId(), ResourceConvert.convert(resource, url, metadata));
            }
            ossClient.shutdown();
        }
        return resourceMap;
    }

    /**
     * 设置资源访问权限
     *
     * @param resource 资源
     */
    @Override
    public void setResourceAcl(Resource resource) {
        OSS ossClient = buildOssClient(AccessTermEnum.BACKEND);
        CannedAccessControlList acl = resource.getAccessStatus() == AccessStatusEnum.ACCESS
            ? CannedAccessControlList.Default : CannedAccessControlList.Private;
        if (resource.getStatus() == ResourceStatusEnum.INIT) {
            boolean exist = doesObjectExist(ossClient, resource.getBucketName(), resource.getResourcePath());
            Assert.state(exist, "OSS资源不存在");
        }
        setObjectAcl(ossClient, resource.getBucketName(), resource.getResourcePath(), acl);
        ossClient.shutdown();
    }

    /**
     * 获取资源ObjectMetadata
     *
     * @param ossClient oss客户端
     * @param resource  资源
     * @return 资源ObjectMetadata
     */
    private ObjectMetadata getObjectMetadata(OSS ossClient, Resource resource) {
        boolean needGetMetadata = true;
        if (resource.getStatus() == ResourceStatusEnum.INIT) {
            needGetMetadata = doesObjectExist(ossClient, resource.getBucketName(), resource.getResourcePath());
        }
        return needGetMetadata ? getObjectMetadata(ossClient, resource.getBucketName(),
            resource.getResourcePath()) : null;
    }

    /**
     * 生成公网访问的资源路径,如果是私有的bucket,路径有有效时间
     *
     * @param resourceList    资源列表
     * @param resourceTypeMap 资源配置map
     * @param processString   oss媒体资源处理字符串（视频截图、图片处理）
     */
    private Map<Long, String> batchGenerateAccessUrl(List<Resource> resourceList,
                                                     Map<Long, ResourceType> resourceTypeMap, AccessTermEnum accessTerm,
                                                     String processString) {
        // 根据配置构造oss客户端,仅构造私有访问的bucket的客户端,公有访问直接使用域名+相对路径即可
        OSS ossClient = buildOssClient(accessTerm);

        //过期时间
        Date expiration = new Date(System.currentTimeMillis() + URL_EXPIRE_TIME);
        Map<Long, String> urlMap = new HashMap<>(resourceList.size());

        // 构造资源的临时访问路径,如果是私有的bucket,临时访问路径存在过期时间
        for (Resource resource : resourceList) {
            ResourceType resourceType = resourceTypeMap.get(resource.getResourceTypeId());

            // 获取访问域名
            String accessDomain = getAccessDomain(accessTerm, resourceType);
            if (resourceType.getAccessType() == AccessTypeEnum.PRIVATE) {
                GeneratePresignedUrlRequest request = buildGenerateUrlRequest(processString, expiration,
                    resource.getBucketName(), resource.getResourcePath());
                URL url = ossClient.generatePresignedUrl(request);
                urlMap.put(resource.getId(), accessDomain + url.getFile());
            } else {
                String url = accessDomain + PATH_SEP + resource.getResourcePath();
                urlMap.put(resource.getId(), StringUtils.isEmpty(processString) ? url : url + processString);
            }
        }

        //关闭oss客户端
        ossClient.shutdown();
        return urlMap;
    }

    /**
     * 生成单个资源访问路径,如果是私有的bucket,路径有有效时间
     *
     * @param accessTerm   访问端位,前端or后端
     * @param resource     资源信息
     * @param resourceType 资源配置
     * @return 资源访问路径
     */
    private String generateTmpUrl(OSS ossClient, AccessTermEnum accessTerm, Resource resource,
                                  ResourceType resourceType) {
        // 获取相对地址
        String relativeUrl = resource.getResourcePath();

        // 私有bucket生成访问路径
        if (resourceType.getAccessType() == AccessTypeEnum.PRIVATE) {
            //过期时间
            Date expiration = new Date(System.currentTimeMillis() + URL_EXPIRE_TIME);
            URL url = ossClient.generatePresignedUrl(resource.getBucketName(), relativeUrl, expiration);
            relativeUrl = url.getFile().substring(1);
        }

        // 获取访问域名
        String accessDomain = getAccessDomain(accessTerm, resourceType);

        return buildTempUrl(accessDomain, relativeUrl);
    }

    /**
     * 生成单个资源访问路径,如果是私有的bucket,路径有有效时间
     *
     * @param accessTerm    访问端位,前端or后端
     * @param resource      资源信息
     * @param resourceType  资源配置
     * @param processString oss媒体资源处理字符串（视频截图、图片处理）
     * @return 资源访问路径
     */
    private String generateTmpUrl(AccessTermEnum accessTerm, Resource resource, ResourceType resourceType,
                                  String processString) {
        // 获取相对地址
        String relativeUrl = resource.getResourcePath();

        // 私有bucket生成访问路径
        if (resourceType.getAccessType() == AccessTypeEnum.PRIVATE) {
            OSS ossClient = buildOssClient(accessTerm);
            //过期时间
            Date expiration = new Date(System.currentTimeMillis() + URL_EXPIRE_TIME);
            GeneratePresignedUrlRequest request = buildGenerateUrlRequest(processString, expiration,
                resource.getBucketName(), relativeUrl);
            URL url = ossClient.generatePresignedUrl(request);
            relativeUrl = url.getFile().substring(1);

            //关闭oss客户端
            ossClient.shutdown();
        } else {
            relativeUrl = StringUtils.isEmpty(processString) ? relativeUrl : relativeUrl + processString;
        }

        // 获取访问域名
        String accessDomain = getAccessDomain(accessTerm, resourceType);

        return buildTempUrl(accessDomain, relativeUrl);
    }

    /**
     * 构建生成url请求
     *
     * @param processString oss媒体资源处理字符串（视频截图、图片处理）
     * @param expiration    私有bucket资源过期时间
     * @param bucketName    资源bucket信息
     * @param relativeUrl   资源相对地址
     * @return 生成url请求
     */
    private GeneratePresignedUrlRequest buildGenerateUrlRequest(String processString, Date expiration,
                                                                String bucketName, String relativeUrl) {
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, relativeUrl);
        request.setExpiration(expiration);
        if (!StringUtils.isEmpty(processString)) {
            String[] process = processString.replaceFirst("\\?", "").split("=");
            Assert.state(process.length == 2, "oss媒体资源处理字符串错误");
            request.addQueryParameter(process[0], process[1]);
        }
        return request;
    }

    /**
     * 构造OssClient
     *
     * @param accessTerm 访问端位,前端or后端
     * @return 资源访问路径
     */
    private OSS buildOssClient(AccessTermEnum accessTerm) {
        // 根据配置构造oss客户端
        String endPoint = getEndPoint(accessTerm);
        return new OSSClientBuilder().build(endPoint, accessKey, accessSecret);
    }

    /**
     * 构造签名host
     *
     * @param accessTerm 访问端位,前端or后端
     * @param bucket     ossBucket
     * @param endPoint   oss接入点
     * @return 签名host
     */
    private String buildHost(AccessTermEnum accessTerm, String bucket, String endPoint) {
        // 如果是获取公网上传文件的签名,返回https格式
        String host = bucket + PERIOD + endPoint;
        return accessTerm == AccessTermEnum.FRONT ? HTTPS + host : HTTP + host;
    }

    /**
     * 获取oss接入点
     *
     * @param accessTerm 访问端位,前端or后端
     * @return oss接入点
     */
    private String getEndPoint(AccessTermEnum accessTerm) {
        return accessTerm == AccessTermEnum.FRONT || usePubEndpoint ? pubEndpoint : priEndpoint;
    }

    /**
     * 获取资源访问域名
     *
     * @param accessTerm   访问端位,前端or后端
     * @param resourceType 资源信息
     * @return 资源访问域名
     */
    private String getAccessDomain(AccessTermEnum accessTerm, ResourceType resourceType) {
        return accessTerm == AccessTermEnum.BACKEND ? resourceType.getPrivateAccessDomain()
            : resourceType.getPublicAccessDomain();
    }

    /**
     * 构造访问路径
     *
     * @param accessDomain 访问域名
     * @param relativeUrl  相对路径
     * @return 访问路径
     */
    private String buildTempUrl(String accessDomain, String relativeUrl) {
        return MessageFormat.format(TEMP_URL, accessDomain, PATH_SEP, relativeUrl);
    }

    /**
     * 获取资源元信息
     *
     * @param ossClient   oss客户端
     * @param bucket      bucket
     * @param relativeUrl 资源相对路径
     * @return ObjectMetadata
     */
    private ObjectMetadata getObjectMetadata(OSS ossClient, String bucket, String relativeUrl) {
        return ossClient.getObjectMetadata(bucket, relativeUrl);
    }

    /**
     * 检查资源是否存在
     *
     * @param ossClient   oss客户端
     * @param bucket      bucket
     * @param relativeUrl 资源相对路径
     * @return 检查结果
     */
    private boolean doesObjectExist(OSS ossClient, String bucket, String relativeUrl) {
        GenericRequest request = new GenericRequest();
        request.setBucketName(bucket);
        request.setKey(relativeUrl);
        return ossClient.doesObjectExist(request);
    }

    /**
     * 设置文件访问权限
     *
     * @param ossClient   oss客户端
     * @param bucket      bucket
     * @param relativeUrl 资源相对路径
     * @param acl         文件访问权限
     */
    private void setObjectAcl(OSS ossClient, String bucket, String relativeUrl, CannedAccessControlList acl) {
        ossClient.setObjectAcl(bucket, relativeUrl, acl);
    }

    /**
     * 构造回调body
     *
     * @param resourceId 资源id
     * @param taskId     任务id
     * @return 回调body
     */
    private String buildCallBackBody(Long resourceId, Long taskId) {
        JSONObject body = new JSONObject();
        body.put("bucket", "${bucket}");
        body.put("object", "${object}");
        body.put("etag", "${etag}");
        body.put("size", "${size}");
        body.put("resourceId", resourceId.toString());
        body.put("taskId", taskId.toString());
        return body.toJSONString();
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getAccessSecret() {
        return accessSecret;
    }

    public void setAccessSecret(String accessSecret) {
        this.accessSecret = accessSecret;
    }

    public String getPriEndpoint() {
        return priEndpoint;
    }

    public void setPriEndpoint(String priEndpoint) {
        this.priEndpoint = priEndpoint;
    }

    public String getPubEndpoint() {
        return pubEndpoint;
    }

    public void setPubEndpoint(String pubEndpoint) {
        this.pubEndpoint = pubEndpoint;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public Boolean getUsePubEndpoint() {
        return usePubEndpoint;
    }

    public void setUsePubEndpoint(Boolean usePubEndpoint) {
        this.usePubEndpoint = usePubEndpoint;
    }
}
