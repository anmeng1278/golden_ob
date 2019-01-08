package com.jsj.member.ob.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.jsj.member.ob.exception.FatalException;

public enum SecKillStatus {

    UNINIT(0, "商品信息暂未初始化"),

    REPEAT(1, "不能重复购买"),

    SOLDOUT(2, "商品已售罄"),

    SUCCESS(3, "秒杀成功"),

    REPEATREQUEST(4, "重复请求"),

    UNBEGIN(5, "未开始"),

    SYSTEMERROR(99, "系统异常");

    private Integer value;
    private String message;

    SecKillStatus(Integer value, String message) {
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

    public static SecKillStatus valueOf(int value) {

        switch (value) {
            case 0:
                return UNINIT;
            case 1:
                return REPEAT;
            case 2:
                return SOLDOUT;
            case 3:
                return SUCCESS;
            case 4:
                return REPEATREQUEST;
            case 5:
                return UNBEGIN;
            case 99:
                return SYSTEMERROR;
            default:
                throw new FatalException("未知的枚举值");
        }
    }
}
