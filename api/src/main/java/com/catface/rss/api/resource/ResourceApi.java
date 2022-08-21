package com.catface.rss.api.resource;

import java.util.Map;

import javax.validation.Valid;

import com.catface.common.model.JsonResult;
import com.catface.rss.api.resource.request.BatchGetResourceRequest;
import com.catface.rss.api.resource.request.BatchGetResourceUrlRequest;
import com.catface.rss.api.resource.request.ChangeResourceAccessStatusRequest;
import com.catface.rss.api.resource.request.DumpResourceRequest;
import com.catface.rss.api.resource.request.GetResourceRequest;
import com.catface.rss.api.resource.request.GetResourceUrlRequest;
import com.catface.rss.api.resource.vo.ResourceVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author by catface
 * @since 2021/8/23 下午10:51 . All rights reserved.
 */
@FeignClient(name = "resourceApi", url = "${rpc.rss.service}", fallbackFactory = ResourceApiFallback.class)
public interface ResourceApi {

    /**
     * 获取资源的访问路径
     *
     * @param request 资源ID,访问资源的终端类型,前端公网,后端VPC
     * @return 资源对应的路径
     */
    @PostMapping(value = "/private/resource/getUrl")
    JsonResult<String> getUrl(@RequestBody @Valid GetResourceUrlRequest request);

    /**
     * 获取资源信息
     *
     * @param request 资源ID,访问资源的终端类型,前端公网,后端VPC
     * @return 资源信息
     */
    @PostMapping(value = "/private/resource/getResource")
    JsonResult<ResourceVO> getResource(@RequestBody @Valid GetResourceRequest request);

    /**
     * 批量获取资源的访问路径
     *
     * @param request 资源ID列表,访问资源的终端类型,前端公网,后端VPC
     * @return 资源->资源路径
     */
    @PostMapping(value = "/private/resource/batchGetUrls")
    JsonResult<Map<Long, String>> batchGetUrls(@RequestBody @Valid BatchGetResourceUrlRequest request);

    /**
     * 批量获取资源信息
     *
     * @param request 资源ID列表,访问资源的终端类型,前端公网,后端VPC
     * @return 资源id->资源信息
     */
    @PostMapping(value = "/private/resource/batchGetResources")
    JsonResult<Map<Long, ResourceVO>> batchGetResources(@RequestBody @Valid BatchGetResourceRequest request);

    /**
     * 设置资源访问状态
     *
     * @param request 资源ID,访问状态
     * @return 请求结果
     */
    @PostMapping(value = "/private/resource/changeAccessStatus")
    JsonResult<Boolean> changeAccessStatus(@RequestBody @Valid ChangeResourceAccessStatusRequest request);

    /**
     * 根据资源url转储资源到OSS
     *
     * @param request 业务码,资源URL
     * @return 请求结果
     */
    @PostMapping(value = "/private/resource/dumpResource")
    JsonResult<Long> dumpResource(@RequestBody @Valid DumpResourceRequest request);

}
