package com.jsj.member.ob.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import com.jsj.member.ob.exception.FatalException;

public enum SmsLevel implements IEnum {

    IMPORTANCE(1, "重要"),

    ORDINARY(2, "一般"),

    MARKETING(3, "营销");

    private Integer value;
    private String message;

    SmsLevel(Integer value, String message) {
        this.value = value;
        this.message = message;
    }


    @JsonValue
    public Integer getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static SmsLevel valueOf(int value) {

        switch (value) {
            case 1:
                return IMPORTANCE;
            case 2:
                return ORDINARY;
            case 3:
                return MARKETING;
            default:
                throw new FatalException("未知的枚举值");
        }
    }
}
