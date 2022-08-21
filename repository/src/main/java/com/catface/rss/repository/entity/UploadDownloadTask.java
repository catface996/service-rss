package com.catface.rss.repository.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.catface.rss.common.enums.UpDownEnum;
import com.catface.rss.common.enums.UpDownTaskStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 上传/下载任务
 * </p>
 *
 * @author catface
 * @since 2021-08-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "UploadDownloadTask对象", description = "上传/下载任务")
public class UploadDownloadTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "任务ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "原始资源ID,如果是上传,即上传的原始文件")
    private Long resourceId;

    @ApiModelProperty(value = "任务类型")
    private String taskType;

    @ApiModelProperty(value = "任务类型,1:上传;2:下载")
    private UpDownEnum uploadOrDownload;

    @ApiModelProperty(value = "任务描述")
    private String taskDesc;

    @ApiModelProperty(value = "任务状态,1:等待上传;2:文件上传到OSS成功;3:业务处理成功;4:业务处理失败;")
    private UpDownTaskStatusEnum status;

    @ApiModelProperty(value = "处理失败后,供下载的错误文件对应的资源ID")
    private Long errorResourceId;

    @ApiModelProperty(value = "创建时间")
    private Date created;

    @ApiModelProperty(value = "修改时间")
    private Date updated;

    @ApiModelProperty(value = "创建人")
    private Long creator;

    @ApiModelProperty(value = "修改人")
    private Long modifier;

    @ApiModelProperty(value = "备注")
    private String remark;

}
