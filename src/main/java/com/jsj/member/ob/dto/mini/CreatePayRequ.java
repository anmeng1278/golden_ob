package com.jsj.member.ob.dto.mini;

import io.swagger.annotations.ApiModelProperty;

public class CreatePayRequ {

    private String openId;

    private String unionId;


    @ApiModelProperty(value = "订单编号，必填", required = true)
    private int orderId;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }
}
