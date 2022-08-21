package com.catface.rss.repository.mapper;

import com.catface.rss.repository.entity.ResourceType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 资源类别 Mapper 接口
 * </p>
 *
 * @author catface
 * @since 2021-08-24
 */
public interface ResourceTypeMapper extends BaseMapper<ResourceType> {

    /**
     * 根据资源类型编码查询
     *
     * @param typeCode 资源类型编码
     * @return 资源类型
     */
    ResourceType selectByCode(@Param("typeCode") String typeCode);
}
