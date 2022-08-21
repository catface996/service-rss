package com.catface.rss.service.config.ons.publisher;

import com.alibaba.fastjson.JSON;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import com.catface.rss.common.message.ResourceMessage;
import com.catface.rss.repository.entity.Resource;
import com.catface.rss.repository.entity.ResourceType;
import com.catface.rss.service.config.ons.RocketMqProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 资源消息生产者
 *
 * @author catface
 * @since 2021年01月21日 11:35 下午
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ResourcesNoticePublisher {

    private final ProducerBean rssProducer;

    private final RocketMqProperties rocketMqProperties;

    /**
     * 异步单条发送资源消息
     *
     * @param resource     资源信息
     * @param resourceType 资源类型
     */
    public void sendResourceMessage(Resource resource, ResourceType resourceType) {
        ResourceMessage message = buildResourceMessage(resource, resourceType);
        sendResourceMessage(message);
    }

    /**
     * 异步单条发送资源消息
     *
     * @param message 资源消息内容
     */
    @Async
    public void sendResourceMessage(ResourceMessage message) {
        log.debug("发送资源消息,message:{}", message);
        try {
            Message msg = new Message(rocketMqProperties.getBiz().getResourceNoticeTopic(),
                message.getResourceTypeCode(), JSON.toJSONString(message).getBytes());
            SendResult sendResult = rssProducer.send(msg);
            log.debug("发送资源消息结果:{}", sendResult);
        } catch (Throwable e) {
            log.error("发送资源消息异常", e);
        }
    }

    /**
     * 构建资源消息
     *
     * @param resource     资源信息
     * @param resourceType 资源类型
     * @return 资源消息
     */
    private ResourceMessage buildResourceMessage(Resource resource, ResourceType resourceType) {
        ResourceMessage resourceMessage = new ResourceMessage();
        resourceMessage.setId(resource.getId());
        resourceMessage.setResourceName(resource.getResourceName());
        resourceMessage.setResourceTypeId(resource.getResourceTypeId());
        resourceMessage.setResourceTypeName(resourceType.getTypeName());
        resourceMessage.setResourceTypeCode(resourceType.getTypeCode());
        resourceMessage.setSize(resource.getSize());
        resourceMessage.setStatus(resource.getStatus());
        resourceMessage.setAccessStatus(resource.getAccessStatus());
        resourceMessage.setAntiFraudContent(resource.getAntiFraudContent());
        return resourceMessage;
    }

}
