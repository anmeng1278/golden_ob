package com.jsj.member.ob.service;

import com.baomidou.mybatisplus.service.IService;
import com.jsj.member.ob.dto.api.order.UserOrderDto;
import com.jsj.member.ob.entity.Order;
import com.jsj.member.ob.enums.OrderFlag;

import java.util.List;

/**
 * @author cc
 * @description : Order 服务接口
 * ---------------------------------
 * @since 2018-12-21
 */
public interface OrderService extends IService<Order> {

    /**
     * 获得用户所有订单
     *
     * @param openId
     * @return
     */
    List<UserOrderDto> getOrders(String openId, OrderFlag orderFlag);

}
