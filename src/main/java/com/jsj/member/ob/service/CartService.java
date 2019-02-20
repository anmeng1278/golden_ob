package com.jsj.member.ob.service;

import com.baomidou.mybatisplus.service.IService;
import com.jsj.member.ob.entity.Cart;
import com.jsj.member.ob.entity.CartProduct;

import java.util.List;

/**
 * @author cc
 * @description : Cart 服务接口
 * ---------------------------------
 * @since 2019-02-11
 */
public interface CartService extends IService<Cart> {

    /**
     * 获取购物车商品
     *
     * @param unionId
     * @return
     */
    List<CartProduct> getCartProducts(String unionId);
}
