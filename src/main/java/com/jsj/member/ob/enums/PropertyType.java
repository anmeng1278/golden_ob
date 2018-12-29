package com.jsj.member.ob.enums;

import com.jsj.member.ob.exception.FatalException;
import com.jsj.member.ob.utils.SpringContextUtils;

public enum PropertyType {

    ENTITY(1, "实物"),

    ACTIVITYCODE(2, "活动码"),

    MONTH(34, "超级空客月体验卡"),

    GOLDEN(46, "商旅管家逸站通卡"),

    NATION(47, "商旅管家全国通卡");

    private Integer value;

    private String message;

    PropertyType(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public Integer getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static PropertyType valueOf(int value) {
        switch (value) {
            case 1:
                return ENTITY;
            case 2:
                return ACTIVITYCODE;
            case 34:
                return MONTH;
            case 46:
                return GOLDEN;
            case 47:
                return NATION;

            default:
                throw new FatalException("未知的枚举值");
        }
    }

    /**
     * 获取开卡API编码
     *
     * @return
     */
    public Integer getApiCode() {
        if (SpringContextUtils.getActiveProfile().equals("dev")) {
            switch (this) {
                case MONTH:
                    return 30;
                case GOLDEN:
                    return 47;
                case NATION:
                    return 48;
                default:
                    throw new FatalException("未知的枚举值");
            }
        }
        return this.getValue();
    }

    /**
     * 判断枚举是否是会员卡
     *
     * @return
     */
    public boolean isMmeberCard() {

        switch (this) {
            case MONTH:
            case GOLDEN:
            case NATION:
                return true;
        }

        return false;
    }
}