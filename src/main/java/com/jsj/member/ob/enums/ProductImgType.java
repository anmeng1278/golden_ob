package com.jsj.member.ob.enums;

import com.jsj.member.ob.exception.FatalException;

public enum ProductImgType {

    //0 封面  1 商品图片
    COVER(0, "封面"),

    PRODUCT(1, "商品图片");

    private Integer value;

    private String message;

    ProductImgType(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public Integer getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static ProductImgType valueOf(int value) {
        switch (value) {
            case 0:
                return COVER;
            case 1:
                return PRODUCT;
            default:
                throw new FatalException("未知的枚举值");
        }
    }
}