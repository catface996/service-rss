package com.catface.rss.http.web.controller.callback.convert;

import com.catface.rss.http.web.controller.callback.request.OssCallbackRequest;
import com.catface.rss.service.callback.param.OssCallbackParam;
import org.springframework.cglib.beans.BeanCopier;

/**
 * @author catface
 * @since 2020/4/28
 */
public class CallbackConvert {

    private static final BeanCopier REQUEST_2_PARAM = BeanCopier.create(OssCallbackRequest.class,
        OssCallbackParam.class, false);

    public static OssCallbackParam convert(OssCallbackRequest request) {
        OssCallbackParam param = new OssCallbackParam();
        REQUEST_2_PARAM.copy(request, param, null);
        return param;
    }
}
