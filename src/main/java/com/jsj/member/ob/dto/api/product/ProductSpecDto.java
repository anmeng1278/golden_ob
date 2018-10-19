package com.jsj.member.ob.dto.api.product;

public class ProductSpecDto {

    /**
     * 主键
     */
    private Integer productSpecId;
    /**
     * 商品编号
     */
    private Integer productId;
    /**
     * 规格名称
     */
    private String specName;

    /**
     * 售价
     */
    private Double salePrice;
    /**
     * 原价
     */
    private Double originalPrice;
    /**
     * 库存量
     */
    private Integer stockCount;
    /**
     * 排序
     */
    private Integer sort;

    public Integer getProductSpecId() {
        return productSpecId;
    }

    public void setProductSpecId(Integer productSpecId) {
        this.productSpecId = productSpecId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Integer getStockCount() {
        return stockCount;
    }

    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
