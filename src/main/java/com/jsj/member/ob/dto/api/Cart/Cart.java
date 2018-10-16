package com.jsj.member.ob.dto.api.Cart;

import com.jsj.member.ob.dto.api.BaseDto;

public class Cart extends BaseDto {

    /**
     * 购物车id
     */
    private Integer cartId;

    /**
     * 用户openid
     */
    private Integer openId;

    /**
     * 商品id
     */
    private Integer productId;

    /**
     * 商品规格
     */
    private Integer productSizeId;

    /**
     * 商品数量
     */
    private Integer number;

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Integer getOpenId() {
        return openId;
    }

    public void setOpenId(Integer openId) {
        this.openId = openId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getProductSizeId() {
        return productSizeId;
    }

    public void setProductSizeId(Integer productSizeId) {
        this.productSizeId = productSizeId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
