package com.jsj.member.ob.logic;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jsj.member.ob.entity.Cart;
import com.jsj.member.ob.entity.CartProduct;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.service.CartProductService;
import com.jsj.member.ob.service.CartService;
import com.jsj.member.ob.service.ProductService;
import com.jsj.member.ob.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车逻辑处理类
 * <p>
 * 1.添加、修改购物车
 * 2.清空购物车
 * 3.获取购物车
 */

@Component
public class CartLogic extends BaseLogic {

    public static CartLogic cartLogic;

    // 初始化的时候，将本类中的sysConfigManager赋值给静态的本类变量
    @PostConstruct
    public void init() {
        cartLogic = this;

    }

    @Autowired
    CartService cartService;


    @Autowired
    CartProductService cartProductService;

    @Autowired
    ProductService productService;


    /**
     * 清空购物车
     *
     * @param openId
     */
    public static void DeleteCart(String openId) {
        if (StringUtils.isBlank(openId)) {
            throw new TipException("参数不合法,用户openId为空");
        }
        //获取用户的购物车
        Cart cart = CartLogic.GetCart(openId);

        if (cart != null) {
            //修改购物车deleteTime，清空购物车
            cart.setDeleteTime(DateUtils.getCurrentUnixTime());
            cartLogic.cartService.updateById(cart);
        }
    }


    /**
     * 删除购物车中的某件商品
     *
     * @param cartProductId
     */
    public static void DeleteCartProduct(int cartProductId) {
        if (cartProductId <= 0) {
            throw new TipException("参数不合法，cartProductId为空");
        }
        cartLogic.cartProductService.deleteById(cartProductId);
    }


    /**
     * 给用户创建购物车
     *
     * @param openId
     */
    public static Cart CreateCart(String openId) {
        if (StringUtils.isBlank(openId)) {
            throw new TipException("参数不合法，用户openId为空");
        }

        Cart cart = new Cart();
        cart.setOpenId(openId);
        cart.setCreateTime(DateUtils.getCurrentUnixTime());
        cart.setUpdateTime(DateUtils.getCurrentUnixTime());
        cartLogic.cartService.insert(cart);

        return cart;
    }

    /**
     * 添加或修改购物车
     *
     * @param openId
     * @param productId
     * @param number
     * @param method    添加方式 add为累加  update为更新
     * @return
     */
    public static void AddUpdateCartProduct(String openId, int productId, int productSpecId, int number, String method) {

        if (StringUtils.isBlank(openId)) {
            throw new TipException("参数不合法，用户openId为空");
        }
        if (productId <= 0) {
            throw new TipException("参数不合法，商品ID不能为空");
        }
        if (productSpecId <= 0) {
            throw new TipException("参数不合法，商品规格不能为空");
        }
        if (number <= 0) {
            number = 0;
        }

        EntityWrapper<Cart> cartWrapper = new EntityWrapper<>();
        cartWrapper.where("open_id={0} and delete_time is null", openId);

        //判断当前用户是否有购物车，没有则创建购物车
        Cart cart = cartLogic.cartService.selectOne(cartWrapper);
        if (cart == null) {
            cart = CartLogic.CreateCart(openId);
        }

        //查询购物车中的商品信息
        EntityWrapper<CartProduct> queryWrapper = new EntityWrapper<CartProduct>();
        queryWrapper.where("cart_id={0} and product_id={1} and product_spec_id={2}",
                cart.getCartId(),
                productId,
                productSpecId
        );

        //商品数为0时，删除购物车中商品
        if (number == 0) {
            cartLogic.cartProductService.delete(queryWrapper);
        } else {
            CartProduct cartProduct = cartLogic.cartProductService.selectOne(queryWrapper);
            //如果购物车中没有此商品，添加此商品
            if (cartProduct == null) {

                cartProduct = new CartProduct();
                cartProduct.setCartId(cart.getCartId());
                cartProduct.setProductId(productId);
                cartProduct.setProductSpecId(productSpecId);
                cartProduct.setNumber(number);
                cartProduct.setCreateTime(DateUtils.getCurrentUnixTime());
                cartProduct.setUpdateTime(DateUtils.getCurrentUnixTime());

                cartLogic.cartProductService.insert(cartProduct);
            } else {   //含有此商品，修改商品数量
                if (method == "add") {
                    cartProduct.setNumber(cartProduct.getNumber() + number);
                } else {
                    cartProduct.setNumber(number);
                }
                cartProduct.setUpdateTime(DateUtils.getCurrentUnixTime());
                cartLogic.cartProductService.updateById(cartProduct);

            }
        }

    }

    /**
     * 获取用户的购物车
     *
     * @param
     */
    public static Cart GetCart(String openId) {
        if (StringUtils.isBlank(openId)) {
            throw new TipException("参数不合法，用户openId为空");
        }
        EntityWrapper<Cart> wrapper = new EntityWrapper<>();
        wrapper.where("open_id={0} and delete_time is null", openId);
        Cart cart = cartLogic.cartService.selectOne(wrapper);
        return cart;
    }


    /**
     * 获取用户购物车中商品列表
     *
     * @param openId
     * @return
     */
    public static List<CartProduct> GetCartProducts(String openId) {

        if (StringUtils.isBlank(openId)) {
            throw new TipException("参数不合法，用户openId为空");
        }

        List<CartProduct> cartProducts = new ArrayList<>();

        Cart cart = CartLogic.GetCart(openId);
        if (cart == null) {
            return cartProducts;
        }

        EntityWrapper<CartProduct> wrapper = new EntityWrapper<>();
        wrapper.where("cart_id={0} and delete_time is null", cart.getCartId());

        //获取购物车中商品
        cartProducts = cartLogic.cartProductService.selectList(wrapper);

        return cartProducts;
    }

    /**
     * 获得用户购物车中商品数量
     *
     * @param openId
     * @return
     */
    public static int GetCartProductCount(String openId) {
        int amount = 0;
        List<CartProduct> cartProducts = CartLogic.GetCartProducts(openId);
        for (CartProduct cartProduct : cartProducts) {
            amount += cartProduct.getNumber();
        }
        return amount;
    }

}
