package com.catface.rss.repository.service.impl;

import java.util.Date;

import com.catface.rss.common.enums.ResourceStatusEnum;
import com.catface.rss.repository.entity.Resource;
import com.catface.rss.repository.mapper.ResourceMapper;
import com.catface.rss.repository.service.ResourceRpService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * <p>
 * 资源 服务实现类
 * </p>
 *
 * @author catface
 * @since 2021-08-24
 */
@Service
public class ResourceRpServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceRpService {

    /**
     * 更改资源状态
     *
     * @param resourceId 资源ID
     * @param size       资源大小
     * @param status     资源状态
     * @return 资源信息
     */
    @Override
    public Resource processOssCallback(Long resourceId, Integer size, ResourceStatusEnum status) {
        Resource resource = getById(resourceId);
        Assert.notNull(resource, "资源不存在");
        if (resource.getStatus() == ResourceStatusEnum.INIT) {
            Resource resourceForUpdate = new Resource();
            resourceForUpdate.setId(resourceId);
            resourceForUpdate.setGmtModified(new Date());
            resourceForUpdate.setStatus(status);
            resourceForUpdate.setSize(size);
            updateById(resourceForUpdate);
        }

        resource.setGmtModified(new Date());
        resource.setStatus(status);
        resource.setSize(size);
        return resource;
    }
}
