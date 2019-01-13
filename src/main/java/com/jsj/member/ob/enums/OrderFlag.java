package com.jsj.member.ob.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import com.jsj.member.ob.exception.FatalException;

public enum OrderFlag implements IEnum {

    //订单表示，0：所有订单 1：待支付和支付失败的订单

    ALLORDERS(0, "所有订单"),

    UNPAIDORDERS(1, "待支付订单");

    private Integer value;

    private String message;


    OrderFlag(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public Integer getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static OrderFlag valueOf(int value) {
        switch (value) {
            case 0:
                return ALLORDERS;
            case 1:
                return UNPAIDORDERS;
            default:
                throw new FatalException("未知的枚举值");

        }
    }
}