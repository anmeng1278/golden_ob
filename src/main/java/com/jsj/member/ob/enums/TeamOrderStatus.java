package com.jsj.member.ob.enums;

public enum TeamOrderStatus {

    //组团状态 0 组团中  10 组团成功  20 组团失败   60 已取消

    TEAMING(0, "组团中"),

    SUCCESS(10, "组团成功"),

    FAIL(20, "组团失败"),

    ORDERSUCCESS(30, "订单成功"),

    CANCEL(60, "已取消");

    private Integer value;

    private String message;

    TeamOrderStatus(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public Integer getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static TeamOrderStatus valueOf(int value) {
        switch (value) {
            case 10:
                return SUCCESS;
            case 20:
                return FAIL;
            case 30:
                return ORDERSUCCESS;
            case 60:
                return CANCEL;
            default:
                return TEAMING;
        }
    }
}