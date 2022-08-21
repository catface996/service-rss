package com.catface.rss;

import com.catface.common.util.EnvUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author by catface
 * @since 2020/12/13
 */
@Slf4j
@SpringBootApplication(
    scanBasePackages = {"com.catface"}
)
public class RssApplication {

    public static void main(String[] args) {
        SpringApplication.run(RssApplication.class, args);
        log.info("RssApplication start success!");
        System.out.println("RssApplication start success!");
        System.out.println(EnvUtil.getSwaggerUrl());
        System.out.println(EnvUtil.getDocUrl());
    }
}
