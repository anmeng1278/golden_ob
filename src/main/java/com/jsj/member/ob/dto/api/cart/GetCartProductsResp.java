package com.jsj.member.ob.dto.api.cart;

import java.util.ArrayList;
import java.util.List;

public class GetCartProductsResp {

    public GetCartProductsResp(){
        this.cartProductDtos = new ArrayList<>();
    }

    /**
     * 购物车商品列表
     */
    private List<com.jsj.member.ob.dto.api.Cart.CartProductDto> cartProductDtos;

}
