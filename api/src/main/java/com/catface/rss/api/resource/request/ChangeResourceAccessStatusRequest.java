package com.catface.rss.api.resource.request;

import javax.validation.constraints.NotNull;

import com.catface.rss.common.enums.AccessStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author catface
 * @since 2021年01月22日 上午11:24
 */
@Data
@ApiModel(description = "设置资源访问状态请求")
public class ChangeResourceAccessStatusRequest {

    @ApiModelProperty(value = "资源ID", required = true, example = "1255046484550774785")
    @NotNull(message = "资源ID不能为空")
    private Long resourceId;

    @ApiModelProperty(value = "访问状态", required = true, example = "ACCESS")
    @NotNull(message = "访问状态不能为空")
    private AccessStatusEnum accessStatus;

    @ApiModelProperty(value = "风控内容")
    private String antiFraudContent;

    @ApiModelProperty(value = "操作人", required = true, example = "1299898989889898989")
    @NotNull(message = "操作人不能为空")
    private String operator;

}
