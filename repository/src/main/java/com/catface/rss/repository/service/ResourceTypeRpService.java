package com.catface.rss.repository.service;

import com.catface.rss.repository.entity.ResourceType;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 资源类别 服务类
 * </p>
 *
 * @author catface
 * @since 2021-08-24
 */
public interface ResourceTypeRpService extends IService<ResourceType> {

    /**
     * 根据业务码查询
     *
     * @param typeCode 资源类型编码,业务编码
     * @return 资源类型
     */
    ResourceType queryByBizCode(String typeCode);
}
