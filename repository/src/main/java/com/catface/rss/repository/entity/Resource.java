package com.catface.rss.repository.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.catface.rss.common.enums.AccessStatusEnum;
import com.catface.rss.common.enums.ResourceStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 资源
 * </p>
 *
 * @author catface
 * @since 2021-08-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "Resource对象", description = "资源")
public class Resource implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "资源类别ID")
    private Long resourceTypeId;

    @ApiModelProperty(value = "bucket名称")
    private String bucketName;

    @ApiModelProperty(value = "资源名称")
    private String resourceName;

    @ApiModelProperty(value = "资源存储路径")
    private String resourcePath;

    @ApiModelProperty(value = "文件大小,单位字节")
    private Integer size;

    @ApiModelProperty(value = "资源状态,1:等待上传;2:上传完成")
    private ResourceStatusEnum status;

    @ApiModelProperty(value = "访问状态,0:不可访问;1:可访问")
    private AccessStatusEnum accessStatus;

    @ApiModelProperty(value = "上传完成时间")
    private Date uploadFinishTime;

    @ApiModelProperty(value = "风控内容")
    private String antiFraudContent;

    @ApiModelProperty(value = "创建时间")
    private Date gmtCreate;

    @ApiModelProperty(value = "修改时间")
    private Date gmtModified;

    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty(value = "修改人")
    private String modifier;

    @ApiModelProperty(value = "备注")
    private String remark;

}
