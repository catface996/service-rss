package com.catface.rss.repository.service;

import com.catface.rss.common.enums.ResourceStatusEnum;
import com.catface.rss.repository.entity.Resource;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 资源 服务类
 * </p>
 *
 * @author catface
 * @since 2021-08-24
 */
public interface ResourceRpService extends IService<Resource> {

    /**
     * 更改资源状态
     *
     * @param resourceId 资源ID
     * @param size 资源大小
     * @param status 资源状态
     * @return 资源信息
     */
    Resource processOssCallback(Long resourceId, Integer size, ResourceStatusEnum status);
}
