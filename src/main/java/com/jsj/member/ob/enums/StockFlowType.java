package com.jsj.member.ob.enums;

public enum StockFlowType {

    BUY(10, "购买成功"),

    GIVING(20, "赠送中"),

    GIVED(30, "赠送成功"),

    CANCEL(40, "取消赠送"),

    RECEVIED(50, "领取成功"),

    USED(60, "已使用");

    private Integer value;
    private String message;

    StockFlowType(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public Integer getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }


}
