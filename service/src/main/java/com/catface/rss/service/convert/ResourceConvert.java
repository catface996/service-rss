package com.catface.rss.service.convert;

import com.aliyun.oss.model.ObjectMetadata;
import com.catface.rss.repository.entity.Resource;
import com.catface.rss.service.resource.dto.ResourceDTO;

/**
 * @author catface
 * @since 2020/12/28
 */
public class ResourceConvert {

    public static ResourceDTO convert(Resource entity, String url, ObjectMetadata metadata) {
        ResourceDTO dto = new ResourceDTO();
        dto.setResourceId(entity.getId());
        dto.setSize(entity.getSize());
        dto.setUrl(url);
        if (metadata != null) {
            dto.setMd5(metadata.getContentMD5());
            dto.setMetadata(metadata.getRawMetadata());
        }
        dto.setStatus(entity.getStatus());
        return dto;
    }

}
