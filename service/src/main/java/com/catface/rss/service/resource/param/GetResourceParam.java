package com.catface.rss.service.resource.param;

import javax.validation.constraints.NotNull;

import com.catface.rss.common.enums.AccessTermEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author catface
 * @since 2020/4/28
 */
@Data
@ApiModel(description = "获取资源信息")
public class GetResourceParam {

    @ApiModelProperty(value = "资源ID", required = true)
    @NotNull(message = "资源ID不能为空")
    private Long resourceId;

    @ApiModelProperty(value = "访问资源的终端类型", required = true)
    @NotNull(message = "访问资源的终端类型不能为空")
    private AccessTermEnum accessTerm;

}
