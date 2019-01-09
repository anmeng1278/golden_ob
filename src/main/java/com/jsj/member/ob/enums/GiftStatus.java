package com.jsj.member.ob.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import com.jsj.member.ob.exception.FatalException;

public enum GiftStatus implements IEnum {

    UNSHARE(0, "未分享"),

    SHARED(10, "已分享"),

    DRAWING(30, "领取中"),

    BROUGHTOUT(40, "已领完"),

    CANCEL(60, "取消分享");

    private Integer value;
    private String message;

    GiftStatus(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public Integer getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static GiftStatus valueOf(int value) {

        switch (value) {
            case 0:
                return UNSHARE;
            case 10:
                return SHARED;
            case 30:
                return DRAWING;
            case 40:
                return BROUGHTOUT;
            case 60:
                return CANCEL;
            default:
                throw new FatalException("未知的枚举值");
        }
    }
}
