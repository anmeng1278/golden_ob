package com.jsj.member.ob.enums;

import com.jsj.member.ob.exception.FatalException;

public enum CopywriterType {

    GIVINGCOPY(1, "赠送文案");

    private Integer value;

    private String message;

    CopywriterType(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public Integer getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static CopywriterType valueOf(int value) {
        switch (value) {
            case 1:
                return GIVINGCOPY;
            default:
                throw new FatalException("未知的枚举值");
        }
    }
}