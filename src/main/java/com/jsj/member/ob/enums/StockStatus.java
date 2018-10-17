package com.jsj.member.ob.enums;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public enum StockStatus {

    //0 未使用  10 已赠送(未领取)  11 已领取  20 已使用(提货)  30 已发货',

    UNUSE(0, "未使用"),

    GIVED(10,"已赠送"),

    RECEIVED(11, "已领取"),

    USED(20, "已使用"),

    SENT(30,"已发货");

    private Integer value;

    private String message;

    StockStatus(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public Integer getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static StockStatus valueOf(int value) {
        switch (value) {
            case 0:
                return UNUSE;
            case 10:
                return GIVED;
            case 11:
                return RECEIVED;
            case 20:
                return USED;
            case 30:
                return SENT;
            default:
                throw new NotImplementedException();
        }
    }

}
