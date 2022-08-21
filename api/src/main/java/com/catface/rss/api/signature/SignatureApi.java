package com.catface.rss.api.signature;

import java.util.List;

import javax.validation.Valid;

import com.catface.common.model.JsonResult;
import com.catface.rss.api.signature.request.GetSignatureRequest;
import com.catface.rss.api.signature.vo.SignatureVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author by catface
 * @since 2021/8/23 下午10:55 . All rights reserved.
 */
@FeignClient(name = "signatureApi", url = "${rpc.rss.service}", fallbackFactory = SignatureApiFallback.class)
public interface SignatureApi {

    /**
     * 获取上传文件需要的签名
     *
     * @param request 获取签名请求
     * @return 用于上传文件的签名
     */
    @PostMapping(value = "/private/signature/getSignature")
    JsonResult<SignatureVO> getSignature(@RequestBody @Valid GetSignatureRequest request);

    /**
     * 批量获取上传文件签名
     *
     * @param request 批量请求参数
     * @return 签名列表
     */
    @PostMapping(value = "/private/signature/batchGetSignature")
    JsonResult<List<SignatureVO>> batchGetSignature(@RequestBody @Valid List<GetSignatureRequest> request);
}
