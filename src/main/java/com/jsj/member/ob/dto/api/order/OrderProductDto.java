package com.jsj.member.ob.dto.api.order;

import com.jsj.member.ob.dto.api.product.ProductDto;

public class OrderProductDto {

    /**
     * 商品编号
     */
    private int productId;

    /**
     * 购买数据
     */
    private int number;

    /**
     * 规格型号编号
     */
    private int productSpecId;

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

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getProductSpecId() {
        return productSpecId;
    }

    public void setProductSpecId(int productSpecId) {
        this.productSpecId = productSpecId;
    }
}
