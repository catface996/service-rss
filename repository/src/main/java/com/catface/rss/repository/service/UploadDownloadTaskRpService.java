package com.catface.rss.repository.service;

import java.util.Set;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.catface.rss.common.enums.UpDownTaskStatusEnum;
import com.catface.rss.repository.entity.UploadDownloadTask;

/**
 * <p>
 * 上传/下载任务 服务类
 * </p>
 *
 * @author catface
 * @since 2021-08-24
 */
public interface UploadDownloadTaskRpService extends IService<UploadDownloadTask> {

    /**
     * 根据资源ID查找对应的导入/导出任务
     *
     * @param resourceId 资源ID
     * @return 导入/导出任务
     */
    UploadDownloadTask queryByResourceId(Long resourceId);

    /**
     * 根据任务类型查询任务列表
     *
     * @param taskType 任务类型
     * @param creators 创建人列表
     * @param current  当前页码
     * @param size     分页大小
     * @return 任务列表-分页
     */
    Page<UploadDownloadTask> queryByTaskType(String taskType, Set<String> creators, Long current, Long size);

    /**
     * 更新上传/下载任务状态,目前仅支持由初始装填改为就绪
     *
     * @param taskId 任务ID
     * @param status 任务待更改的任务状态
     */
    void changeTaskStatus(Long taskId, UpDownTaskStatusEnum status);

}
