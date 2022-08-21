package com.catface.rss.http.web.controller.signature;

import java.util.List;

import javax.validation.Valid;

import com.catface.common.model.JsonResult;
import com.catface.rss.http.config.swagger.ApiConst;
import com.catface.rss.http.web.controller.signature.convert.SignatureConvert;
import com.catface.rss.http.web.controller.signature.request.GetSignatureRequest;
import com.catface.rss.http.web.controller.signature.vo.SignatureVO;
import com.catface.rss.service.signature.SignatureService;
import com.catface.rss.service.signature.dto.SignatureDTO;
import com.catface.rss.service.signature.param.GetSignatureParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author by catface
 * @since 2021/8/24 上午10:25 . All rights reserved.
 */
@Api(tags = {ApiConst.SIGNATURE})
@Slf4j
@RestController
@RequiredArgsConstructor
public class SignatureController {

    private final SignatureService signatureService;

    /**
     * 获取上传文件需要的签名
     *
     * @param request 获取签名请求
     * @return 用于上传文件的签名
     */
    @ApiOperation(value = "获取上传文件需要的签名")
    @PostMapping(value = "/public/signature/getSignature")
    public JsonResult<SignatureVO> getSignature(@RequestBody @Valid GetSignatureRequest request) {
        GetSignatureParam param = SignatureConvert.convert(request);
        SignatureDTO dto = signatureService.getSignature(param);
        return JsonResult.success(SignatureConvert.convert(dto));
    }

    /**
     * 批量获取上传文件签名
     *
     * @param request 批量请求参数
     * @return 签名列表
     */
    @ApiOperation(value = "批量获取上传文件签名")
    @PostMapping(value = "/public/signature/batchGetSignature")
    public JsonResult<List<SignatureVO>> batchGetSignature(@RequestBody @Valid List<GetSignatureRequest> request) {
        List<GetSignatureParam> params = SignatureConvert.convert(request);
        List<SignatureDTO> signatures = signatureService.batchGetSignature(params);
        return JsonResult.success(SignatureConvert.convertSignatures(signatures));
    }
}
