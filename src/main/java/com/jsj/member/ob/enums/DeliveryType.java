package com.jsj.member.ob.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import com.jsj.member.ob.exception.FatalException;

public enum DeliveryType implements IEnum {

    //1：自提，2：配送'

    PICKUP(1, "自提"),

    DISTRIBUTE(2, "配送");


    private Integer value;
    private String message;

    DeliveryType(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public Integer getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static DeliveryType valueOf(int value) {
        switch (value) {
            case 1:
                return PICKUP;
            case 2:
                return DISTRIBUTE;
            default:
                throw new FatalException("未知的枚举值");
        }
    }

}
