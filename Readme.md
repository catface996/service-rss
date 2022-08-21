# Getting Started

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.4.1/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.4.1/maven-plugin/reference/html/#build-image)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/2.4.1/reference/htmlsingle/#production-ready)
* [Spring Configuration Processor](https://docs.spring.io/spring-boot/docs/2.4.1/reference/htmlsingle/#configuration-metadata-annotation-processor)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.4.1/reference/htmlsingle/#boot-features-developing-web-applications)
* [Cloud Bootstrap](https://spring.io/projects/spring-cloud-commons)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/2.4.1/reference/htmlsingle/#using-boot-devtools)
* [Prometheus](https://docs.spring.io/spring-boot/docs/2.4.1/reference/html/production-ready-features.html#production-ready-metrics-export-prometheus)

### Guides

The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)

### 代码生成

GeneratorServiceEntity.generateAllCode 会生dao层的相关代码

### 自定义banner

[自定义banner传送门](http://patorjk.com/software/taag/#p=display&f=Soft&t=rss)

建议使用Soft字体,将生成的内容复制到banner.txt即可,文件路径 app/src/main/resources/banner.txt

### 版本号更新

1.更新所有模块版本号：输入命令：mvn versions:set -DnewVersion=0.0.3-SNAPSHOT

2.如果还有没有修改到的。执行下方的命令重试。

更新子模块版本号：输入命令：mvn versions:update-child-modules

3.如果你后悔更新了。

输入命令：mvn versions:revert 可以回退到上一个版本号

4.如果你觉得OK了。

输入命令：mvn versions:commit 就可以提交版本号了。

切记：提交之后，就无法回退到上个版本号了。

# rss(资源存储服务)

## 目录
-   [原有文件上传/下载的若干问题](#原有文件上传/下载的若干问题)
-   [用例图](#用例图)
-   [系统边界](#系统边界)
-   [系统交互时序](#系统交互时序)
-   [数据库模型](#数据库模型)
-   [资源配置](#资源配置)
-   [OSS相关文档](#OSS相关文档)

## 原有文件上传/下载的若干问题

1.目前的实现方案是公共jar包,各自系统配置oss访问的accessKey 和 secretKey,不便于统一维护

2.需要各自系统实现获取签名和接收回调处理

3.需要各自系统记录文件存储的bucket和在bucket中的路径

4.需要各自完成资源审计

5.OSS支公网和VPC访问,支持绑定域名,目前需要各自业务系统来选择网络和域名

6.OSS支持公共读,和私有读写,目前也需要各自业务系统来选择

7.各自维护导入导出记录

## 用例

![image-20220821134939464](/Users/catface/Library/Application%20Support/typora-user-images/image-20220821134939464.png)

## 系统边界

![image-20220821134951018](https://tva1.sinaimg.cn/large/e6c9d24ely1h5ecb18gdvj20og0j23zk.jpg)

## 系统交互时序

### 前端异步上环文件

![image-20220821135010777](https://tva1.sinaimg.cn/large/e6c9d24ely1h5ecbaowhaj20uq0kiwfz.jpg)

### 服务端上传文件(常用于导出)

![image-20220821135024531](https://tva1.sinaimg.cn/large/e6c9d24ely1h5ecbjm3rej20wd0qdtb0.jpg)

## 数据库模型

![image-20220821135038684](https://tva1.sinaimg.cn/large/e6c9d24ely1h5ecbti842j210o0u0djs.jpg)

~~~mysql

create table resource
(
    id                 bigint                                 not null comment '主键'
        primary key,
    resource_type_id   bigint                                 not null comment '资源类别ID',
    bucket_name        varchar(64)                            not null comment 'bucket名称',
    resource_name      varchar(128)                           not null comment '资源名称',
    resource_path      varchar(256)                           not null comment '资源存储路径',
    size               int          default 0                 not null comment '文件大小,单位字节',
    status             tinyint                                not null comment '资源状态,1:等待上传;2:上传完成',
    access_status      tinyint      default 1                 not null comment '访问状态,0:不可访问;1:可访问',
    upload_finish_time datetime                               null comment '上传完成时间',
    anti_fraud_content text                                   null comment '风控内容',
    gmt_create         datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    gmt_modified       datetime     default CURRENT_TIMESTAMP not null comment '修改时间',
    creator            bigint                                 not null comment '创建人',
    modifier           bigint                                 not null comment '修改人',
    remark             varchar(128) default ''                not null comment '备注'
)
    comment '资源'  collate = utf8_bin;
    
create table resource_type
(
    id                    bigint                                 not null comment '主键'
        primary key,
    type_name             varchar(128)                           not null comment '资源类别名称',
    type_code             varchar(64)                            not null comment '资源类别编码',
    belong_to_biz_domain  varchar(64)                            not null comment '所属业务域',
    up_down               int                                    not null comment '对用户来说,上传or下载,1:上传;2:下载,',
    bucket_name           varchar(64)                            not null comment 'bucket名称',
    access_type           int          default 0                 not null comment '访问类型,1:公共读,0:私有读',
    public_access_domain  varchar(128)                           not null comment '公网访问域名',
    private_access_domain varchar(128)                           not null comment 'VPC网络访问域名',
    upload_term           tinyint                                not null comment '上传端位,1:前端WEB;2:后端',
    need_callback         tinyint      default 1                 not null comment '是否需要接收回调,1:需要;2:不需要',
    path_pattern          varchar(128)                           not null comment '存储路径格式',
    gmt_create            datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    gmt_modified          datetime     default CURRENT_TIMESTAMP not null comment '修改时间',
    status                tinyint      default 1                 not null comment '状态,1:有效,0:无效',
    default_access_status tinyint      default 1                 not null comment '默认访问状态,0:不可访问;1:可访问',
    remark                varchar(128) default ''                not null comment '备注',
    constraint resource_type_type_code_uindex
        unique (type_code)
) 
    comment '资源类别'  collate = utf8_bin;
    
create table upload_download_task
(
    id                 bigint                                 not null comment '任务ID'
        primary key,
    resource_id        bigint                                 not null comment '原始资源ID,如果是上传,即上传的原始文件',
    task_type          varchar(64)                            not null comment '任务类型',
    upload_or_download tinyint                                not null comment '任务类型,1:上传;2:下载',
    task_desc          varchar(128) default ''                not null comment '任务描述',
    status             tinyint                                not null comment '任务状态,1:初始状态;2:资源就绪;',
    error_resource_id  bigint                                 null comment '处理失败后,供下载的错误文件对应的资源ID',
    gmt_create         datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    gmt_modified       datetime     default CURRENT_TIMESTAMP not null comment '修改时间',
    creator            bigint                                 not null comment '创建人',
    modifier           bigint                                 not null comment '修改人',
    remark             varchar(128) default ''                not null comment '备注',
    constraint upload_download_task_resource_id_uindex
        unique (resource_id)
)
    comment '上传/下载任务'  collate = utf8_bin;

~~~

## 获取签名的返回数据格式

~~~java

@Data
@ApiModel(description = "临时访问权限签名模型")
public class SignatureVO {
  
  @ApiModelProperty(value = "任务ID", required = true)
  private Long taskId;

  @ApiModelProperty(value = "资源ID", required = true)
  private Long resourceId;

  @ApiModelProperty(value = "接入ID", required = true)
  private String accessId;

  @ApiModelProperty(value = "访问策略", required = true)
  private String policy;

  @ApiModelProperty(value = "签名", required = true)
  private String signature;

  @ApiModelProperty(value = "对象路径", required = true)
  private String key;

  @ApiModelProperty(value = "接入点", required = true)
  private String host;

  @ApiModelProperty(value = "过期时间", required = true)
  private String expire;

  @ApiModelProperty(value = "回调地址", required = false)
  private String callback;

}
~~~

## OSS相关文档

[服务端上传文件到OSS](https://help.aliyun.com/document_detail/112718.html?spm=a2c4g.11186623.6.1520.21da406cEEuFld)

[服务端签名后前端上传文件到OSS](https://help.aliyun.com/document_detail/31926.html?spm=a2c4g.11186623.6.1524.b76574b8HBS551)
