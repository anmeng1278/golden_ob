package com.jsj.member.ob.logic;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jsj.member.ob.dto.api.cart.CartProductDto;
import com.jsj.member.ob.dto.api.product.ProductDto;
import com.jsj.member.ob.entity.Cart;
import com.jsj.member.ob.entity.CartProduct;
import com.jsj.member.ob.enums.ActivityType;
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


    //region (public) 清空购物车 DeleteCart

    /**
     * 清空购物车
     *
     * @param unionId
     */
    public static void DeleteCart(String unionId) {
        if (StringUtils.isBlank(unionId)) {
            throw new TipException("参数不合法,用户unionId为空");
        }
        //获取用户的购物车
        Cart cart = CartLogic.GetCart(unionId);

        if (cart != null) {
            //修改购物车deleteTime，清空购物车
            cart.setDeleteTime(DateUtils.getCurrentUnixTime());
            cartLogic.cartService.updateById(cart);
        }
    }
    //endregion

    //region (public) 删除购物车中的某件商品 DeleteCartProduct

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
    //endregion

    //region (public) 给用户创建购物车 CreateCart

    /**
     * 给用户创建购物车
     *
     * @param unionId
     * @param openId
     */
    public static Cart CreateCart(String openId, String unionId) {
        if (StringUtils.isBlank(openId)) {
            throw new TipException("参数不合法，用户openId为空");
        }
        if (StringUtils.isBlank(unionId)) {
            throw new TipException("参数不合法，用户unionId为空");
        }

        Cart cart = new Cart();
        cart.setUnionId(unionId);
        cart.setOpenId(openId);
        cart.setCreateTime(DateUtils.getCurrentUnixTime());
        cart.setUpdateTime(DateUtils.getCurrentUnixTime());
        cartLogic.cartService.insert(cart);

        return cart;
    }
    //endregion

    //region (public) 添加或修改购物车 AddUpdateCartProduct

    /**
     * 添加或修改购物车
     *
     * @param openId
     * @param unionId
     * @param productId
     * @param productSpecId
     * @param number
     * @param method        添加方式 add为累加  update为更新
     * @param activityId    活动编号
     * @param activityType  活动类型
     * @return
     */
    public static void AddUpdateCartProduct(String openId,
                                            String unionId,
                                            int productId,
                                            int productSpecId,
                                            int number,
                                            String method,
                                            int activityId,
                                            ActivityType activityType
    ) {

        if (StringUtils.isBlank(openId)) {
            throw new TipException("参数不合法，用户openId为空");
        }
        if (StringUtils.isBlank(unionId)) {
            throw new TipException("参数不合法，用户unionId为空");
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
        cartWrapper.where("union_id={0} and delete_time is null", unionId);

        //判断当前用户是否有购物车，没有则创建购物车
        Cart cart = cartLogic.cartService.selectOne(cartWrapper);
        if (cart == null) {
            cart = CartLogic.CreateCart(openId, unionId);
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
                cartProduct.setActivityId(activityId);
                cartProduct.setActivityTypeId(activityType.getValue());

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
    //endregion

    //region (public) 获取用户的购物车 GetCart

    /**
     * 获取用户的购物车
     *
     * @param
     */
    public static Cart GetCart(String unionId) {
        if (StringUtils.isBlank(unionId)) {
            throw new TipException("参数不合法，用户unionId为空");
        }
        EntityWrapper<Cart> wrapper = new EntityWrapper<>();
        wrapper.where("union_id={0} and delete_time is null", unionId);
        Cart cart = cartLogic.cartService.selectOne(wrapper);
        return cart;
    }
    //endregion

    //region (public) 获取用户购物车中商品列表 GetCartProducts

    /**
     * 获取用户购物车中商品列表
     *
     * @param unionId
     * @return
     */
    public static List<CartProductDto> GetCartProducts(String openId, String unionId) {

        if (StringUtils.isBlank(openId)) {
            throw new TipException("参数不合法，用户openId为空");
        }
        if (StringUtils.isBlank(unionId)) {
            throw new TipException("参数不合法，用户unionId为空");
        }

        List<CartProductDto> cartProductDtos = new ArrayList<>();

        Cart cart = CartLogic.GetCart(unionId);
        if (cart == null) {
            return cartProductDtos;
        }

        EntityWrapper<CartProduct> wrapper = new EntityWrapper<>();
        wrapper.where("cart_id={0} and delete_time is null", cart.getCartId());
        wrapper.orderBy("create_time desc");

        List<CartProduct> cartProducts = cartLogic.cartProductService.selectList(wrapper);

        for (CartProduct cartProduct : cartProducts) {

            CartProductDto dto = new CartProductDto();

            ProductDto productDto = ProductLogic.GetProduct(cartProduct.getProductId());

            dto.setProductDto(productDto);
            dto.setCartId(cartProduct.getCartId());
            dto.setNumber(cartProduct.getNumber());

            dto.setCartProductId(cartProduct.getCartProductId());
            dto.setOpenId(openId);
            dto.setProductSpecId(cartProduct.getProductSpecId());
            dto.setProductId(cartProduct.getProductId());
            dto.setCreateTime(cartProduct.getCreateTime());
            dto.setUpdateTime(cartProduct.getUpdateTime());
            dto.setDeleteTime(cartProduct.getDeleteTime());

            dto.setActivityId(cartProduct.getActivityId());
            dto.setActivityType(ActivityType.valueOf(cartProduct.getActivityTypeId()));

            //过滤非法商品
            //if (cartProduct.getActivityId() != null) {
            //    List<ActivityProductDto> activityProductDtos = ActivityLogic.GetActivityProductDtos(cartProduct.getActivityId());
            //    if (activityProductDtos.isEmpty()) {
            //        continue;
            //    }
            //    Optional<ActivityProductDto> first = activityProductDtos.stream().filter(apd -> apd.getProductId().equals(cartProduct.getProductId()) && apd.getProductSpecId().equals(cartProduct.getProductSpecId())).findFirst();
            //    if (!first.isPresent()) {
            //        continue;
            //    }
            //}

            cartProductDtos.add(dto);
        }


        return cartProductDtos;
    }
    //endregion

    //region (public) 获得用户购物车中商品数量 GetCartProductCount

    /**
     * 获得用户购物车中商品数量
     *
     * @param openId
     * @param unionId
     * @return
     */
    public static int GetCartProductCount(String openId, String unionId) {
        int amount = 0;
        List<CartProductDto> cartProducts = CartLogic.GetCartProducts(openId, unionId);
        for (CartProductDto cartProduct : cartProducts) {
            amount += cartProduct.getNumber();
        }
        return amount;
    }
    //endregion

}
