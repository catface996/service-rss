package com.catface.rss.api.task.request;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author by catface
 * @since 2021/8/23 下午11:22 . All rights reserved.
 */
@Data
@ApiModel(description = "更新对文件处理之后封装的错误文件资源ID请求")
public class UpdateErrorResourceByTaskIdRequest {

    @ApiModelProperty(value = "任务ID", required = true, example = "1255046484550774785")
    @NotNull(message = "任务ID不能为空")
    private Long taskId;

    @ApiModelProperty(value = "错误结果文件对应的资源ID", required = true, example = "1255046484550774788")
    @NotNull(message = "错误结果文件对应的资源ID不能为空")
    private Long errorResourceId;

}
