package com.jsj.member.ob.logic;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jsj.member.ob.entity.Cart;
import com.jsj.member.ob.entity.CartProduct;
import com.jsj.member.ob.service.CartProductService;
import com.jsj.member.ob.service.CartService;
import com.jsj.member.ob.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;

/**
 * 购物车逻辑处理类
 *
 * 1.添加、修改购物车
 * 2.清空购物车
 * 3.获取购物车
 */

@Component
public class CartLogic {


    public static CartLogic cartLogic;

    // 初始化的时候，将本类中的sysConfigManager赋值给静态的本类变量
    @PostConstruct
    public void init() {
        cartLogic = this;
        cartLogic.cartService = this.cartService;
        cartLogic.cartProductService = this.cartProductService;
    }

    @Autowired
    CartService cartService;


    @Autowired
    CartProductService cartProductService;


    public static void Add(){

        Cart cart = new Cart();

        cart.setCreateTime(DateUtils.getCurrentUnixTime());
        cart.setUpdateTime(DateUtils.getCurrentUnixTime());
        cart.setOpenId("123");

        cartLogic.cartService.insert(cart);

        System.out.println(cart.getCartId());

    }

    public static void Get(){

        Integer cartId = 10;

        EntityWrapper<CartProduct> wrapper = new EntityWrapper<>();
        wrapper.where("cart_id={0}", cartId);

        List<CartProduct> cartProducts = cartLogic.cartProductService.selectList(wrapper);
        System.out.println(JSON.toJSONString(cartProducts));

        HashMap<String, Object> kv = new HashMap<>();
        kv.put("cart_id", 10);

        List<CartProduct> cartProducts1 = cartLogic.cartProductService.selectByMap(kv);
        System.out.println(JSON.toJSONString(cartProducts1));

    }

}
