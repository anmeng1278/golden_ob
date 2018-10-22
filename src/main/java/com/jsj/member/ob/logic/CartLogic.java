package com.jsj.member.ob.logic;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jsj.member.ob.dto.api.cart.CartProductDto;
import com.jsj.member.ob.dto.api.cart.GetCartProductsRequ;
import com.jsj.member.ob.dto.api.cart.GetCartProductsResp;
import com.jsj.member.ob.dto.api.product.ProductDto;
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
public class CartLogic {

    public static CartLogic cartLogic;

    // 初始化的时候，将本类中的sysConfigManager赋值给静态的本类变量
    @PostConstruct
    public void init() {
        cartLogic = this;
        cartLogic.cartService = this.cartService;
        cartLogic.cartProductService = this.cartProductService;
        cartLogic.productService = this.productService;

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
        if (cartProductId == 0) {
            throw new TipException("参数不合法，cartProductId为空");
        }
        cartLogic.cartProductService.deleteById(cartProductId);
    }


    /**
     * 添加或修改购物车
     *
     * @param openId
     * @param productId
     * @param number
     * @return
     */
    public static void AddUpdateCartProduct(String openId, int productId, int productSpecId, int number) {
        if (StringUtils.isBlank(openId)) {
            throw new TipException("参数不合法，用户openId为空");
        }
        if (productId == 0) {
            throw new TipException("参数不合法，商品ID不能为空");
        }
        if (productSpecId == 0) {
            throw new TipException("参数不合法，商品规格不能为空");
        }
        if (number < 0) {
            number = 0;
        }

        EntityWrapper<Cart> cartWrapper = new EntityWrapper<>();
        cartWrapper.where("open_id={0} and delete_time is null", openId);

        //判断当前用户是否有购物车，没有则创建购物车
        Cart cart = cartLogic.cartService.selectOne(cartWrapper);
        if (cart == null) {
            cart = new Cart();
            cart.setOpenId(openId);
            cart.setCreateTime(DateUtils.getCurrentUnixTime());
            cart.setUpdateTime(DateUtils.getCurrentUnixTime());
            cartLogic.cartService.insert(cart);

        }

        EntityWrapper<CartProduct> cartProductWrapper = new EntityWrapper<CartProduct>();
        Wrapper<CartProduct> where = cartProductWrapper.where("cart_id={0} and product_id={1} and product_size_id={2}",
                cart.getCartId(),
                productId,
                productSpecId
        );

        CartProduct cartProduct = cartLogic.cartProductService.selectOne(where);

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
            if (number <= 0) {
                CartLogic.DeleteCartProduct(cartProduct.getCartProductId());
            } else {
                cartProduct.setNumber(number);
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
     * @param requ
     * @return
     */
    public static GetCartProductsResp GetCartProducts(GetCartProductsRequ requ) {

        if (StringUtils.isBlank(requ.getBaseRequ().getOpenId())) {
            throw new TipException("参数不合法，用户openId为空");
        }

        GetCartProductsResp resp = new GetCartProductsResp();

        Cart cart = CartLogic.GetCart(requ.getBaseRequ().getOpenId());
        if (cart == null) {
            return resp;
        }

        EntityWrapper<CartProduct> wrapper = new EntityWrapper<>();
        wrapper.where("cart_id={0} and delete_time is null", cart.getCartId());

        List<CartProductDto> cartProductDtoList = new ArrayList<>();
        //获取购物车中商品
        List<CartProduct> cartProductList = cartLogic.cartProductService.selectList(wrapper);

        CartProductDto cartProductDto = new CartProductDto();
        for (CartProduct cp : cartProductList) {
            //获取商品详情
            ProductDto productDto = ProductLogic.GetProduct(cp.getProductId());
            cartProductDto.setCartProductId(cp.getCartProductId());
            cartProductDto.setProductId(productDto.getProductId());
            cartProductDto.setNumber(cp.getNumber());
            cartProductDto.setProductDto(productDto);
            cartProductDtoList.add(cartProductDto);
            resp.setCartProductDtoList(cartProductDtoList);
        }
        return resp;
    }


}
