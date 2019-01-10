package com.jsj.member.ob.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jsj.member.ob.dto.api.order.UserOrderDto;
import com.jsj.member.ob.entity.Order;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *   @description : Order Mapper 接口
 *   ---------------------------------
 * 	 @author cc
 *   @since 2018-12-21
 */
@Repository
public interface OrderMapper extends BaseMapper<Order> {

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