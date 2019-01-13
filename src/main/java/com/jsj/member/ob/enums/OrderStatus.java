package com.jsj.member.ob.enums;

import com.baomidou.mybatisplus.enums.IEnum;
import com.jsj.member.ob.exception.FatalException;

public enum OrderStatus implements IEnum {

    //订单状态，0：待支付 10：支付成功 20：支付失败 60：取消订单

    UNPAY(0, "待支付"),

    PAYSUCCESS(10, "支付成功"),

    PAYFAIL(20, "支付失败"),

    CANCEL(60, "订单已取消");

    private Integer value;

    private String message;

    private String format;

    OrderStatus(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    public Integer getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public String getFormat() {
        String format = "%s";
        switch (this) {
            case UNPAY:
                format = "<span class=\"layui-badge layui-bg-cyan\">%s</span>";
                break;
            case PAYSUCCESS:
                format = "<span class=\"layui-badge layui-bg-green\">%s</span>";
                break;
            case PAYFAIL:
                format = "<span class=\"layui-badge layui-bg-orange\">%s</span>";
                break;
            case CANCEL:
                format = "<span class=\"layui-badge\">%s</span>";
                break;
        }

        this.format = String.format(format, this.message);
        return this.format;
    }

    public static OrderStatus valueOf(int value) {
        switch (value) {
            case 0:
                return UNPAY;
            case 10:
                return PAYSUCCESS;
            case 20:
                return PAYFAIL;
            case 60:
                return CANCEL;
            default:
                throw new FatalException("未知的枚举值");

        }
    }
}