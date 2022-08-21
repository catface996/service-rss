package com.catface.rss.repository.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.catface.rss.common.enums.AccessStatusEnum;
import com.catface.rss.common.enums.AccessTermEnum;
import com.catface.rss.common.enums.AccessTypeEnum;
import com.catface.rss.common.enums.CommonStatusEnum;
import com.catface.rss.common.enums.NeedCallbackEnum;
import com.catface.rss.common.enums.UpDownEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 资源类别
 * </p>
 *
 * @author catface
 * @since 2021-08-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "ResourceType对象", description = "资源类别")
public class ResourceType implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "资源类别名称")
    private String typeName;

    @ApiModelProperty(value = "资源类别编码")
    private String typeCode;

    @ApiModelProperty(value = "所属业务域")
    private String belongToBizDomain;

    @ApiModelProperty(value = "对用户来说,上传or下载,1:上传;2:下载,")
    private UpDownEnum upDown;

    @ApiModelProperty(value = "bucket名称")
    private String bucketName;

    @ApiModelProperty(value = "访问类型,1:公共读,0:私有读")
    private AccessTypeEnum accessType;

    @ApiModelProperty(value = "公网访问域名")
    private String publicAccessDomain;

    @ApiModelProperty(value = "VPC网络访问域名")
    private String privateAccessDomain;

    @ApiModelProperty(value = "上传端位,1:前端WEB;2:后端")
    private AccessTermEnum uploadTerm;

    @ApiModelProperty(value = "是否需要接收回调,1:需要;2:不需要")
    private NeedCallbackEnum needCallback;

    @ApiModelProperty(value = "存储路径格式")
    private String pathPattern;

    @ApiModelProperty(value = "创建时间")
    private Date created;

    @ApiModelProperty(value = "修改时间")
    private Date updated;

    @ApiModelProperty(value = "状态,1:有效,0:无效")
    private CommonStatusEnum status;

    @ApiModelProperty(value = "默认访问状态,0:不可访问;1:可访问")
    private AccessStatusEnum defaultAccessStatus;

    @ApiModelProperty(value = "备注")
    private String remark;

}
