package com.catface.rss.service.config.ons;

import com.aliyun.openservices.ons.api.bean.ProducerBean;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author by catface
 * @since 2020/08/22
 */
@Configuration
@RequiredArgsConstructor
public class PublisherConfig {

    private final RocketMqProperties rocketMqProperties;

    @Bean(initMethod = "start", destroyMethod = "shutdown", name = "rssProducer")
    public ProducerBean rssProducer() {
        ProducerBean producer = new ProducerBean();
        producer.setProperties(rocketMqProperties.getCommonProperTies());
        return producer;
    }

}
