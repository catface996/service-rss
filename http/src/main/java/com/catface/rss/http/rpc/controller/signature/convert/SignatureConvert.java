package com.catface.rss.http.rpc.controller.signature.convert;

import java.util.List;

import com.catface.rss.api.signature.request.GetSignatureRequest;
import com.catface.rss.api.signature.vo.SignatureVO;
import com.catface.rss.service.signature.dto.SignatureDTO;
import com.catface.rss.service.signature.param.GetSignatureParam;
import com.google.common.collect.Lists;
import org.springframework.cglib.beans.BeanCopier;

/**
 * @author by catface
 * @since 2021/8/24 下午1:28 . All rights reserved.
 */
public class SignatureConvert {

    private static final BeanCopier GET_SIGNATURE_REQUEST_2_PARAM = BeanCopier.create(GetSignatureRequest.class,
        GetSignatureParam.class, false);

    private static final BeanCopier SIGNATURE_DTO_2_VO = BeanCopier.create(SignatureDTO.class, SignatureVO.class,
        false);

    public static GetSignatureParam convert(GetSignatureRequest request) {
        GetSignatureParam param = new GetSignatureParam();
        GET_SIGNATURE_REQUEST_2_PARAM.copy(request, param, null);
        return param;
    }

    public static List<GetSignatureParam> convert(List<GetSignatureRequest> requests) {
        List<GetSignatureParam> params = Lists.newArrayList();
        for (GetSignatureRequest request : requests) {
            params.add(convert(request));
        }
        return params;
    }

    public static SignatureVO convert(SignatureDTO dto) {
        SignatureVO vo = new SignatureVO();
        SIGNATURE_DTO_2_VO.copy(dto, vo, null);
        return vo;
    }

    public static List<SignatureVO> convertSignatures(List<SignatureDTO> signatures) {
        List<SignatureVO> vos = Lists.newArrayList();
        for (SignatureDTO signature : signatures) {
            vos.add(convert(signature));
        }
        return vos;
    }
}
