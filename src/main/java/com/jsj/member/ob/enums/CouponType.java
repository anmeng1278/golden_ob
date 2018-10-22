package com.jsj.member.ob.enums;

import com.jsj.member.ob.exception.FatalException;

public enum CouponType {

    UNSET(0, "未设置"),

    CUT(1, "直减券"),

    DISCOUNT(2, "折扣券");

    private Integer value;

    private String message;

    CouponType(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public Integer getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static CouponType valueOf(int value) {
        switch (value) {
            case 0:
                return UNSET;
            case 1:
                return CUT;
            case 2:
                return DISCOUNT;
            default:
                throw new FatalException("未知的枚举值");
        }
    }
}