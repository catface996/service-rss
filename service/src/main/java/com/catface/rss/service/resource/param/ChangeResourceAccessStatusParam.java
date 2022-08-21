package com.catface.rss.service.resource.param;

import com.catface.rss.common.enums.AccessStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author catface
 * @since 2021年01月22日 上午11:24
 */
@Data
@ApiModel(description = "设置资源访问状态")
public class ChangeResourceAccessStatusParam {

    @ApiModelProperty(value = "资源ID", required = true)
    @NotNull(message = "资源ID不能为空")
    private Long resourceId;

    @ApiModelProperty(value = "访问状态", required = true)
    @NotNull(message = "访问状态不能为空")
    private AccessStatusEnum accessStatus;

    @ApiModelProperty(value = "风控内容")
    private String antiFraudContent;

    @ApiModelProperty(value = "操作人", required = true)
    @NotNull(message = "操作人不能为空")
    private Long operator;

}
