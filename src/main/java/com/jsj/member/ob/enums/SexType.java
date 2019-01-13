package com.jsj.member.ob.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import com.jsj.member.ob.exception.FatalException;

public enum SexType implements IEnum {
    UNKNOWED(0, "未知"),

    MAN(1, "男"),

    WOMAN(2, "女");

    private Integer value;
    private String message;

    SexType(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public Integer getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static SexType valueOf(int value) {

        switch (value) {
            case 0:
                return UNKNOWED;
            case 1:
                return MAN;
            case 2:
                return WOMAN;
            default:
                throw new FatalException("未知的枚举值");
        }
    }
}
