package com.jsj.member.ob.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import com.jsj.member.ob.exception.FatalException;

public enum CouponUseRange implements IEnum {

    UNSET(0, "未设置"),

    ALL(1, "全部商品"),

    SPECIAL(2, "指定商品");

    private Integer value;

    private String message;

    CouponUseRange(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public Integer getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static CouponUseRange valueOf(int value) {
        switch (value) {
            case 0:
                return UNSET;
            case 1:
                return ALL;
            case 2:
                return SPECIAL;
            default:
                throw new FatalException("未知的枚举值");
        }
    }
}