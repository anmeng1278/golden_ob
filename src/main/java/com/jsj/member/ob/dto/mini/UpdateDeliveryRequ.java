package com.jsj.member.ob.dto.mini;

import io.swagger.annotations.ApiModelProperty;

public class UpdateDeliveryRequ {

    @ApiModelProperty(value = "配送编号", required = true)
    private int deliveryId;

    @ApiModelProperty(value = "操作方法 confirm确认收货 delete删除", required = true)
    private String method;

    public int getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(int deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
