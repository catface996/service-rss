package com.catface.rss.api.task;

import com.catface.common.model.JsonResult;
import com.catface.rss.api.task.request.ChangeTaskStatusByResourceIdRequest;
import com.catface.rss.api.task.request.ChangeTaskStatusRequest;
import com.catface.rss.api.task.request.GetTaskByResourceIdRequest;
import com.catface.rss.api.task.request.UpdateErrorResourceBySourceIdRequest;
import com.catface.rss.api.task.request.UpdateErrorResourceByTaskIdRequest;
import com.catface.rss.api.task.vo.UploadDownloadTaskVO;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author by catface
 * @since 2021/8/23 下午10:59 . All rights reserved.
 */
@Slf4j
@Component
public class TaskApiFallback implements FallbackFactory<TaskApi> {

    @Override
    public TaskApi create(Throwable throwable) {
        return new TaskApi() {

            /**
             * 更改任务状态
             *
             * @param request 请求参数
             * @return 请求结果
             */
            @Override
            public JsonResult<Boolean> changeStatus(ChangeTaskStatusRequest request) {
                log.error("rpcError,method:changeStatus,request:{}", request, throwable);
                return JsonResult.rpcError("调用更改任务状态接口异常");
            }

            /**
             * 更改资源对应的任务状态
             *
             * @param request 请求参数
             * @return 请求结果
             */
            @Override
            public JsonResult<Boolean> changeStatus(ChangeTaskStatusByResourceIdRequest request) {
                log.error("rpcError,method:changeStatus,request:{}", request, throwable);
                return JsonResult.rpcError("调用更改资源对应的任务状态接口异常");
            }

            /**
             * 更新对文件处理之后封装的错误文件资源ID
             *
             * @param request 请求参数
             * @return 请求结果
             */
            @Override
            public JsonResult<Boolean> updateErrorResource(UpdateErrorResourceByTaskIdRequest request) {
                log.error("rpcError,method:updateErrorResource,request:{}", request, throwable);
                return JsonResult.rpcError("调用更新对文件处理之后封装的错误文件资源ID接口异常");
            }

            /**
             * 根据原始资源ID,更新对应的处理结果文件ID
             *
             * @param request 请求参数
             * @return 请求结果
             */
            @Override
            public JsonResult<Boolean> updateErrorResource(UpdateErrorResourceBySourceIdRequest request) {
                log.error("rpcError,method:updateErrorResource,request:{}", request, throwable);
                return JsonResult.rpcError("调用根据原始资源ID,更新对应的处理结果文件ID接口异常");
            }

            /**
             * 根据资源ID查询导入/导出任务
             *
             * @param request 请求参数
             * @return 请求结果
             */
            @Override
            public JsonResult<UploadDownloadTaskVO> getTask(GetTaskByResourceIdRequest request) {
                log.error("rpcError,method:getTask,request:{}", request, throwable);
                return JsonResult.rpcError("调用根据资源ID查询导入/导出任务接口异常");
            }
        };
    }
}
