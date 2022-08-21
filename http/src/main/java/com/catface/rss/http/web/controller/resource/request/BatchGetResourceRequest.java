package com.catface.rss.http.web.controller.resource.request;

import java.util.Set;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.catface.rss.common.enums.AccessTermEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author catface
 * @since 2020/12/30
 */
@Data
@ApiModel(description = "批量获取资源信息请求")
public class BatchGetResourceRequest {

    @ApiModelProperty(value = "资源ID列表", required = true, example = "[1255046484550774785]")
    @NotEmpty(message = "资源ID列表不能为空")
    private Set<Long> resourceIds;

    @ApiModelProperty(value = "访问端位", required = true, example = "FRONT")
    @NotNull(message = "访问端位不能为空")
    private AccessTermEnum accessTerm;

}
