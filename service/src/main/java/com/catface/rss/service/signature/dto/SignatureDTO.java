package com.catface.rss.service.signature.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author catface
 * @since 2019-01-31
 */
@Data
@ApiModel(description = "临时访问权限签名模型")
public class SignatureDTO {

    @ApiModelProperty(value = "资源ID", required = true)
    private Long resourceId;

    @ApiModelProperty(value = "接入ID", required = true)
    private String accessId;

    @ApiModelProperty(value = "访问策略", required = true)
    private String policy;

    @ApiModelProperty(value = "签名", required = true)
    private String signature;

    @ApiModelProperty(value = "对象路径", required = true)
    private String key;

    @ApiModelProperty(value = "接入点", required = true)
    private String host;

    @ApiModelProperty(value = "过期时间", required = true)
    private String expire;

    @ApiModelProperty(value = "回调地址", required = true)
    private String callback;

}
