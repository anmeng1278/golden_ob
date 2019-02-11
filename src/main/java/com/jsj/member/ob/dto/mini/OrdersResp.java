package com.jsj.member.ob.dto.mini;

import com.jsj.member.ob.dto.api.order.UserOrderDto;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class OrdersResp {

    @ApiModelProperty(value = "订单列表")
    private List<UserOrderDto> orderDtos;

    @ApiModelProperty(value = "订单状态标识，可选")
    private String orderFlag;

    public List<UserOrderDto> getOrderDtos() {
        return orderDtos;
    }

    public void setOrderDtos(List<UserOrderDto> orderDtos) {
        this.orderDtos = orderDtos;
    }

    public String getOrderFlag() {
        return orderFlag;
    }

    public void setOrderFlag(String orderFlag) {
        this.orderFlag = orderFlag;
    }
}
