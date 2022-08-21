package com.catface.rss.service.resource;

import java.util.Map;

import com.catface.rss.service.resource.dto.ResourceDTO;
import com.catface.rss.service.resource.param.BatchGetResourceParam;
import com.catface.rss.service.resource.param.BatchGetResourceUrlParam;
import com.catface.rss.service.resource.param.ChangeResourceAccessStatusParam;
import com.catface.rss.service.resource.param.DumpResourceParam;
import com.catface.rss.service.resource.param.GetResourceParam;
import com.catface.rss.service.resource.param.GetResourceUrlParam;

/**
 * @author catface
 * @since 2020/4/28
 */
public interface ResourceService {

    /**
     * 获取资源的访问路径
     *
     * @param param 资源ID,访问资源的终端类型,前端公网,后端VPC
     * @return 资源对应的路径
     */
    String getUrl(GetResourceUrlParam param);

    /**
     * 获取资源信息
     *
     * @param param 资源ID,访问资源的终端类型,前端公网,后端VPC
     * @return 资源信息
     */
    ResourceDTO getResource(GetResourceParam param);

    /**
     * 批量获取资源的访问路径
     *
     * @param param 资源ID列表,访问资源的终端类型,前端公网,后端VPC
     * @return 资源->资源路径
     */
    Map<Long, String> batchGetUrls(BatchGetResourceUrlParam param);

    /**
     * 批量获取资源信息
     *
     * @param param 资源ID列表,访问资源的终端类型,前端公网,后端VPC
     * @return 资源id->资源信息
     */
    Map<Long, ResourceDTO> batchGetResources(BatchGetResourceParam param);

    /**
     * 设置资源访问状态
     *
     * @param param 资源ID,访问状态
     */
    void changeAccessStatus(ChangeResourceAccessStatusParam param);

    /**
     * 根据资源url转储资源到OSS
     *
     * @param param 业务码,资源URL
     * @return 资源ID
     */
    Long dumpResource(DumpResourceParam param);

}
