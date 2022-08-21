package com.catface.rss.service.consts;

import com.catface.common.exception.BaseErrorEnum;

/**
 * @author by catface
 * @since 2021/8/16 9:29 下午
 * <p>
 * Copyright 2020   Inc. All rights reserved.
 */
public enum RssErrorEnum implements BaseErrorEnum {

    ;

    private final String code;
    private final String message;

    RssErrorEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
