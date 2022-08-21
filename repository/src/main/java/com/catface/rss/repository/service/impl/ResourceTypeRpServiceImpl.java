package com.catface.rss.repository.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.catface.rss.repository.entity.ResourceType;
import com.catface.rss.repository.mapper.ResourceTypeMapper;
import com.catface.rss.repository.service.ResourceTypeRpService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 资源类别 服务实现类
 * </p>
 *
 * @author catface
 * @since 2021-08-24
 */
@Service
public class ResourceTypeRpServiceImpl extends ServiceImpl<ResourceTypeMapper, ResourceType>
    implements ResourceTypeRpService {

    /**
     * 根据业务码查询
     *
     * @param typeCode 资源类型编码,业务编码
     * @return 资源类型
     */
    @Override
    public ResourceType queryByBizCode(String typeCode) {
        return baseMapper.selectByCode(typeCode);
    }
}
