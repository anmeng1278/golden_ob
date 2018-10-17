package com.jsj.member.ob.dto.api.cart;

import com.jsj.member.ob.dto.BaseRequ;

import java.util.ArrayList;
import java.util.List;

public class GetCartProductsRequ {

    public  GetCartProductsRequ(){
        this.baseRequ = new BaseRequ();
        this.cartProductDtos = new ArrayList<>();
    }
    private BaseRequ baseRequ;


    public BaseRequ getBaseRequ() {
        return baseRequ;
    }

    public void setBaseRequ(BaseRequ baseRequ) {
        this.baseRequ = baseRequ;
    }

    /**
     * 购物车商品列表
     */
    private List<CartProductDto> cartProductDtos;

    public List<CartProductDto> getCartProductDtos() {
        return cartProductDtos;
    }

    public void setCartProductDtos(List<CartProductDto> cartProductDtos) {
        this.cartProductDtos = cartProductDtos;
    }
}
