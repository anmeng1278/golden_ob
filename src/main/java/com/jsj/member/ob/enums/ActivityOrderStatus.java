package com.jsj.member.ob.enums;


import com.baomidou.mybatisplus.enums.IEnum;
import com.jsj.member.ob.exception.FatalException;

public enum ActivityOrderStatus  implements IEnum {

    //组团状态 0 组团中  10 组团成功  20 组团失败   60 已取消

    GROUPONING(0, "组团中"),

    SUCCESS(10, "组团成功"),

    FAIL(20, "组团失败"),

    ORDERSUCCESS(30, "订单成功"),

    CANCEL(60, "已取消");

    private Integer value;

    private String message;

    ActivityOrderStatus(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public Integer getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public static ActivityOrderStatus valueOf(int value) {
        switch (value) {
            case 0:
                return GROUPONING;
            case 10:
                return SUCCESS;
            case 20:
                return FAIL;
            case 30:
                return ORDERSUCCESS;
            case 60:
                return CANCEL;
            default:
                throw new FatalException("未知的枚举值");
        }
    }
}