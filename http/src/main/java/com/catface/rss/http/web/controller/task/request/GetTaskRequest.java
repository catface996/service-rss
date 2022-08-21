package com.catface.rss.http.web.controller.task.request;

import java.util.Set;

import javax.validation.constraints.NotBlank;

import com.catface.common.model.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author catface
 * @since 2020/4/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "分页查询任务")
public class GetTaskRequest extends PageRequest {

    @ApiModelProperty(value = "任务类型", required = true, example = "TEST_CODE")
    @NotBlank(message = "任务类型不能为空")
    private String taskType;

    @ApiModelProperty(value = "创建人")
    private Set<String> creators;

}
