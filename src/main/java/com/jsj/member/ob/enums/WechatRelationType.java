package com.jsj.member.ob.enums;

import com.jsj.member.ob.exception.FatalException;

public enum WechatRelationType {

    AWKMINI(1, "空铁小程序"),

    GOLDENMINI(2, "金色世纪小程序"),

    GOLDENTC(3, "金色世纪公众号");


    private Integer value;

    private String message;

    WechatRelationType(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public Integer getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static WechatRelationType valueOf(int value) {
        switch (value) {
            case 1:
                return AWKMINI;
            case 2:
                return GOLDENMINI;
            case 3:
                return GOLDENTC;
            default:
                throw new FatalException("未知的枚举值");
        }
    }
}