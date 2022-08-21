package com.catface.rss.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author catface
 * @since 2020/4/26
 */
@Getter
public enum ResourceStatusEnum {

  /**
   * 资源状态枚举值
   */
  INIT(1),

  UPLOAD_SUCCESS(2),

  ;
  @EnumValue
  private Integer code;

  ResourceStatusEnum(Integer code) {
    this.code = code;
  }
}
