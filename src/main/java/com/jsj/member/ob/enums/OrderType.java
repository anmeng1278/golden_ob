package com.jsj.member.ob.enums;

public enum OrderType {

    NORMAL(0, "普通订单"),

    TEAMORDER(10, "团单"),

    SECORDER(20, "秒杀单");

    private Integer value;

    private String message;

    OrderType(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public Integer getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static OrderType valueOf(int value) {
        switch (value) {
            case 10:
                return TEAMORDER;
            case 20:
                return SECORDER;
            default:
                return NORMAL;
        }
    }
}