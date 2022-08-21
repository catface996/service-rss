package com.catface.rss.api.task.request;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author catface
 * @since 2020/4/28
 */
@Data
@ApiModel(description = "根据资源ID查询导入/导出任务请求")
public class GetTaskByResourceIdRequest {

    @ApiModelProperty(value = "资源ID", required = true, example = "1255046484550774785")
    @NotNull(message = "资源ID不能为空")
    private Long resourceId;

}
