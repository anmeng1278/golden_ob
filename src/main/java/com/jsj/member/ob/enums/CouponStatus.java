package com.jsj.member.ob.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import com.jsj.member.ob.exception.FatalException;

public enum CouponStatus implements IEnum {

    UNUSE(0, "未使用"),

    USED(10, "已使用"),

    EXPIRED(60, "已过期");

    private Integer value;

    private String message;

    CouponStatus(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public Integer getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static CouponStatus valueOf(int value) {
        switch (value) {
            case 0:
                return UNUSE;
            case 10:
                return USED;
            case 60:
                return EXPIRED;
            default:
                throw new FatalException("未知的枚举值");
        }
    }
}