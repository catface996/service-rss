package com.catface.rss.repository.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.catface.rss.common.enums.UpDownTaskStatusEnum;
import com.catface.rss.repository.entity.UploadDownloadTask;
import com.catface.rss.repository.mapper.UploadDownloadTaskMapper;
import com.catface.rss.repository.service.UploadDownloadTaskRpService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.Set;

/**
 * <p>
 * 上传/下载任务 服务实现类
 * </p>
 *
 * @author catface
 * @since 2021-08-24
 */
@Service
public class UploadDownloadTaskRpServiceImpl extends ServiceImpl<UploadDownloadTaskMapper, UploadDownloadTask>
        implements UploadDownloadTaskRpService {

    /**
     * 根据资源ID查找对应的导入/导出任务
     *
     * @param resourceId 资源ID
     * @return 导入/导出任务
     */
    @Override
    public UploadDownloadTask queryByResourceId(Long resourceId) {
        return baseMapper.selectByResourceId(resourceId);
    }

    /**
     * 根据任务类型查询任务列表
     *
     * @param taskType 任务类型
     * @param creators 创建人列表
     * @param current  当前页码
     * @param size     分页大小
     * @return 任务列表-分页
     */
    @Override
    public Page<UploadDownloadTask> queryByTaskType(String taskType, Set<String> creators, Long current,
                                                    Long size) {
        Page<UploadDownloadTask> page = new Page<>(current, size);
        page.setRecords(baseMapper.selectByTaskType(taskType, creators, page));
        return page;
    }

    /**
     * 更新上传/下载任务状态,目前仅支持由初始装填改为就绪
     *
     * @param taskId 任务ID
     * @param status 任务待更改的任务状态
     */
    @Override
    public void changeTaskStatus(Long taskId, UpDownTaskStatusEnum status) {
        UploadDownloadTask task = getById(taskId);
        Assert.notNull(task, "文件上传/下载任务不存在");

        if (task.getStatus() == UpDownTaskStatusEnum.INIT) {
            UploadDownloadTask taskForUpdate = new UploadDownloadTask();
            taskForUpdate.setId(taskId);
            taskForUpdate.setGmtModified(new Date());
            taskForUpdate.setStatus(UpDownTaskStatusEnum.READY);
            updateById(taskForUpdate);
        }
    }
}
