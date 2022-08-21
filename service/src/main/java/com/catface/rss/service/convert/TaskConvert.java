package com.catface.rss.service.convert;

import com.catface.rss.repository.entity.UploadDownloadTask;
import com.catface.rss.service.task.dto.UploadDownloadTaskDTO;
import org.springframework.cglib.beans.BeanCopier;

/**
 * @author catface
 * @since 2020/5/4
 */
public class TaskConvert {

    private static final BeanCopier ENTITY_2_DTO = BeanCopier.create(UploadDownloadTask.class,
        UploadDownloadTaskDTO.class, false);

    public static UploadDownloadTaskDTO convert(UploadDownloadTask entity) {
        UploadDownloadTaskDTO dto = new UploadDownloadTaskDTO();
        ENTITY_2_DTO.copy(entity, dto, null);
        return dto;
    }

}
