package com.catface.rss.repository.mapper;

import java.util.List;
import java.util.Set;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.catface.rss.repository.entity.UploadDownloadTask;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 上传/下载任务 Mapper 接口
 * </p>
 *
 * @author catface
 * @since 2021-08-24
 */
public interface UploadDownloadTaskMapper extends BaseMapper<UploadDownloadTask> {

    /**
     * 根据资源ID查询对应的导入/导出任务
     *
     * @param resourceId 资源ID
     * @return 导入导出任务
     */
    UploadDownloadTask selectByResourceId(@Param("resourceId") Long resourceId);

    /**
     * 根据任务类型分页查询任务列表
     *
     * @param taskType 任务类型
     * @param creators 创建人
     * @param page     分页
     * @return 任务列表
     */
    List<UploadDownloadTask> selectByTaskType(@Param("taskType") String taskType,
                                              @Param("creators") Set<String> creators,
                                              @Param("page") Page<UploadDownloadTask> page);
}
