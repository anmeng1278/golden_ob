package com.jsj.member.ob.enums;

import com.jsj.member.ob.exception.FatalException;

public enum DeliveryStatus {

    //0未发货 10已发货 20已签收',

    UNDELIVERY(0, "未发货"),

    DELIVERED(10, "已发货"),

    SIGNED(20, "已签收");

    //活动码  未获取=0 已获取=10 已使用=20
    //卡      未开卡 0 已开卡 10

    private Integer value;
    private String message;

    DeliveryStatus(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public Integer getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static DeliveryStatus valueOf(int value) {
        switch (value) {
            case 0:
                return UNDELIVERY;
            case 10:
                return DELIVERED;
            case 20:
                return SIGNED;
            default:
                throw new FatalException("未知的枚举值");
        }
    }
}
