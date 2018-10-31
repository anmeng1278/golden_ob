package com.jsj.member.ob.enums;

import com.jsj.member.ob.exception.FatalException;

public enum StockStatus {

    //0 未使用  10 赠送中(未领取)  11 赠送成功  20 已使用(提货)  30 已发货',

    UNUSE(0, "未使用"),

    GIVING(10,"赠送中"),

    RECEIVED(11, "赠送成功"),

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
                return GIVING;
            case 11:
                return RECEIVED;
            case 20:
                return USED;
            case 30:
                return SENT;
            default:
                throw new FatalException("未知的枚举值");
        }
    }

}
