package com.jsj.member.ob.dto.mini;

import com.jsj.member.ob.dto.api.cart.CartProductDto;

import java.util.List;

public class CartResp {


    private List<CartProductDto> cartProductDtos;

    public List<CartProductDto> getCartProductDtos() {
        return cartProductDtos;
    }

    public void setCartProductDtos(List<CartProductDto> cartProductDtos) {
        this.cartProductDtos = cartProductDtos;
    }
}
