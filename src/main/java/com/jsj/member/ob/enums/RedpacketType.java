package com.jsj.member.ob.enums;

import com.jsj.member.ob.exception.FatalException;

public enum RedpacketType {

    COUPONPACKAGE(1, "券类红包");

    //CASHPACKAGE(2, "现金红包");

    private Integer value;

    private String message;

    RedpacketType(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public Integer getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static RedpacketType valueOf(int value) {
        switch (value) {
            case 1:
                return COUPONPACKAGE;
//            case 2:
//                return CASHPACKAGE;
            default:
                throw new FatalException("未知的枚举值");
        }
    }
}