package com.catface.rss.service.resource.param;

import java.util.Set;

import javax.validation.constraints.NotEmpty;
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
@ApiModel(description = "批量获取资源的路径参数")
public class BatchGetResourceUrlParam {

    @ApiModelProperty(value = "资源ID列表", required = true)
    @NotEmpty(message = "资源ID列表不能为空")
    private Set<Long> resourceIds;

    @ApiModelProperty(value = "访问端位", required = true)
    @NotNull(message = "访问端位不能为空")
    private AccessTermEnum accessTerm;

    @ApiModelProperty(value = "oss媒体资源处理字符串（视频截图、图片处理）")
    private String processString;

}
