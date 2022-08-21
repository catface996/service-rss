package com.catface.rss.service.signature.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.catface.rss.common.enums.ResourceStatusEnum;
import com.catface.rss.common.enums.UpDownTaskStatusEnum;
import com.catface.rss.repository.entity.Resource;
import com.catface.rss.repository.entity.ResourceType;
import com.catface.rss.repository.entity.UploadDownloadTask;
import com.catface.rss.repository.service.ResourceRpService;
import com.catface.rss.repository.service.ResourceTypeRpService;
import com.catface.rss.repository.service.UploadDownloadTaskRpService;
import com.catface.rss.service.oss.OssService;
import com.catface.rss.service.signature.SignatureService;
import com.catface.rss.service.signature.dto.SignatureDTO;
import com.catface.rss.service.signature.param.GetSignatureParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * @author catface
 * @since 2020/4/27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SignatureServiceImpl implements SignatureService {

    private static final String PATTERN_SEPARATE = "#";

    private static final String PATH_SEPARATE = "/";

    private final ResourceTypeRpService resourceTypeRpService;

    private final ResourceRpService resourceRpService;

    private final UploadDownloadTaskRpService uploadDownloadTaskRpService;

    private final OssService ossService;

    /**
     * 获取上传文件需要的签名,如果传入了taskId,生成的签名是用于记录处理结果文件
     *
     * @param param 获取签名请求
     * @return 用于上传文件的签名
     */
    @Override
    public SignatureDTO getSignature(GetSignatureParam param) {

        log.debug("打印获取签名接口的调用日志,用于验证 dubboTrace !");

        // 根据业务码,获取上传资源类型
        ResourceType resourceType = resourceTypeRpService.queryByBizCode(param.getTypeCode());
        Assert.notNull(resourceType, "无效的资源类型编码");

        // 生成resource
        String resourcePath = buildResourcePath(resourceType, param.getResourceName(), param.getPathParam());
        Resource resource = generateResource(resourceType, param.getResourceName(), param.getCreator(), resourcePath);

        // 生成签名
        SignatureDTO signatureDTO = ossService.generateSignature(resourceType.getUploadTerm(),
            resourceType.getBucketName(), resourcePath, resource.getId(), resourceType.getNeedCallback());

        if (param.getCreateTask()) {
            // 生成task
            generateTask(resource, resourceType, param.getTaskDesc());
        }

        return signatureDTO;
    }

    /**
     * 批量获取上传文件签名
     *
     * @param params 批量请求参数
     * @return 签名列表
     */
    @Override
    public List<SignatureDTO> batchGetSignature(List<GetSignatureParam> params) {
        List<SignatureDTO> signatures = new ArrayList<>();
        for (GetSignatureParam param : params) {
            signatures.add(getSignature(param));
        }
        return signatures;
    }

    /**
     * 构建资源的URL
     *
     * @param resourceType 资源类型
     * @param pathParam    用于构建资源路径的参数
     * @return 资源的URL
     */
    private String buildResourcePath(ResourceType resourceType, String resourceName, Map<String, String> pathParam) {
        String resourcePath = resourceType.getPathPattern();
        if (!CollectionUtils.isEmpty(pathParam)) {
            for (String param : pathParam.keySet()) {
                resourcePath = resourcePath.replaceFirst(PATTERN_SEPARATE + param + PATTERN_SEPARATE,
                    pathParam.get(param));
            }
            if (resourcePath.contains(PATTERN_SEPARATE)) {
                throw new IllegalArgumentException("渲染资源路径的业务参数不完整");
            }
        }

        if (!resourcePath.endsWith(PATH_SEPARATE)) {
            resourcePath = resourcePath + PATH_SEPARATE;
        }
        if (resourcePath.startsWith(PATH_SEPARATE)) {
            resourcePath = resourcePath.replaceFirst(PATH_SEPARATE, "");
        }

        return resourcePath + UUID.randomUUID().toString().replaceAll("-", "")
            + PATH_SEPARATE + resourceName;
    }

    /**
     * 生成资源记录
     *
     * @param resourceType 资源类型
     * @param resourceName 资源名称
     * @param operator     操作人
     * @param resourcePath 资源路径
     * @return 生成后的资源记录
     */
    private Resource generateResource(ResourceType resourceType, String resourceName, String operator,
                                      String resourcePath) {
        Date currentDate = new Date();
        Resource resource = new Resource();
        resource.setCreator(operator);
        resource.setModifier(operator);
        resource.setResourceName(resourceName);
        resource.setResourcePath(resourcePath);
        resource.setResourceTypeId(resourceType.getId());
        resource.setBucketName(resourceType.getBucketName());
        resource.setGmtCreate(currentDate);
        resource.setGmtModified(currentDate);
        resource.setStatus(ResourceStatusEnum.INIT);
        resource.setAccessStatus(resourceType.getDefaultAccessStatus());
        resourceRpService.save(resource);
        return resource;
    }

    /**
     * 生成资源上传/下载任务
     *
     * @param resource     资源
     * @param resourceType 资源类型
     * @param taskDesc     任务描述
     * @return 生成的任务
     */
    private UploadDownloadTask generateTask(Resource resource, ResourceType resourceType,
                                            String taskDesc) {
        UploadDownloadTask uploadDownloadTask = new UploadDownloadTask();
        uploadDownloadTask.setCreator(resource.getCreator());
        uploadDownloadTask.setModifier(resource.getModifier());
        uploadDownloadTask.setGmtCreate(resource.getGmtCreate());
        uploadDownloadTask.setGmtModified(resource.getGmtModified());
        uploadDownloadTask.setResourceId(resource.getId());
        uploadDownloadTask.setStatus(UpDownTaskStatusEnum.INIT);
        uploadDownloadTask.setTaskDesc(taskDesc);
        uploadDownloadTask.setTaskType(resourceType.getTypeCode());
        uploadDownloadTask.setUploadOrDownload(resourceType.getUpDown());
        uploadDownloadTaskRpService.save(uploadDownloadTask);
        return uploadDownloadTask;
    }

}
