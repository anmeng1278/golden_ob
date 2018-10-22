package com.jsj.member.ob.enums;

import com.jsj.member.ob.exception.FatalException;

public enum DictType {

    PRODUCTPERPROTY(100, "商品属性"),

    PRODUCTTYPE(200, "商品分类"),

    AREA(50000, "省市地区");

    private Integer value;
    private String message;

    DictType(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public Integer getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static DictType valueOf(int value) {
        switch (value) {
            case 100:
                return PRODUCTPERPROTY;
            case 200:
                return PRODUCTTYPE;
            case 50000:
                return AREA;
            default:
                throw new FatalException("未知的枚举值");
        }
    }
}