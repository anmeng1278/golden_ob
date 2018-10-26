package com.jsj.member.ob.enums;

import com.jsj.member.ob.exception.FatalException;

public enum DeliveryStatus {

    //0未发货 10已发货 20已签收',

    UNDELIVERY(0, "未发货"),

    Delivered(10, "已发货"),

    SIGNED(20, "已签收");

    private Integer value;
    private String message;

    DeliveryStatus(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public Integer getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static DeliveryStatus valueOf(int value) {
        switch (value) {
            case 0:
                return UNDELIVERY;
            case 10:
                return Delivered;
            case 20:
                return SIGNED;
            default:
                throw new FatalException("未知的枚举值");
        }
    }
}
