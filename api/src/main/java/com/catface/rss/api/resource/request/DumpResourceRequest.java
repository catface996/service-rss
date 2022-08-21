package com.catface.rss.api.resource.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author by catface
 * @since 2021/10/20 下午2:27 . All rights reserved.
 */
@Data
@ApiModel(description = "转储资源信息请求")
public class DumpResourceRequest {

    @ApiModelProperty(value = "业务码", required = true, example = "TEST_CODE")
    @NotBlank(message = "业务码不能为空")
    private String typeCode;

    @ApiModelProperty(value = "资源url", required = true,
        example = "https://catface-static-dev.oss-cn-hangzhou.aliyuncs.com/export/template/template.xlsx")
    @NotBlank(message = "资源url不能为空")
    private String resourceUrl;

    @ApiModelProperty(value = "对象名称", required = true, example = "导入库区.xlsx")
    @NotBlank(message = "对象名称不能为空")
    @Length(max = 128, message = "文件名称不能大于128个字符串")
    private String resourceName;

    @ApiModelProperty(value = "资源内容类型,允许为空,默认是: multipart/form-data", example = "multipart/form-data")
    private String contentType;

    @ApiModelProperty(value = "操作人", required = true, example = "1299898989889898989")
    @NotNull(message = "操作人不能为空")
    private Long creator;

}
