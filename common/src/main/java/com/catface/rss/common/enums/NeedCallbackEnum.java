package com.catface.rss.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author catface
 * @since 2020/4/26
 */
@Getter
public enum NeedCallbackEnum {

  /**
   * 是否需要回调 1:需要;2:不需要;
   */
  NEED(1),

  NO(2),
  ;

  @EnumValue
  private Integer code;

  NeedCallbackEnum(Integer code) {
    this.code = code;
  }
}
