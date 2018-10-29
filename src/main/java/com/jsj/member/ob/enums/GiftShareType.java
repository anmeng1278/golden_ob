package com.jsj.member.ob.enums;

import com.jsj.member.ob.exception.FatalException;

public enum GiftShareType {

    UNSEND(0, "未赠送"),

    FRIEND(1, "好友"),

    GROUP(2, "群发");

    private Integer value;
    private String message;

    GiftShareType(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public Integer getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static GiftShareType valueOf(int value) {

        switch (value) {
            case 0:
                return UNSEND;
            case 1:
                return FRIEND;
            case 2:
                return GROUP;
            default:
                throw new FatalException("未知的枚举值");
        }
    }
}
