package com.catface.rss.common.message;

import com.catface.rss.common.enums.AccessStatusEnum;
import com.catface.rss.common.enums.ResourceStatusEnum;
import lombok.Data;

/**
 * @author catface
 * @since 2021年01月21日 下午6:03
 */
@Data
public class ResourceMessage {
    /**
     * 资源id
     */
    private Long id;

    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 资源类别ID
     */
    private Long resourceTypeId;

    /**
     * 资源类别名称
     */
    private String resourceTypeName;

    /**
     * 资源类别编码
     */
    private String resourceTypeCode;

    /**
     * 文件大小,单位字节
     */
    private Integer size;

    /**
     * 资源状态,1:等待上传;2:上传完成
     */
    private ResourceStatusEnum status;

    /**
     * 访问状态,0:不可访问;1:可访问
     */
    private AccessStatusEnum accessStatus;

    /**
     * 风控内容
     */
    private String antiFraudContent;

}
