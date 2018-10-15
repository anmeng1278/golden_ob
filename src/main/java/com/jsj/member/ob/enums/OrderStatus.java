package com.jsj.member.ob.enums;

public enum OrderStatus {

    //订单状态，0：待支付 10：支付成功 20：支付失败 60：取消订单

    UNPAY(0, "待支付"),

    PAYSUCCESS(10, "支付成功"),

    PAYFAIL(20, "支付失败"),

    CANCEL(60, "取消订单");

    private Integer value;

    private String message;

    OrderStatus(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public Integer getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static OrderStatus valueOf(int value) {
        switch (value) {
            case 10:
                return PAYSUCCESS;
            case 20:
                return PAYFAIL;
            case 60:
                return CANCEL;
            default:
                return UNPAY;
        }
    }
}