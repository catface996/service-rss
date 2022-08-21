package com.catface.rss.service.resource.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.catface.rss.common.enums.AccessStatusEnum;
import com.catface.rss.common.enums.AccessTypeEnum;
import com.catface.rss.repository.entity.Resource;
import com.catface.rss.repository.entity.ResourceType;
import com.catface.rss.repository.service.ResourceRpService;
import com.catface.rss.repository.service.ResourceTypeRpService;
import com.catface.rss.service.config.ons.publisher.ResourcesNoticePublisher;
import com.catface.rss.service.oss.OssService;
import com.catface.rss.service.resource.ResourceService;
import com.catface.rss.service.resource.dto.ResourceDTO;
import com.catface.rss.service.resource.param.BatchGetResourceParam;
import com.catface.rss.service.resource.param.BatchGetResourceUrlParam;
import com.catface.rss.service.resource.param.ChangeResourceAccessStatusParam;
import com.catface.rss.service.resource.param.DumpResourceParam;
import com.catface.rss.service.resource.param.GetResourceParam;
import com.catface.rss.service.resource.param.GetResourceUrlParam;
import com.catface.rss.service.resource.util.ResourceUtil;
import com.catface.rss.service.signature.SignatureService;
import com.catface.rss.service.signature.dto.SignatureDTO;
import com.catface.rss.service.signature.param.GetSignatureParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author catface
 * @since 2020/4/28
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final OssService ossService;

    private final SignatureService signatureService;

    private final ResourceRpService resourceRpService;

    private final ResourceTypeRpService resourceTypeRpService;

    private final ResourcesNoticePublisher resourcesNoticePublisher;

    /**
     * 获取资源的访问路径
     *
     * @param param 资源ID,访问资源的终端类型,前端公网,后端VPC
     * @return 资源对应的路径
     */
    @Override
    public String getUrl(GetResourceUrlParam param) {
        Resource resource = resourceRpService.getById(param.getResourceId());
        Assert.notNull(resource, "资源不存在");
        if (resource.getAccessStatus() == AccessStatusEnum.NO_ACCESS) {
            log.error("当前资源访问状态为不可访问");
            return null;
        }
        return ossService.generateTempUrl(resource, param.getAccessTerm(), param.getProcessString());
    }

    /**
     * 获取资源信息
     *
     * @param param 资源ID,访问资源的终端类型,前端公网,后端VPC
     * @return 资源信息
     */
    @Override
    public ResourceDTO getResource(GetResourceParam param) {
        Resource resource = resourceRpService.getById(param.getResourceId());
        Assert.notNull(resource, "资源不存在");
        if (resource.getAccessStatus() == AccessStatusEnum.NO_ACCESS) {
            log.error("当前资源访问状态为不可访问");
            return null;
        }
        return ossService.getResource(resource, param.getAccessTerm());
    }

    /**
     * 批量获取资源的访问路径
     *
     * @param param 资源ID列表,访问资源的终端类型,前端公网,后端VPC
     * @return 资源->资源路径
     */
    @Override
    public Map<Long, String> batchGetUrls(BatchGetResourceUrlParam param) {
        List<Resource> resourceList = new ArrayList<>(resourceRpService.listByIds(param.getResourceIds()));
        List<Resource> resources = resourceList.stream().filter(f -> f.getAccessStatus() == AccessStatusEnum.ACCESS)
            .collect(Collectors.toList());
        return ossService.generateTempUrl(resources, param.getAccessTerm(), param.getProcessString());
    }

    /**
     * 批量获取资源信息
     *
     * @param param 资源ID列表,访问资源的终端类型,前端公网,后端VPC
     * @return 资源id->资源信息
     */
    @Override
    public Map<Long, ResourceDTO> batchGetResources(BatchGetResourceParam param) {
        List<Resource> resourceList = new ArrayList<>(resourceRpService.listByIds(param.getResourceIds()));
        List<Resource> resources = resourceList.stream().filter(f -> f.getAccessStatus() == AccessStatusEnum.ACCESS)
            .collect(Collectors.toList());
        return ossService.getResources(resources, param.getAccessTerm());
    }

    /**
     * 设置资源访问状态
     *
     * @param param 资源ID,访问状态
     */
    @Override
    public void changeAccessStatus(ChangeResourceAccessStatusParam param) {
        Resource resource = resourceRpService.getById(param.getResourceId());
        Assert.notNull(resource, "资源不存在");

        // 设置公有bucket资源访问权限
        resource.setAccessStatus(param.getAccessStatus());
        ResourceType resourceType = resourceTypeRpService.getById(resource.getResourceTypeId());
        if (resourceType.getAccessType() == AccessTypeEnum.PUBLIC) {
            ossService.setResourceAcl(resource);
        }

        // 更新资源访问状态
        Resource resourceForUpdate = new Resource();
        resourceForUpdate.setId(param.getResourceId());
        resourceForUpdate.setAccessStatus(param.getAccessStatus());
        resourceForUpdate.setAntiFraudContent(param.getAntiFraudContent());
        resourceForUpdate.setUpdated(new Date());
        resourceForUpdate.setModifier(param.getOperator());
        resourceRpService.updateById(resourceForUpdate);

        // 发送资源消息
        resource.setUpdated(new Date());
        resource.setModifier(param.getOperator());
        resource.setAntiFraudContent(param.getAntiFraudContent());
        resourcesNoticePublisher.sendResourceMessage(resource, resourceType);
    }

    /**
     * 根据资源url转储资源到OSS
     *
     * @param param 业务码,资源URL
     * @return 资源ID
     */
    @Override
    public Long dumpResource(DumpResourceParam param) {
        InputStream stream = getInputStreamByUrl(param.getResourceUrl());
        Assert.notNull(stream, "获取资源文件流错误");
        Long resourceId = uploadFile(param.getTypeCode(), param.getResourceName(), param.getContentType(), stream,
            param.getCreator(), 10000);
        Assert.notNull(resourceId, "转储资源文件到OSS异常");
        return resourceId;
    }

    /**
     * 上传文件到oss
     *
     * @param typeCode     rss资源业务码
     * @param resourceName 资源名称
     * @param contentType  内容的类型,允许为空,默认是: multipart/form-data
     * @param inputStream  文件流
     * @param creator      创建人
     * @param timeout      socket读取时间(毫秒)
     * @return 资源ID
     */
    public Long uploadFile(String typeCode, String resourceName, String contentType, InputStream inputStream,
                           Long creator, int timeout) {
        SignatureDTO signature = getSignature(typeCode, resourceName, creator);
        try {
            boolean success = ResourceUtil.uploadFile(inputStream, resourceName, contentType, signature, timeout);
            if (success) {
                return signature.getResourceId();
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获取资源签名信息
     *
     * @param typeCode     rss资源业务码
     * @param resourceName 资源名称
     * @param creator      创建人
     * @return 资源签名信息
     */
    private SignatureDTO getSignature(String typeCode, String resourceName, Long creator) {
        GetSignatureParam param = new GetSignatureParam();
        param.setTypeCode(typeCode);
        param.setResourceName(resourceName);
        param.setCreateTask(false);
        param.setCreator(creator);
        return signatureService.getSignature(param);
    }

    /**
     * 根据地址获得数据的输入流
     *
     * @param resourceUrl 网络连接地址
     * @return url的输入流
     */
    private InputStream getInputStreamByUrl(String resourceUrl) {
        try {
            URL url = new URL(resourceUrl);
            return url.openStream();
        } catch (Exception e) {
            log.error("read url resource fail, url : {}", resourceUrl, e);
        }
        return null;
    }

}
