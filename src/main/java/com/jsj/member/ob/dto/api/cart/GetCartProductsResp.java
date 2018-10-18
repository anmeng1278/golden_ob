package com.jsj.member.ob.dto.api.cart;


import java.util.List;

public class GetCartProductsResp {


    private List<CartProductDto> cartProductDtoList;

    public List<CartProductDto> getCartProductDtoList() {
        return cartProductDtoList;
    }

    public void setCartProductDtoList(List<CartProductDto> cartProductDtoList) {
        this.cartProductDtoList = cartProductDtoList;
    }

}
