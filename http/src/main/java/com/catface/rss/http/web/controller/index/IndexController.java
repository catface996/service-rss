package com.catface.rss.http.web.controller.index;

import java.net.InetAddress;

import com.catface.common.model.JsonResult;
import com.catface.rss.http.config.swagger.ApiConst;
import io.swagger.annotations.Api;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author by catface
 * @since 2020/12/15
 */
@Api(tags = {ApiConst.INDEX})
@Slf4j
@RestController
public class IndexController {

    @Value("${spring.profiles.active}")
    private String env;

    @SneakyThrows
    @GetMapping(value = {"/"})
    public JsonResult<String> index() {
        String ip = InetAddress.getLocalHost().getHostAddress();
        String message = "Hello,这里是 catface rss.IP:" + ip + ",env:" + env;
        log.info(message);
        return JsonResult.success(message);
    }

}
