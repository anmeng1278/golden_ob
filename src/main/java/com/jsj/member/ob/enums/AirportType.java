package com.jsj.member.ob.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import com.jsj.member.ob.exception.FatalException;

public enum AirportType implements IEnum {

    AIRPORT(1, "机场"),

    TRAIN(2, "高铁");

    private Integer value;

    private String message;

    AirportType(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public Integer getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static AirportType valueOf(int value) {
        switch (value) {
            case 1:
                return AIRPORT;
            case 2:
                return TRAIN;
            default:
                throw new FatalException("未知的枚举值");
        }
    }
}