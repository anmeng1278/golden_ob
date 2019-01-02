package com.jsj.member.ob.enums;

import com.jsj.member.ob.exception.FatalException;

public enum DeliveryStatus {

    //0未发货 10已发货 20已签收',

    UNDELIVERY(0, "未发货"),

    DELIVERED(10, "已发货"),

    SIGNED(20, "已签收");

    //活动码  未使用=0 已获取=10 已核销=20
    //卡      未开卡 0 未开卡 10 已开卡 20

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

    public String getMessage(PropertyType propertyType) {

        switch (propertyType) {
            case ENTITY:
                return this.getMessage();
            case ACTIVITYCODE: {
                switch (this) {
                    case UNDELIVERY:
                        return "未使用";
                    case DELIVERED:
                        return "已获取";
                    case SIGNED:
                        return "已核销";
                    default:
                        throw new FatalException("未知的枚举值");
                }
            }
            case MONTH:
            case GOLDEN:
            case NATION: {
                switch (this) {
                    case UNDELIVERY:
                        return "未开卡";
                    case DELIVERED:
                        return "已开卡";
                    case SIGNED:
                        return "已开卡";
                    default:
                        throw new FatalException("未知的枚举值");
                }
            }
            default:
                throw new FatalException("未知的枚举值");
        }
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
