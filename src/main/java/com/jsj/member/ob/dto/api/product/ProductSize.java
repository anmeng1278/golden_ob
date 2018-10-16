package com.jsj.member.ob.dto.api.product;

public class ProductSize {

    /**
     * 主键
     */
    private Integer productSizeId;
    /**
     * 商品编号
     */
    private Integer productId;
    /**
     * 尺寸名称
     */
    private String sizeName;

    public Integer getProductSizeId() {
        return productSizeId;
    }

    public void setProductSizeId(Integer productSizeId) {
        this.productSizeId = productSizeId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }
}
