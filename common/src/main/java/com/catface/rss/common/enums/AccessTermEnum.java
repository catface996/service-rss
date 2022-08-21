package com.catface.rss.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author catface
 * @since 2020/4/26
 */
@Getter
public enum AccessTermEnum {

  /**
   * 文件上传的端位 1:前端;2:后端
   */
  FRONT(1),

  BACKEND(2),
  ;

  @EnumValue
  private Integer code;

  AccessTermEnum(Integer code) {
    this.code = code;
  }
}
