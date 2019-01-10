package com.jsj.member.ob.service;

import com.baomidou.mybatisplus.service.IService;
import com.jsj.member.ob.dto.api.order.UserOrderDto;
import com.jsj.member.ob.entity.Order;

import java.util.List;

/**
 *   @description : Order 服务接口
 *   ---------------------------------
 * 	 @author cc
 *   @since 2018-12-21
 */
public interface OrderService extends IService<Order> {

    /**
     * 获得用户所有订单
     * @param openId
     * @return
     */
    List<UserOrderDto> getAllOrders(String openId);

    /**
     * 获得用户未支付订单
     * @param openId
     * @return
     */
    List<UserOrderDto> getUnPayOrders(String openId);
}
