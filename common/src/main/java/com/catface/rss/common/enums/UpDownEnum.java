package com.catface.rss.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author catface
 * @since 2020/4/27
 */
@Getter
public enum UpDownEnum {

  /**
   * 任务类型
   */
  UPLOAD(1),

  DOWNLOAD(2),
  ;

  @EnumValue
  private Integer code;

  UpDownEnum(Integer code) {
    this.code = code;
  }
}
