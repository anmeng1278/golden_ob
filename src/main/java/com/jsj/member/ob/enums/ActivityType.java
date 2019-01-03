package com.jsj.member.ob.enums;

import com.jsj.member.ob.exception.FatalException;

public enum ActivityType {

    NORMAL(0, "普通"),

    GROUPON(10, "团购"),

    SECKILL(20, "秒杀"),

    COMBINATION(30, "组合"),

    RIGHTSEXCHANGE(40,"权益兑换");


    private Integer value;

    private String message;

    ActivityType(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public Integer getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static ActivityType valueOf(int value) {
        switch (value) {
            case 0:
                return NORMAL;
            case 10:
                return GROUPON;
            case 20:
                return SECKILL;
            case 30:
                return COMBINATION;
            case 40:
                return RIGHTSEXCHANGE;
            default:
                throw new FatalException("未知的枚举值");
        }
    }
}