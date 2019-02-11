package com.jsj.member.ob.dto.mini;

import com.jsj.member.ob.dto.api.cart.CartProductDto;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class UpdateCartRequ {


    @ApiModelProperty(value = "openId，必填", required = true)
    private String openId;

    @ApiModelProperty(value = "unionId，必填", required = true)
    private String unionId;


    @ApiModelProperty(value = "购物车商品信息", required = true)
    private List<CartProductDto> cartProductDtos;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public List<CartProductDto> getCartProductDtos() {
        return cartProductDtos;
    }

    public void setCartProductDtos(List<CartProductDto> cartProductDtos) {
        this.cartProductDtos = cartProductDtos;
    }
}
