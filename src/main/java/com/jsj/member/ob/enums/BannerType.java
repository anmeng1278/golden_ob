package com.jsj.member.ob.enums;

import com.jsj.member.ob.exception.FatalException;

public enum  BannerType {

    COVER(1, "首页轮播"),

    STOCK(2, "库存轮播");

    private Integer value;

    private String message;

    BannerType(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public Integer getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static BannerType valueOf(int value) {
        switch (value) {
            case 1:
                return COVER;
            case 2:
                return STOCK;
            default:
                throw new FatalException("未知的枚举值");
        }
    }
}
