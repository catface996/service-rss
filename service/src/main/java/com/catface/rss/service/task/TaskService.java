package com.catface.rss.service.task;

import com.catface.rss.common.enums.UpDownTaskStatusEnum;
import com.catface.rss.service.task.dto.UploadDownloadTaskDTO;

/**
 * @author catface
 * @since 2020/4/27
 */
public interface TaskService {

    /**
     * 更改任务状态
     *
     * @param taskId 任务ID
     * @param status 任务状态
     */
    void changeTaskStatus(Long taskId, UpDownTaskStatusEnum status);

    /**
     * 更改资源对应的任务状态
     *
     * @param resourceId 资源ID
     * @param status     任务状态
     */
    void changeTaskStatusByResourceId(Long resourceId, UpDownTaskStatusEnum status);

    /**
     * 更新对文件处理之后封装的错误文件资源ID
     *
     * @param taskId          任务ID
     * @param errorResourceId 错误结果文件对应的资源ID
     */
    void updateErrorResource(Long taskId, Long errorResourceId);

    /**
     * 根据原始资源ID,更新对应的处理结果文件ID
     *
     * @param sourceResourceId 原始资源ID
     * @param errorResourceId  错误文件ID
     */
    void updateErrorResourceBySourceId(Long sourceResourceId, Long errorResourceId);

    /**
     * 根据资源ID查询导入/导出任务
     *
     * @param resourceId 资源ID
     * @return 导入/导出任务
     */
    UploadDownloadTaskDTO queryByResourceId(Long resourceId);

}
