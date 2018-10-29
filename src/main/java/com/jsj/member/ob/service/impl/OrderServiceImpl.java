package com.jsj.member.ob.service.impl;

import com.jsj.member.ob.entity.Order;
import com.jsj.member.ob.dao.OrderMapper;
import com.jsj.member.ob.service.OrderService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 *   @description : Order 服务实现类
 *   ---------------------------------
 * 	 @author cc
 *   @since 2018-10-29
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
	
}
