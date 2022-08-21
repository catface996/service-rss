package com.catface.rss.http.rpc.controller.task.convert;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.catface.common.model.PageVO;
import com.catface.rss.api.task.vo.UploadDownloadTaskVO;
import com.catface.rss.repository.entity.UploadDownloadTask;
import com.google.common.collect.Lists;
import org.springframework.cglib.beans.BeanCopier;

/**
 * @author catface
 * @since 2020/4/29
 */
public class TaskConvert {

    private static final BeanCopier ENTITY_2_VO = BeanCopier.create(UploadDownloadTask.class,
        UploadDownloadTaskVO.class, false);

    public static UploadDownloadTaskVO convert(UploadDownloadTask entity) {
        if (entity == null) {
            return null;
        }
        UploadDownloadTaskVO vo = new UploadDownloadTaskVO();
        ENTITY_2_VO.copy(entity, vo, null);
        return vo;
    }

    public static List<UploadDownloadTaskVO> convert(List<UploadDownloadTask> tasks) {
        List<UploadDownloadTaskVO> vos = Lists.newArrayList();
        tasks.forEach(task -> vos.add(convert(task)));
        return vos;
    }

    public static PageVO<UploadDownloadTaskVO> convert(Page<UploadDownloadTask> page) {
        return new PageVO<>(convert(page.getRecords()), (int)page.getCurrent(), (int)page.getSize(),
            (int)page.getTotal(), (int)page.getPages());
    }

}
