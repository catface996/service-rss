package com.catface.rss.api.task.request;

import javax.validation.constraints.NotNull;

import com.catface.rss.common.enums.UpDownTaskStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author by catface
 * @since 2021/8/23 下午11:16 . All rights reserved.
 */
@Data
@ApiModel(description = "更改资源对应的任务状态请求")
public class ChangeTaskStatusByResourceIdRequest {

    @ApiModelProperty(value = "资源ID", required = true, example = "1255046484550774785")
    @NotNull(message = "资源ID不能为空")
    private Long resourceId;

    @ApiModelProperty(value = "任务状态 1:等待上传;2:文件上传到OSS成功;3:业务处理成功;4:业务处理失败;", required = true,
        example = "BIZ_PROCESS_SUCCESS")
    @NotNull(message = "任务状态不能为空")
    private UpDownTaskStatusEnum status;

}

