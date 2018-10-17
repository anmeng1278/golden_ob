package com.jsj.member.ob.logic;

import com.jsj.member.ob.dto.api.order.CreateOrderRequ;
import com.jsj.member.ob.dto.api.order.CreateOrderResp;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.order.OrderBase;
import com.jsj.member.ob.logic.order.OrderFactory;
import com.jsj.member.ob.service.OrderProductService;
import com.jsj.member.ob.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class OrderLogic {

    public static OrderLogic orderLogic;

    // 初始化的时候，将本类中的sysConfigManager赋值给静态的本类变量
    @PostConstruct
    public void init() {
        orderLogic = this;
        orderLogic.orderService = this.orderService;
    }

    @Autowired
    OrderService orderService;

    @Autowired
    OrderProductService orderProductService;

    /**
     * 创建订单，普通订单、团单、秒杀单、
     *
     * @param requ
     * @return
     */
    public static CreateOrderResp CreateOrder(CreateOrderRequ requ) {

        //参数校验
        if (requ.getOrderType() == null) {
            throw new TipException("订单类型不能为空");
        }

        OrderBase orderBase = OrderFactory.GetInstance(requ.getOrderType());
        return orderBase.CreateOrder(requ);

    }

}
