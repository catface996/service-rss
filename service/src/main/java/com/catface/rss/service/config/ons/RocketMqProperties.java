package com.catface.rss.service.config.ons;

import java.util.Properties;

import com.aliyun.openservices.ons.api.PropertyKeyConst;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author catface
 * @since 2020/5/25
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "rocketmq")
public class RocketMqProperties {

    /**
     * 通用配置
     */
    private CommonProperties common;

    /**
     * 业务 rocket mq properties
     */
    private BizProperties biz;

    @Data
    public static class CommonProperties {
        /**
         * accessKey
         */
        private String accessKey;

        /**
         * secretKey
         */
        private String secretKey;

        /**
         * nameSrvAddr
         */
        private String nameSrvAddr;

        /**
         * 消费者线程数
         */
        private Integer consumeThreadNums;

        /**
         * consumer groupId
         */
        private String groupId;
    }

    @Data
    public static class BizProperties {

        /**
         * 资源消息topic
         */
        private String resourceNoticeTopic;

    }

    public Properties getCommonProperTies() {
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.AccessKey, this.common.accessKey);
        properties.setProperty(PropertyKeyConst.SecretKey, this.common.secretKey);
        properties.setProperty(PropertyKeyConst.NAMESRV_ADDR, this.common.nameSrvAddr);
        properties.setProperty(PropertyKeyConst.ConsumeThreadNums, this.common.consumeThreadNums.toString());
        properties.setProperty(PropertyKeyConst.GROUP_ID, this.common.groupId);
        return properties;
    }

}
