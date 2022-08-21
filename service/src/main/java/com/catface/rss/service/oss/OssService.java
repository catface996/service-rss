package com.catface.rss.service.oss;

import java.util.List;
import java.util.Map;

import com.catface.rss.common.enums.AccessTermEnum;
import com.catface.rss.common.enums.NeedCallbackEnum;
import com.catface.rss.repository.entity.Resource;
import com.catface.rss.service.signature.dto.SignatureDTO;
import com.catface.rss.service.resource.dto.ResourceDTO;

/**
 * @author catface
 * @since 2020/4/27
 */
public interface OssService {

    /**
     * 生成资源的临时访问路径
     *
     * @param resourceStoreList 资源列表
     * @param accessTerm        访问资源的端位,前端公网,后端VPC
     * @param processString     oss媒体资源处理字符串（视频截图、图片处理）
     * @return 资源ID和访问路径关联关系
     */
    Map<Long, String> generateTempUrl(List<Resource> resourceStoreList, AccessTermEnum accessTerm,
                                      String processString);

    /**
     * 生成资源的访问路径
     *
     * @param resource      资源对象
     * @param accessTerm    访问资源的端位,前端公网,后端VPC
     * @param processString oss媒体资源处理字符串（视频截图、图片处理）
     * @return 资源的访问路径
     */
    String generateTempUrl(Resource resource, AccessTermEnum accessTerm, String processString);

    /**
     * 生成临时访问oss的授权签名
     *
     * @param accessTerm   访问端位,前端or后端
     * @param bucket       bucket
     * @param objectName   访问文件路径
     * @param resourceId   资源ID
     * @param needCallback 是否需要回调
     * @return 临时访问权限的签名
     */
    SignatureDTO generateSignature(AccessTermEnum accessTerm, String bucket, String objectName, Long resourceId,
                                   NeedCallbackEnum needCallback);

    /**
     * 获取资源信息
     *
     * @param resource   资源对象
     * @param accessTerm 访问资源的端位,前端公网,后端VPC
     * @return 资源信息
     */
    ResourceDTO getResource(Resource resource, AccessTermEnum accessTerm);

    /**
     * 批量获取资源信息
     *
     * @param resourceList 资源列表
     * @param accessTerm   访问资源的端位,前端公网,后端VPC
     * @return 资源id->资源信息
     */
    Map<Long, ResourceDTO> getResources(List<Resource> resourceList, AccessTermEnum accessTerm);

    /**
     * 设置公有bucket资源访问权限为私有读
     *
     * @param resource 资源
     */
    void setResourceAcl(Resource resource);

}
