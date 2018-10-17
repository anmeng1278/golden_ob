package com.jsj.member.ob.dto.api.cart;

import com.jsj.member.ob.dto.api.BaseDto;

public class CartDto extends BaseDto {

    /**
     * 购物车id
     */
    private Integer cartId;

    /**
     * 用户openId
     */
    private String openId;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

}
