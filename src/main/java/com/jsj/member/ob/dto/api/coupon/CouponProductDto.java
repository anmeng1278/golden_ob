package com.jsj.member.ob.dto.api.coupon;


import com.jsj.member.ob.dto.api.product.ProductDto;

public class CouponProductDto {

    /**
     * 主键
     */
    private Integer couponProductId;
    /**
     * 优惠券编号
     */
    private Integer couponId;
    /**
     * 商品编号
     */
    private Integer productId;

    private ProductDto productDto;

    public Integer getCouponProductId() {
        return couponProductId;
    }

    public void setCouponProductId(Integer couponProductId) {
        this.couponProductId = couponProductId;
    }

    public Integer getCouponId() {
        return couponId;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public ProductDto getProductDto() {
        return productDto;
    }

    public void setProductDto(ProductDto productDto) {
        this.productDto = productDto;
    }
}
