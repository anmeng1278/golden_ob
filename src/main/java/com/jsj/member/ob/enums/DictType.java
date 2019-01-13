package com.jsj.member.ob.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import com.jsj.member.ob.exception.FatalException;

public enum DictType implements IEnum {


    PRODUCTTYPE(200, "商品分类"),

    AREA(50000, "省市地区"),

    COPYWRITER(900,"文案分类");

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
            case 200:
                return PRODUCTTYPE;
            case 50000:
                return AREA;
            case 900:
                return COPYWRITER;
            default:
                throw new FatalException("未知的枚举值");
        }
    }
}