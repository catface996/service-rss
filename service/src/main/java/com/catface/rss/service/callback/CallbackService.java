package com.catface.rss.service.callback;

import com.catface.rss.service.callback.param.OssCallbackParam;

/**
 * @author by catface
 * @since 2021/8/24 上午10:55 . All rights reserved.
 */
public interface CallbackService {

    /**
     * 处理OSS的回调
     *
     * @param param 回调数据
     */
    void processCallback(OssCallbackParam param);
}
