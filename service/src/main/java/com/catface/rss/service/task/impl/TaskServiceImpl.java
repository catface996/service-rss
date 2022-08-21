package com.catface.rss.service.task.impl;

import java.util.Date;
import java.util.Set;

import com.catface.rss.common.enums.UpDownTaskStatusEnum;
import com.catface.rss.repository.entity.UploadDownloadTask;
import com.catface.rss.repository.service.UploadDownloadTaskRpService;
import com.catface.rss.service.convert.TaskConvert;
import com.catface.rss.service.task.TaskService;
import com.catface.rss.service.task.dto.UploadDownloadTaskDTO;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author catface
 * @since 2020/4/27
 */
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final UploadDownloadTaskRpService uploadDownloadTaskRpService;

    /**
     * 更改任务状态,提供给其他系统使用的dubbo接口,仅支持更改业务处理状态
     *
     * @param taskId 任务ID
     * @param status 任务状态
     */
    @Override
    public void changeTaskStatus(Long taskId, UpDownTaskStatusEnum status) {
        UploadDownloadTask task = uploadDownloadTaskRpService.getById(taskId);
        Assert.notNull(task, "任务不存在");
        changeTaskStatus(task, status);
    }

    /**
     * 更改资源对应的任务状态
     *
     * @param resourceId 资源ID
     * @param status     任务状态
     */
    @Override
    public void changeTaskStatusByResourceId(Long resourceId, UpDownTaskStatusEnum status) {
        UploadDownloadTask task = uploadDownloadTaskRpService.queryByResourceId(resourceId);
        Assert.notNull(task, "任务不存在");
        changeTaskStatus(task, status);
    }

    /**
     * 更新对文件处理之后封装的错误文件资源ID
     *
     * @param taskId          任务ID
     * @param errorResourceId 错误结果文件对应的资源ID
     */
    @Override
    public void updateErrorResource(Long taskId, Long errorResourceId) {
        UploadDownloadTask task = uploadDownloadTaskRpService.getById(taskId);
        Assert.notNull(task, "上传下载任务不能为空");

        UploadDownloadTask taskForUpdate = new UploadDownloadTask();
        taskForUpdate.setId(taskId);
        taskForUpdate.setErrorResourceId(errorResourceId);
        taskForUpdate.setGmtModified(new Date());

        uploadDownloadTaskRpService.updateById(taskForUpdate);
    }

    /**
     * 根据原始资源ID,更新对应的处理结果文件ID
     *
     * @param sourceResourceId 原始资源ID
     * @param errorResourceId  错误文件ID
     */
    @Override
    public void updateErrorResourceBySourceId(Long sourceResourceId, Long errorResourceId) {
        UploadDownloadTask task = uploadDownloadTaskRpService.queryByResourceId(sourceResourceId);
        Assert.notNull(task, "上传下载任务不能为空");

        UploadDownloadTask taskForUpdate = new UploadDownloadTask();
        taskForUpdate.setId(task.getId());
        taskForUpdate.setErrorResourceId(errorResourceId);
        taskForUpdate.setGmtModified(new Date());

        uploadDownloadTaskRpService.updateById(taskForUpdate);
    }

    /**
     * 根据资源ID查询导入/导出任务
     *
     * @param resourceId 资源ID
     * @return 导入/导出任务
     */
    @Override
    public UploadDownloadTaskDTO queryByResourceId(Long resourceId) {
        UploadDownloadTask task = uploadDownloadTaskRpService.queryByResourceId(resourceId);
        if (task != null) {
            return TaskConvert.convert(task);
        }
        return null;
    }

    /**
     * 更改任务状态
     *
     * @param task   任务
     * @param status 状态
     */
    private void changeTaskStatus(UploadDownloadTask task, UpDownTaskStatusEnum status) {
        Set<UpDownTaskStatusEnum> legalStatus = Sets
            .immutableEnumSet(UpDownTaskStatusEnum.BIZ_PROCESS_SUCCESS,
                UpDownTaskStatusEnum.BIZ_PROCESS_FAIL);
        Assert.state(legalStatus.contains(status), "待修改的状态不合法");
        Assert.state(task.getStatus() == UpDownTaskStatusEnum.READY, "仅允许修改处于就绪状态的任务状态");

        UploadDownloadTask taskForUpdate = new UploadDownloadTask();
        taskForUpdate.setId(task.getId());
        taskForUpdate.setStatus(status);
        taskForUpdate.setGmtModified(new Date());
        uploadDownloadTaskRpService.updateById(taskForUpdate);
    }

}
