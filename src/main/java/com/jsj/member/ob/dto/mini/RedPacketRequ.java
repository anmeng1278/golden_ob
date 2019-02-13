package com.jsj.member.ob.dto.mini;

import io.swagger.annotations.ApiModelProperty;

public class RedPacketRequ {

    @ApiModelProperty(value = "订单唯一编码，必填", required = true)
    private String orderUniqueCode;

    @ApiModelProperty(value = "openId，必填", required = true)
    private String openId;

    @ApiModelProperty(value = "unionId，必填", required = true)
    private String unionId;

    public String getOrderUniqueCode() {
        return orderUniqueCode;
    }

    public void setOrderUniqueCode(String orderUniqueCode) {
        this.orderUniqueCode = orderUniqueCode;
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
