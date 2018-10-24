package com.jsj.member.ob.enums;

import com.jsj.member.ob.exception.FatalException;

public enum  WechatType {

    SOURCE_NORMAL(0, "正常关注"),

    SOURCE_SCAN(1, "扫码关注"),

    SOURCE_OAUTH(2, "授权加入");

    private Integer value;
    private String message;

    WechatType(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public Integer getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static WechatType valueOf(int value) {

        switch (value) {
            case 0:
                return SOURCE_NORMAL;
            case 1:
                return SOURCE_SCAN;
            case 2:
                return SOURCE_OAUTH;
            default:
                throw new FatalException("未知的枚举值");
        }
    }

}
