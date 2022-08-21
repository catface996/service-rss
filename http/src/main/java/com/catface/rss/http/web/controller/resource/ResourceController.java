package com.catface.rss.http.web.controller.resource;

import java.util.Map;

import javax.validation.Valid;

import com.catface.common.model.JsonResult;
import com.catface.rss.http.config.swagger.ApiConst;
import com.catface.rss.http.web.controller.resource.convert.ResourceConvert;
import com.catface.rss.http.web.controller.resource.request.BatchGetResourceRequest;
import com.catface.rss.http.web.controller.resource.request.BatchGetResourceUrlRequest;
import com.catface.rss.http.web.controller.resource.request.DumpResourceRequest;
import com.catface.rss.http.web.controller.resource.request.GetResourceRequest;
import com.catface.rss.http.web.controller.resource.request.GetResourceUrlRequest;
import com.catface.rss.http.web.controller.resource.vo.ResourceVO;
import com.catface.rss.service.resource.ResourceService;
import com.catface.rss.service.resource.dto.ResourceDTO;
import com.catface.rss.service.resource.param.BatchGetResourceParam;
import com.catface.rss.service.resource.param.BatchGetResourceUrlParam;
import com.catface.rss.service.resource.param.DumpResourceParam;
import com.catface.rss.service.resource.param.GetResourceParam;
import com.catface.rss.service.resource.param.GetResourceUrlParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author by catface
 * @since 2021/8/24 上午10:20 . All rights reserved.
 */
@Api(tags = {ApiConst.RESOURCE})
@Slf4j
@RestController
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    /**
     * 获取资源的访问路径
     *
     * @param request 资源ID,访问资源的终端类型,前端公网,后端VPC
     * @return 资源对应的路径
     */
    @ApiOperation(value = "获取资源的访问路径")
    @PostMapping(value = "/public/resource/getResourceUrl")
    public JsonResult<String> getResourceUrl(@RequestBody @Valid GetResourceUrlRequest request) {
        GetResourceUrlParam param = ResourceConvert.convert(request);
        return JsonResult.success(resourceService.getUrl(param));
    }

    /**
     * 获取资源信息
     *
     * @param request 资源ID,访问资源的终端类型,前端公网,后端VPC
     * @return 资源信息
     */
    @ApiOperation(value = "获取资源信息")
    @PostMapping(value = "/public/resource/getResource")
    public JsonResult<ResourceVO> getResource(@RequestBody @Valid GetResourceRequest request) {
        GetResourceParam param = ResourceConvert.convert(request);
        ResourceDTO dto = resourceService.getResource(param);
        return JsonResult.success(ResourceConvert.convert(dto));
    }

    /**
     * 批量获取资源的访问路径
     *
     * @param request 资源ID列表,访问资源的终端类型,前端公网,后端VPC
     * @return 资源->资源路径
     */
    @ApiOperation(value = "批量获取资源的访问路径")
    @PostMapping(value = "/public/resource/batchGetResourceUrls")
    public JsonResult<Map<Long, String>> batchGetResourceUrls(@RequestBody @Valid BatchGetResourceUrlRequest request) {
        BatchGetResourceUrlParam param = ResourceConvert.convert(request);
        return JsonResult.success(resourceService.batchGetUrls(param));
    }

    /**
     * 批量获取资源信息
     *
     * @param request 资源ID列表,访问资源的终端类型,前端公网,后端VPC
     * @return 资源id->资源信息
     */
    @ApiOperation(value = "批量获取资源信息")
    @PostMapping(value = "/public/resource/batchGetResources")
    public JsonResult<Map<Long, ResourceVO>> batchGetResources(@RequestBody @Valid BatchGetResourceRequest request) {
        BatchGetResourceParam param = ResourceConvert.convert(request);
        Map<Long, ResourceDTO> resources = resourceService.batchGetResources(param);
        return JsonResult.success(ResourceConvert.convert(resources));
    }

    /**
     * 根据资源url转储资源到OSS
     *
     * @param request 业务码,资源URL
     * @return 请求结果
     */
    @ApiOperation(value = "根据资源url转储资源到OSS")
    @PostMapping(value = "/public/resource/dumpResource")
    public JsonResult<Long> dumpResource(@RequestBody @Valid DumpResourceRequest request) {
        DumpResourceParam param = ResourceConvert.convert(request);
        return JsonResult.success(resourceService.dumpResource(param));
    }

}
