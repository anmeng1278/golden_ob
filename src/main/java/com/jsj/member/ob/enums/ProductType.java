package com.jsj.member.ob.enums;

import com.jsj.member.ob.exception.FatalException;

public enum ProductType {

    QUALITYTRAVELS(1, "品质出行"),

    HOTPRODUCT(2,"爆款单品");

    private Integer value;

    private String message;

    ProductType(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public Integer getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static ProductType valueOf(int value) {
        switch (value) {
            case 1:
                return QUALITYTRAVELS;
            case 2:
                return HOTPRODUCT;
            default:
                throw new FatalException("未知的枚举值");
        }
    }
}