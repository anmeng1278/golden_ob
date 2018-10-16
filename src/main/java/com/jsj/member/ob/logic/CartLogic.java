package com.jsj.member.ob.logic;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jsj.member.ob.entity.Cart;
import com.jsj.member.ob.entity.CartProduct;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.service.CartProductService;
import com.jsj.member.ob.service.CartService;
import com.jsj.member.ob.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    /**
     * 清空购物车
     */
    public static void DeleteCart(String openId){
        //获取用户的购物车
        Cart cart = CartLogic.GetCart(openId);
        //修改购物车deleteTime，清空购物车
        cart.setDeleteTime(DateUtils.getCurrentUnixTime());
        cartLogic.cartService.updateById(cart);
    }


    /**
     * 删除购物车中的某件商品
     */
    public static void DeleteProduct(String openId){
        Map<String,Object> map = new HashMap<>();
        //获取用户的购物车中的商品详情
        List<CartProduct> cartProductList = CartLogic.GetCartProduct(openId);
        for (CartProduct cartProduct : cartProductList) {
            Integer productId = cartProduct.getProductId();
            map.put("product_id",productId);
            cartLogic.cartProductService.deleteByMap(map);
        }
    }


    /**
     * 添加或修改购物车
     * @param openId
     * @param productId
     * @param number
     * @return
     */
    public static CartProduct SaveAndUpdate(String openId,Integer productId,Integer number){
        CartProduct cartProduct = null;

        EntityWrapper<CartProduct> wrapper = new EntityWrapper<>();
        wrapper.where("product_id={0}", productId);

        //查询用户的购物车
        Cart cart = CartLogic.GetCart(openId);
        //1.1判断该用户是否有购物车没有则创建
        if(cart == null){
            cart.setOpenId(openId);
            cart.setCreateTime(DateUtils.getCurrentUnixTime());
            cart.setUpdateTime(DateUtils.getCurrentUnixTime());
            cartLogic.cartService.insert(cart);
        }else {
            //1.2判断该用户购物车中是否含有此商品没有则添加
            List<CartProduct> cartProductList = CartLogic.GetCartProduct(openId);
            //购物车中不存在此商品
           if(CollectionUtils.isEmpty(cartProductList)){
               cartProduct = new CartProduct();
               cartProduct.setProductId(productId);
               cartProduct.setNumber(number);
               cartProduct.setCartId(cart.getCartId());
               cartProduct.setProductSizeId(1);
               cartProduct.setCreateTime(DateUtils.getCurrentUnixTime());
               cartProduct.setUpdateTime(DateUtils.getCurrentUnixTime());
               cartLogic.cartProductService.insert(cartProduct);
           }else {
               //购物车商品中存在此商品，修改数量
               cartProduct = cartLogic.cartProductService.selectOne(wrapper);
               cartProduct.setNumber(cartProduct.getNumber()+number);
               //当商品数量小于0，移除购物车中此商品
               if (cartProduct.getNumber()<=0){
                   cartLogic.cartProductService.deleteById(cartProduct.getCartProductId());
               }
           }

        }
            return cartProduct;

    }

    /**
     * 获得用户的购物车
     * @param
     */
    public static Cart GetCart(String openId){
        EntityWrapper<Cart> wrapper = new EntityWrapper<>();
        wrapper.where("open_id={0}", openId);
        Cart cart = cartLogic.cartService.selectOne(wrapper);
        if(cart == null){
            throw new TipException("购物车为空");
        }
        return cart;
    }


    /**
     * 获得用户购物车中商品列表
     * @param openId
     * @return
     */
    public static List<CartProduct> GetCartProduct(String openId){
        Cart cart = CartLogic.GetCart(openId);
        EntityWrapper<CartProduct> wrapper = new EntityWrapper<>();
        wrapper.where("cart_id={0}", cart.getCartId());
        //获取购物车中商品商品
        List<CartProduct> cartProductList = cartLogic.cartProductService.selectList(wrapper);
        /*if(cartProductList == null || cartProductList.size() == 0){
            throw new TipException("您的购物车中空空如也，快加购商品吧！");
        }*/
        return cartProductList;
    }


}
