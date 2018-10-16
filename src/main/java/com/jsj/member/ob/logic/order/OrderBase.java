package com.jsj.member.ob.logic.order;

import com.jsj.member.ob.dto.api.order.CreateOrderRequ;
import com.jsj.member.ob.dto.api.order.CreateOrderResp;
import com.jsj.member.ob.enums.OrderType;
import com.jsj.member.ob.service.OrderProductService;
import com.jsj.member.ob.service.OrderService;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public abstract class OrderBase {

    OrderService orderService;
    OrderProductService orderProductService;

    public OrderBase(OrderType ot) {
        this.orderType = ot;
    }

    private OrderType orderType;

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    /**
     * 创建订单
     *
     * @param requ
     * @return
     */
    public abstract CreateOrderResp CreateOrder(CreateOrderRequ requ);

    /**
     * 订单支付成功
     */
    public void OrderPaySuccess(String orderNumber) {
        throw new NotImplementedException();
    }

}
