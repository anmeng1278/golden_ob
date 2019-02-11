package com.jsj.member.ob.dto.mini;

import io.swagger.annotations.ApiModelProperty;

public class OrdersRequ {

    @ApiModelProperty(value = "unionId，必填", required = true)
    private String unionId;

    @ApiModelProperty(value = "订单状态标识，可选")
    private String orderFlag;

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getOrderFlag() {
        return orderFlag;
    }

    public void setOrderFlag(String orderFlag) {
        this.orderFlag = orderFlag;
    }
}
