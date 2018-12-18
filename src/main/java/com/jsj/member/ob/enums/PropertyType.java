package com.jsj.member.ob.enums;

import com.jsj.member.ob.exception.FatalException;

public enum PropertyType {

    ENTITY(1, "实物"),

    ACTIVITYCODE(2, "活动码"),

    GOLDENCARD(3, "会员卡");

    private Integer value;

    private String message;

    PropertyType(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public Integer getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static PropertyType valueOf(int value) {
        switch (value) {
            case 1:
                return ENTITY;
            case 2:
                return ACTIVITYCODE;
            case 3:
                return GOLDENCARD;
            default:
                throw new FatalException("未知的枚举值");
        }
    }
}