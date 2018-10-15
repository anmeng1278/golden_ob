package com.jsj.member.ob.enums;

public enum CouponStatus {

    UNUSE(0, "未使用"),

    USED(10, "已使用"),

    EXPIRED(60, "已过期");

    private Integer value;

    private String message;

    CouponStatus(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public Integer getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static CouponStatus valueOf(int value) {
        switch (value) {
            case 10:
                return USED;
            case 60:
                return EXPIRED;
            default:
                return UNUSE;
        }
    }
}