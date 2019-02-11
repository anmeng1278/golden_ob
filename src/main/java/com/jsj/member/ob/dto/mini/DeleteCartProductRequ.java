package com.jsj.member.ob.dto.mini;

import io.swagger.annotations.ApiModelProperty;

public class DeleteCartProductRequ {


    @ApiModelProperty(value = "购物车商品编号，必填", required = true)
    private int cartProductId;

    public int getCartProductId() {
        return cartProductId;
    }

    public void setCartProductId(int cartProductId) {
        this.cartProductId = cartProductId;
    }
}
