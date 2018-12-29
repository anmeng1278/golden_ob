package com.jsj.member.ob.enums;

import com.jsj.member.ob.exception.FatalException;
import com.jsj.member.ob.utils.SpringContextUtils;

public enum GoldenCardType {

    //线下：月体验30 金卡47 全国通48  ；
    //线上：月体验 34 金卡46 全国通47

    MONTH(34, "超级空客月体验卡"),

    GOLDEN(46, "商旅管家逸站通卡"),

    NATION(47, "商旅管家全国通卡");

    private Integer value;

    private String message;

    GoldenCardType(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public Integer getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static GoldenCardType valueOf(int value) {
        switch (value) {
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
     * @param goldenCardType
     * @return
     */
    public static Integer getApiCode(GoldenCardType goldenCardType) {
        if (SpringContextUtils.getActiveProfile().equals("dev")) {
            switch (goldenCardType) {
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
        return goldenCardType.getValue();
    }
}