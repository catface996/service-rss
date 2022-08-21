package com.catface.rss.http.web.controller.resource.convert;

import java.util.Map;
import java.util.Map.Entry;

import com.catface.rss.http.web.controller.resource.request.BatchGetResourceRequest;
import com.catface.rss.http.web.controller.resource.request.BatchGetResourceUrlRequest;
import com.catface.rss.http.web.controller.resource.request.DumpResourceRequest;
import com.catface.rss.http.web.controller.resource.request.GetResourceRequest;
import com.catface.rss.http.web.controller.resource.request.GetResourceUrlRequest;
import com.catface.rss.http.web.controller.resource.vo.ResourceVO;
import com.catface.rss.service.resource.dto.ResourceDTO;
import com.catface.rss.service.resource.param.BatchGetResourceParam;
import com.catface.rss.service.resource.param.BatchGetResourceUrlParam;
import com.catface.rss.service.resource.param.DumpResourceParam;
import com.catface.rss.service.resource.param.GetResourceParam;
import com.catface.rss.service.resource.param.GetResourceUrlParam;
import com.google.common.collect.Maps;
import org.springframework.cglib.beans.BeanCopier;

/**
 * @author by catface
 * @since 2021/8/24 下午1:07 . All rights reserved.
 */
public class ResourceConvert {

    private static final BeanCopier GET_RESOURCE_URL_REQUEST_2_PARAM = BeanCopier.create(GetResourceUrlRequest.class,
        GetResourceUrlParam.class, false);

    private static final BeanCopier GET_RESOURCE_REQUEST_2_PARAM = BeanCopier.create(GetResourceRequest.class,
        GetResourceParam.class, false);

    private static final BeanCopier BATCH_GET_RESOURCE_URL_REQUEST_2_PARAM = BeanCopier.create(
        BatchGetResourceUrlRequest.class, BatchGetResourceUrlParam.class, false);

    private static final BeanCopier BATCH_GET_RESOURCE_REQUEST_2_PARAM = BeanCopier.create(
        BatchGetResourceRequest.class, BatchGetResourceParam.class, false);

    private static final BeanCopier DUMP_RESOURCE_REQUEST_2_PARAM = BeanCopier.create(DumpResourceRequest.class,
        DumpResourceParam.class, false);

    private static final BeanCopier RESOURCE_DTO_2_VO = BeanCopier.create(ResourceDTO.class, ResourceVO.class, false);

    public static GetResourceUrlParam convert(GetResourceUrlRequest request) {
        GetResourceUrlParam param = new GetResourceUrlParam();
        GET_RESOURCE_URL_REQUEST_2_PARAM.copy(request, param, null);
        return param;
    }

    public static GetResourceParam convert(GetResourceRequest request) {
        GetResourceParam param = new GetResourceParam();
        GET_RESOURCE_REQUEST_2_PARAM.copy(request, param, null);
        return param;
    }

    public static BatchGetResourceUrlParam convert(BatchGetResourceUrlRequest request) {
        BatchGetResourceUrlParam param = new BatchGetResourceUrlParam();
        BATCH_GET_RESOURCE_URL_REQUEST_2_PARAM.copy(request, param, null);
        return param;
    }

    public static BatchGetResourceParam convert(BatchGetResourceRequest request) {
        BatchGetResourceParam param = new BatchGetResourceParam();
        BATCH_GET_RESOURCE_REQUEST_2_PARAM.copy(request, param, null);
        return param;
    }

    public static DumpResourceParam convert(DumpResourceRequest request) {
        DumpResourceParam param = new DumpResourceParam();
        DUMP_RESOURCE_REQUEST_2_PARAM.copy(request, param, null);
        param.setCreator(request.getCtxOperatorId());
        return param;
    }

    public static ResourceVO convert(ResourceDTO dto) {
        ResourceVO vo = new ResourceVO();
        RESOURCE_DTO_2_VO.copy(dto, vo, null);
        return vo;
    }

    public static Map<Long, ResourceVO> convert(Map<Long, ResourceDTO> resources) {
        Map<Long, ResourceVO> map = Maps.newHashMap();
        for (Entry<Long, ResourceDTO> entry : resources.entrySet()) {
            map.put(entry.getKey(), convert(entry.getValue()));
        }
        return map;
    }
}
