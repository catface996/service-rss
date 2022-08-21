package com.catface.rss.service.callback.impl;

import com.catface.rss.common.enums.ResourceStatusEnum;
import com.catface.rss.common.enums.UpDownTaskStatusEnum;
import com.catface.rss.repository.entity.Resource;
import com.catface.rss.repository.entity.ResourceType;
import com.catface.rss.repository.entity.UploadDownloadTask;
import com.catface.rss.repository.service.ResourceRpService;
import com.catface.rss.repository.service.ResourceTypeRpService;
import com.catface.rss.repository.service.UploadDownloadTaskRpService;
import com.catface.rss.service.callback.CallbackService;
import com.catface.rss.service.callback.param.OssCallbackParam;
import com.catface.rss.service.config.ons.publisher.ResourcesNoticePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author by catface
 * @since 2021/8/24 上午10:55 . All rights reserved.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CallbackServiceImpl implements CallbackService {

    private final ResourceRpService resourceRpService;

    private final ResourceTypeRpService resourceTypeRpService;

    private final UploadDownloadTaskRpService uploadDownloadTaskRpService;

    private final ResourcesNoticePublisher resourcesNoticePublisher;

    /**
     * 处理OSS的回调
     *
     * @param param 回调数据
     */
    @Override
    public void processCallback(OssCallbackParam param) {
        Resource resource = resourceRpService.processOssCallback(param.getResourceId(), param.getSize(),
            ResourceStatusEnum.UPLOAD_SUCCESS);

        // 发送资源消息
        ResourceType resourceType = resourceTypeRpService.getById(resource.getResourceTypeId());
        resourcesNoticePublisher.sendResourceMessage(resource, resourceType);

        UploadDownloadTask uploadDownloadTask = uploadDownloadTaskRpService.queryByResourceId(param.getResourceId());
        if (uploadDownloadTask != null) {
            uploadDownloadTaskRpService.changeTaskStatus(uploadDownloadTask.getId(), UpDownTaskStatusEnum.READY);
        }
    }
}
