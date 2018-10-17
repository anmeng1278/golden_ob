package com.jsj.member.ob.dto.api.product;

import com.jsj.member.ob.enums.ProductImgType;

public class ProductImgDto {

    /**
     * 主键
     */
    private Integer productImgId;

    /**
     * 商品编号
     */
    private Integer productId;

    /**
     * 图片地址
     */
    private String imgPath;

    /**
     * 类型 0 封面  1 商品图片
     */
    private ProductImgType productImgType;

    public Integer getProductImgId() {
        return productImgId;
    }

    public void setProductImgId(Integer productImgId) {
        this.productImgId = productImgId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public ProductImgType getProductImgType() {
        return productImgType;
    }

    public void setProductImgType(ProductImgType productImgType) {
        this.productImgType = productImgType;
    }
}
