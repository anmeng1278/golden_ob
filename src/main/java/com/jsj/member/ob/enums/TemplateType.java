package com.jsj.member.ob.enums;

import com.jsj.member.ob.exception.FatalException;

public enum TemplateType {

    SERVICE(0, "客服消息"),

    PAYSUCCESSED(1, "支付成功");


    private Integer value;

    private String message;

    TemplateType(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public Integer getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static TemplateType valueOf(int value) {
        switch (value) {
            case 0:
                return SERVICE;
            case 1:
                return PAYSUCCESSED;
            default:
                throw new FatalException("未知的枚举值");
        }
    }
}