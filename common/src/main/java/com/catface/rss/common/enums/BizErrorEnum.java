package com.catface.rss.common.enums;

import com.catface.common.exception.BaseErrorEnum;

/**
 * @author by catface
 * @since 2020/12/17
 */
public enum BizErrorEnum implements BaseErrorEnum {

	;

	private final String message;

	BizErrorEnum(String message) {
		this.message = message;
	}

	@Override
	public String getCode() {
		return name();
	}

	@Override
	public String getMessage() {
		return message;
	}
}