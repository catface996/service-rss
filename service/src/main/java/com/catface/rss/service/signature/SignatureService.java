package com.catface.rss.service.signature;

import java.util.List;

import com.catface.rss.service.signature.dto.SignatureDTO;
import com.catface.rss.service.signature.param.GetSignatureParam;

/**
 * @author catface
 * @since 2020/4/27
 */
public interface SignatureService {

    /**
     * 获取上传文件需要的签名
     *
     * @param param 获取签名请求
     * @return 用于上传文件的签名
     */
    SignatureDTO getSignature(GetSignatureParam param);

    /**
     * 批量获取上传文件签名
     *
     * @param params 批量请求参数
     * @return 签名列表
     */
    List<SignatureDTO> batchGetSignature(List<GetSignatureParam> params);

}
