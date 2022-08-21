package com.catface.rss.api.signature.request;

import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author catface
 * @since 2020/4/27
 */
@Data
@ApiModel(description = "获取上传文件签名的的请求")
public class GetSignatureRequest {

    @ApiModelProperty(value = "业务码", required = true, example = "TEST_CODE")
    @NotBlank(message = "业务码不能为空")
    private String typeCode;

    @ApiModelProperty(value = "对象名称", required = true, example = "导入库区.xlsx")
    @NotBlank(message = "对象名称不能为空")
    @Length(max = 128, message = "文件名称不能大于128个字符串")
    private String resourceName;

    @ApiModelProperty(value = "是否需要创建任务", required = true)
    @NotNull(message = "需传入是否创建任务")
    private Boolean createTask;

    @ApiModelProperty(value = "任务描述")
    private String taskDesc;

    @ApiModelProperty(value = "用于生成资源路径的参数")
    private Map<String, String> pathParam;

    @ApiModelProperty(value = "操作人", required = true, example = "1299898989889898989")
    @NotNull(message = "操作人不能为空")
    private String creator;

}
