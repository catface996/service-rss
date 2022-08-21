package com.catface.rss.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @author catface
 * @since 2019-01-31
 */
public enum AccessTypeEnum {

  /**
   * bucket访问类别
   */
  PUBLIC(1, "公共读"),

  PRIVATE(0, "私有读"),

  ;

  @EnumValue
  private final Integer code;

  private final String message;

  AccessTypeEnum(Integer code, String message) {
    this.code = code;
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public Integer getCode() {
    return code;
  }
}
