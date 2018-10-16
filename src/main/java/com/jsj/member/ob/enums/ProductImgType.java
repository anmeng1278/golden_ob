package com.jsj.member.ob.enums;

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
            case 1:
                return PRODUCT;
            default:
                return COVER;
        }
    }
}