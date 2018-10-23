package com.jsj.member.ob.dao;

import com.jsj.member.ob.entity.Order;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.plugins.Page;

import java.util.List;

/**
 *   @description : Order Mapper 接口
 *   ---------------------------------
 * 	 @author cc
 *   @since 2018-10-23
 */
@Repository
public interface OrderMapper extends BaseMapper<Order> {

}