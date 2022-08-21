package com.catface.rss.http.rpc.controller.task;

import javax.validation.Valid;

import com.catface.common.model.JsonResult;
import com.catface.rss.api.task.TaskApi;
import com.catface.rss.api.task.request.ChangeTaskStatusByResourceIdRequest;
import com.catface.rss.api.task.request.ChangeTaskStatusRequest;
import com.catface.rss.api.task.request.GetTaskByResourceIdRequest;
import com.catface.rss.api.task.request.UpdateErrorResourceBySourceIdRequest;
import com.catface.rss.api.task.request.UpdateErrorResourceByTaskIdRequest;
import com.catface.rss.api.task.vo.UploadDownloadTaskVO;
import com.catface.rss.http.config.swagger.ApiConst;
import com.catface.rss.http.rpc.controller.task.convert.TaskConvert;
import com.catface.rss.repository.entity.UploadDownloadTask;
import com.catface.rss.repository.service.UploadDownloadTaskRpService;
import com.catface.rss.service.task.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author by catface
 * @since 2021/8/24 上午10:26 . All rights reserved.
 */
@Api(tags = {ApiConst.TASK})
@Slf4j
@RestController
@RequiredArgsConstructor
public class TaskApiController implements TaskApi {

    private final TaskService taskService;

    private final UploadDownloadTaskRpService uploadDownloadTaskRpService;

    /**
     * 更改任务状态
     *
     * @param request 请求参数
     * @return 请求结果
     */
    @ApiOperation(value = "更改任务状态")
    @Override
    public JsonResult<Boolean> changeStatus(@RequestBody @Valid ChangeTaskStatusRequest request) {
        taskService.changeTaskStatus(request.getTaskId(), request.getStatus());
        return JsonResult.success(true);
    }

    /**
     * 更改资源对应的任务状态
     *
     * @param request 请求参数
     * @return 请求结果
     */
    @ApiOperation(value = "更改资源对应的任务状态")
    @Override
    public JsonResult<Boolean> changeStatus(@RequestBody @Valid ChangeTaskStatusByResourceIdRequest request) {
        taskService.changeTaskStatusByResourceId(request.getResourceId(), request.getStatus());
        return JsonResult.success(true);
    }

    /**
     * 更新对文件处理之后封装的错误文件资源ID
     *
     * @param request 请求参数
     * @return 请求结果
     */
    @ApiOperation(value = "更新对文件处理之后封装的错误文件资源ID")
    @Override
    public JsonResult<Boolean> updateErrorResource(@RequestBody @Valid UpdateErrorResourceByTaskIdRequest request) {
        taskService.updateErrorResource(request.getTaskId(), request.getErrorResourceId());
        return JsonResult.success(true);
    }

    /**
     * 根据原始资源ID,更新对应的处理结果文件ID
     *
     * @param request 请求参数
     * @return 请求结果
     */
    @ApiOperation(value = "根据原始资源ID,更新对应的处理结果文件ID")
    @Override
    public JsonResult<Boolean> updateErrorResource(@RequestBody @Valid UpdateErrorResourceBySourceIdRequest request) {
        taskService.updateErrorResourceBySourceId(request.getSourceResourceId(), request.getErrorResourceId());
        return JsonResult.success(true);
    }

    /**
     * 根据资源ID查询导入/导出任务
     *
     * @param request 请求参数
     * @return 请求结果
     */
    @ApiOperation(value = "根据资源ID查询导入/导出任务")
    @Override
    public JsonResult<UploadDownloadTaskVO> getTask(@RequestBody @Valid GetTaskByResourceIdRequest request) {
        UploadDownloadTask task = uploadDownloadTaskRpService.queryByResourceId(request.getResourceId());
        return JsonResult.success(TaskConvert.convert(task));
    }
}
