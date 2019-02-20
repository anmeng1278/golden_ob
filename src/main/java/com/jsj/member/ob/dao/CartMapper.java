package com.jsj.member.ob.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jsj.member.ob.entity.Cart;
import com.jsj.member.ob.entity.CartProduct;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *   @description : Cart Mapper 接口
 *   ---------------------------------
 * 	 @author cc
 *   @since 2019-02-11
 */
@Repository
public interface CartMapper extends BaseMapper<Cart> {


    /**
     * 获取购物车商品
     * @param unionId
     * @return
     */
    List<CartProduct> getCartProducts(@Param("unionId")String unionId);

}