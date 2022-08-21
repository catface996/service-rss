package com.catface.rss.http.web.controller.callback;

import com.catface.common.model.JsonResult;
import com.catface.rss.http.config.swagger.ApiConst;
import com.catface.rss.http.web.controller.callback.convert.CallbackConvert;
import com.catface.rss.http.web.controller.callback.request.OssCallbackRequest;
import com.catface.rss.service.callback.CallbackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author by catface
 * @since 2021/8/24 上午10:52 . All rights reserved.
 */
@Api(tags = {ApiConst.CALLBACK})
@Slf4j
@RestController
@RequiredArgsConstructor
public class CallbackController {

    private final CallbackService callbackService;

    /**
     * 接收OSS回调
     *
     * @param request 回调参数
     * @return 返回结果
     */
    @ApiOperation(value = "接收OSS回调")
    @PostMapping(value = {"/anonymous/callback/receiveOssCallback"})
    public JsonResult<String> receiveOssCallback(@RequestBody OssCallbackRequest request) {
        log.info("receive callback:{}", request);
        callbackService.processCallback(CallbackConvert.convert(request));
        return JsonResult.success("success");
    }

}
