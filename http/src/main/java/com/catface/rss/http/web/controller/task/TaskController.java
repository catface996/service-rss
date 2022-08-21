package com.catface.rss.http.web.controller.task;

import javax.validation.Valid;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.catface.common.model.JsonResult;
import com.catface.common.model.PageVO;
import com.catface.rss.http.config.swagger.ApiConst;
import com.catface.rss.http.web.controller.task.convert.TaskConvert;
import com.catface.rss.http.web.controller.task.request.GetTaskByResourceIdRequest;
import com.catface.rss.http.web.controller.task.request.GetTaskRequest;
import com.catface.rss.http.web.controller.task.vo.UploadDownloadTaskVO;
import com.catface.rss.repository.entity.UploadDownloadTask;
import com.catface.rss.repository.service.UploadDownloadTaskRpService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
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
public class TaskController {

    private final UploadDownloadTaskRpService uploadDownloadTaskRpService;

    /**
     * 分页查询任务
     *
     * @param request 请求参数
     * @return 请求结果
     */
    @ApiOperation(value = "分页查询任务")
    @PostMapping(value = "/public/task/page")
    public JsonResult<PageVO<UploadDownloadTaskVO>> page(@RequestBody @Valid GetTaskRequest request) {
        Page<UploadDownloadTask> page = uploadDownloadTaskRpService.queryByTaskType(request.getTaskType(),
            request.getCreators(), request.getCurrent(), request.getSize());
        PageVO<UploadDownloadTaskVO> pageVO = TaskConvert.convert(page);
        return JsonResult.success(pageVO);

    }

    /**
     * 根据资源ID查询导入/导出任务
     *
     * @param request 请求参数
     * @return 请求结果
     */
    @ApiOperation(value = "根据资源ID查询导入/导出任务")
    @PostMapping(value = "/public/task/getTask")
    public JsonResult<UploadDownloadTaskVO> getTask(@RequestBody @Valid GetTaskByResourceIdRequest request) {
        UploadDownloadTask task = uploadDownloadTaskRpService.queryByResourceId(request.getResourceId());
        return JsonResult.success(TaskConvert.convert(task));
    }
}
