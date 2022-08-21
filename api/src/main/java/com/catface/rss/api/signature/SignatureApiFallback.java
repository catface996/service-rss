package com.catface.rss.api.signature;

import java.util.List;

import com.catface.common.model.JsonResult;
import com.catface.rss.api.signature.request.GetSignatureRequest;
import com.catface.rss.api.signature.vo.SignatureVO;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author by catface
 * @since 2021/8/23 下午10:56 . All rights reserved.
 */
@Slf4j
@Component
public class SignatureApiFallback implements FallbackFactory<SignatureApi> {

    @Override
    public SignatureApi create(Throwable throwable) {
        return new SignatureApi() {

            /**
             * 获取上传文件需要的签名
             *
             * @param request 获取签名请求
             * @return 用于上传文件的签名
             */
            @Override
            public JsonResult<SignatureVO> getSignature(GetSignatureRequest request) {
                log.error("rpcError,method:getSignature,request:{}", request, throwable);
                return JsonResult.rpcError("调用获取上传文件需要的签名接口异常");
            }

            /**
             * 批量获取上传文件签名
             *
             * @param request 批量请求参数
             * @return 签名列表
             */
            @Override
            public JsonResult<List<SignatureVO>> batchGetSignature(List<GetSignatureRequest> request) {
                log.error("rpcError,method:batchGetSignature,request:{}", request, throwable);
                return JsonResult.rpcError("调用批量获取上传文件签名接口异常");
            }
        };
    }
}
