package com.jsj.member.ob.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jsj.member.ob.dto.api.order.UserOrderDto;
import com.jsj.member.ob.entity.Order;
import com.jsj.member.ob.enums.OrderFlag;
import org.apache.ibatis.annotations.Param;
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
     * @param unionId
     * @return
     */
    List<UserOrderDto> getOrders(@Param("unionId")String unionId, @Param("orderFlag")OrderFlag orderFlag);


}