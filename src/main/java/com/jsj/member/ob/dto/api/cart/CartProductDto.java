package com.jsj.member.ob.dto.api.cart;

import com.jsj.member.ob.dto.api.BaseDto;
import com.jsj.member.ob.dto.api.product.ProductDto;

public class CartProductDto extends BaseDto {


    /**
     * id
     */
    private Integer cartProductId;

    /**
     * 购物车id
     */
    private Integer cartId;

    /**
     * 用户openid
     */
    private String openId;

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

    /**
     * 商品信息
     */
    private ProductDto productDto;

    public ProductDto getProductDto() {
        return productDto;
    }

    public void setProductDto(ProductDto productDto) {
        this.productDto = productDto;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
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

    public Integer getCartProductId() {
        return cartProductId;
    }

    public void setCartProductId(Integer cartProductId) {
        this.cartProductId = cartProductId;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }
}
