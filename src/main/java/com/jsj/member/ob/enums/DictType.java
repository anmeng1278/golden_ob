package com.jsj.member.ob.enums;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public enum DictType {

    PRODUCTPERPROTY(100, "商品属性"),

    PRODUCTTYPE(200, "商品分类");

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
            default:
                throw new NotImplementedException();
        }
    }
}