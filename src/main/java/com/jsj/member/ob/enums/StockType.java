package com.jsj.member.ob.enums;

import com.jsj.member.ob.exception.FatalException;

public enum StockType {

    BUY(1, "购买"),

    GIFT(2, "获赠");

    private Integer value;
    private String message;

    StockType(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public Integer getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static StockType valueOf(int value) {

        switch (value) {
            case 1:
                return BUY;
            case 2:
                return GIFT;
            default:
                throw new FatalException("未知的枚举值");
        }
    }
}
