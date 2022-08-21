package com.catface.rss.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author catface
 * @since 2020/4/26
 */
@Getter
public enum CommonStatusEnum {

  /**
   * 通用状态 1:有效,0:无效;
   */
  VALID(1),

  INVALID(0),
  ;

  @EnumValue
  private Integer code;

  CommonStatusEnum(Integer code) {
    this.code = code;
  }
}
