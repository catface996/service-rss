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
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author by catface
 * @since 2021/8/23 下午10:54 . All rights reserved.
 */
@Slf4j
@Component
public class ResourceApiFallback implements FallbackFactory<ResourceApi> {

    @Override
    public ResourceApi create(Throwable throwable) {
        return new ResourceApi() {

            /**
             * 获取资源的访问路径
             *
             * @param request 资源ID,访问资源的终端类型,前端公网,后端VPC
             * @return 资源对应的路径
             */
            @Override
            public JsonResult<String> getUrl(GetResourceUrlRequest request) {
                log.error("rpcError,method:getResourceUrl,request:{}", request, throwable);
                return JsonResult.rpcError("调用获取资源的访问路径接口异常");
            }

            /**
             * 获取资源信息
             *
             * @param request 资源ID,访问资源的终端类型,前端公网,后端VPC
             * @return 资源信息
             */
            @Override
            public JsonResult<ResourceVO> getResource(GetResourceRequest request) {
                log.error("rpcError,method:getResource,request:{}", request, throwable);
                return JsonResult.rpcError("调用获取资源信息接口异常");
            }

            /**
             * 批量获取资源的访问路径
             *
             * @param request 资源ID列表,访问资源的终端类型,前端公网,后端VPC
             * @return 资源->资源路径
             */
            @Override
            public JsonResult<Map<Long, String>> batchGetUrls(BatchGetResourceUrlRequest request) {
                log.error("rpcError,method:batchGetResourceUrls,request:{}", request, throwable);
                return JsonResult.rpcError("调用批量获取资源的访问路径接口异常");
            }

            /**
             * 批量获取资源信息
             *
             * @param request 资源ID列表,访问资源的终端类型,前端公网,后端VPC
             * @return 资源id->资源信息
             */
            @Override
            public JsonResult<Map<Long, ResourceVO>> batchGetResources(BatchGetResourceRequest request) {
                log.error("rpcError,method:batchGetResources,request:{}", request, throwable);
                return JsonResult.rpcError("调用批量获取资源信息接口异常");
            }

            /**
             * 设置资源访问状态
             *
             * @param request 资源ID,访问状态
             * @return 请求结果
             */
            @Override
            public JsonResult<Boolean> changeAccessStatus(ChangeResourceAccessStatusRequest request) {
                log.error("rpcError,method:changeResourceAccessStatus,request:{}", request, throwable);
                return JsonResult.rpcError("调用设置资源访问状态接口异常");
            }

            /**
             * 根据资源url转储资源到OSS
             *
             * @param request 业务码,资源URL
             * @return 请求结果
             */
            @Override
            public JsonResult<Long> dumpResource(@Valid DumpResourceRequest request) {
                log.error("rpcError,method:dump,request:{}", request, throwable);
                return JsonResult.rpcError("调用根据资源url转储资源到OSS接口异常");
            }
        };
    }
}
