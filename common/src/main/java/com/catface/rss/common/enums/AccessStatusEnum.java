package com.catface.rss.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @author catface
 * @since 2021-01-25
 */
public enum AccessStatusEnum {

  /**
   * 访问状态,0:不可访问;1:可访问
   */
  ACCESS(1, "可访问"),

  NO_ACCESS(0, "不可访问"),

  ;

  @EnumValue
  private final Integer code;

  private final String message;

  AccessStatusEnum(Integer code, String message) {
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
