package com.catface.rss.api.task;

import javax.validation.Valid;

import com.catface.common.model.JsonResult;
import com.catface.rss.api.task.request.ChangeTaskStatusByResourceIdRequest;
import com.catface.rss.api.task.request.ChangeTaskStatusRequest;
import com.catface.rss.api.task.request.GetTaskByResourceIdRequest;
import com.catface.rss.api.task.request.UpdateErrorResourceBySourceIdRequest;
import com.catface.rss.api.task.request.UpdateErrorResourceByTaskIdRequest;
import com.catface.rss.api.task.vo.UploadDownloadTaskVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author by catface
 * @since 2021/8/23 下午10:58 . All rights reserved.
 */
@FeignClient(name = "taskApi", url = "${rpc.rss.service}", fallbackFactory = TaskApiFallback.class)
public interface TaskApi {

    /**
     * 更改任务状态
     *
     * @param request 请求参数
     * @return 请求结果
     */
    @PostMapping(value = "/private/task/changeStatus")
    JsonResult<Boolean> changeStatus(@RequestBody @Valid ChangeTaskStatusRequest request);

    /**
     * 更改资源对应的任务状态
     *
     * @param request 请求参数
     * @return 请求结果
     */
    @PostMapping(value = "/private/task/changeStatusByResourceId")
    JsonResult<Boolean> changeStatus(@RequestBody @Valid ChangeTaskStatusByResourceIdRequest request);

    /**
     * 更新对文件处理之后封装的错误文件资源ID
     *
     * @param request 请求参数
     * @return 请求结果
     */
    @PostMapping(value = "/private/task/updateErrorResourceByTaskId")
    JsonResult<Boolean> updateErrorResource(@RequestBody @Valid UpdateErrorResourceByTaskIdRequest request);

    /**
     * 根据原始资源ID,更新对应的处理结果文件ID
     *
     * @param request 请求参数
     * @return 请求结果
     */
    @PostMapping(value = "/private/task/updateErrorResourceBySourceId")
    JsonResult<Boolean> updateErrorResource(@RequestBody @Valid UpdateErrorResourceBySourceIdRequest request);

    /**
     * 根据资源ID查询导入/导出任务
     *
     * @param request 请求参数
     * @return 请求结果
     */
    @PostMapping(value = "/private/task/getTask")
    JsonResult<UploadDownloadTaskVO> getTask(@RequestBody @Valid GetTaskByResourceIdRequest request);
}
