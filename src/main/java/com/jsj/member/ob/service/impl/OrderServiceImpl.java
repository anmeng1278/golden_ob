package com.jsj.member.ob.service.impl;

import com.jsj.member.ob.dto.api.order.UserOrderDto;
import com.jsj.member.ob.entity.Order;
import com.jsj.member.ob.dao.OrderMapper;
import com.jsj.member.ob.enums.OrderFlag;
import com.jsj.member.ob.service.OrderService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cc
 * @description : Order 服务实现类
 * ---------------------------------
 * @since 2018-12-21
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Override
    public List<UserOrderDto> getOrders(String openId, OrderFlag orderFlag) {

        return baseMapper.getOrders(openId, orderFlag);
    }

}
