package com.jsj.member.ob.enums;

import com.jsj.member.ob.exception.FatalException;

public enum TemplateType {

    SERVICE(0, "客服消息"),

    PAYSUCCESSED(1, "支付成功"),

    ENTITYUSESUCCESSED(2, "实物使用成功"),

    OPENCARDCONFIRM(3, "开卡确认中"),

    QRCODEUSESUCCESSED(4,"活动码使用成功");

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
            case 2:
                return ENTITYUSESUCCESSED;
            case 3:
                return OPENCARDCONFIRM;
            case 4:
                return QRCODEUSESUCCESSED;
            default:
                throw new FatalException("未知的枚举值");
        }
    }
}