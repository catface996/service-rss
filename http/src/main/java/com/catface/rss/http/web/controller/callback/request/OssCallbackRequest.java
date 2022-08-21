package com.catface.rss.http.web.controller.callback.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author catface
 * @since 2020/4/28
 */
@Data
@ApiModel(description = "OSS回调请求")
public class OssCallbackRequest {

  @ApiModelProperty(value = "oss bucket", required = true, example = "xmdev-resource")
  private String bucket;

  @ApiModelProperty(value = "存储在oss的objectName", required = true, example = "inst/1090869416918245378/class/456789/20190213183924/nohup.out")
  private String object;

  @ApiModelProperty(value = "oss etag", required = true)
  private String etag;

  @ApiModelProperty(value = "资源大小(单位:字节)", required = true, example = "3374")
  private Integer size;

  @ApiModelProperty(value = "资源ID", required = true, example = "1095633535147950082")
  private Long resourceId;

}
