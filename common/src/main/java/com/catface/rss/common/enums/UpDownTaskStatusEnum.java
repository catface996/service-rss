package com.catface.rss.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author catface
 * @since 2020/4/26
 */
@Getter
public enum UpDownTaskStatusEnum {

  /**
   * 文件上传/下载任务状态 1:等待上传;2:文件上传到OSS成功;3:业务处理成功;4:业务处理失败;
   */
  INIT(1),

  READY(2),

  BIZ_PROCESS_SUCCESS(3),

  BIZ_PROCESS_FAIL(4),
  ;

  @EnumValue
  private Integer code;

  UpDownTaskStatusEnum(Integer code) {
    this.code = code;
  }
}
