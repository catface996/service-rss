package com.catface.rss.http.web.controller.resource.request;

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
@ApiModel(description = "获取资源的访问路径请求")
public class GetResourceUrlRequest {

    @ApiModelProperty(value = "资源ID", required = true, example = "1255046484550774785")
    @NotNull(message = "资源ID不能为空")
    private Long resourceId;

    @ApiModelProperty(value = "访问资源的终端类型", required = true, example = "FRONT")
    @NotNull(message = "访问资源的终端类型不能为空")
    private AccessTermEnum accessTerm;

    @ApiModelProperty(value = "oss媒体资源处理字符串（视频截图、图片处理）")
    private String processString;

}
