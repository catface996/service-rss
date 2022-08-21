package com.catface.rss.http.web.controller.resource.vo;

import java.util.Map;

import com.catface.rss.common.enums.ResourceStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author catface
 * @since 2020-12-28
 */
@Data
@ApiModel(description = "资源信息")
public class ResourceVO {

    @ApiModelProperty(value = "资源ID")
    private Long resourceId;

    @ApiModelProperty(value = "资源访问地址")
    private String url;

    @ApiModelProperty(value = "文件大小")
    private Integer size;

    @ApiModelProperty(value = "资源MD5")
    private String md5;

    @ApiModelProperty(value = "资源元信息")
    private Map<String, Object> metadata;

    @ApiModelProperty(value = "资源状态,1:等待上传;2:上传完成")
    private ResourceStatusEnum status;

}
