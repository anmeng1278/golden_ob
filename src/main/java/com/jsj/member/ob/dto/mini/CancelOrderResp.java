package com.jsj.member.ob.dto.mini;

import io.swagger.annotations.ApiModelProperty;

public class CancelOrderResp {

    @ApiModelProperty(value = "订单编号，必填", required = true)
    private int orderId;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
