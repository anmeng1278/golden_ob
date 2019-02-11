package com.jsj.member.ob.dto.mini;

import com.jsj.member.ob.dto.api.order.OrderDto;
import com.jsj.member.ob.dto.thirdParty.GetPayTradeResp;

public class CreatePayResp {

    private GetPayTradeResp pay;


    private OrderDto orderDto;

    public GetPayTradeResp getPay() {
        return pay;
    }

    public void setPay(GetPayTradeResp pay) {
        this.pay = pay;
    }


    public OrderDto getOrderDto() {
        return orderDto;
    }

    public void setOrderDto(OrderDto orderDto) {
        this.orderDto = orderDto;
    }
}
