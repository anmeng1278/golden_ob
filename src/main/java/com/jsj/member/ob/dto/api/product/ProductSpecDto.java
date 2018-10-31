package com.jsj.member.ob.dto.api.product;

import com.jsj.member.ob.dto.api.activity.ActivityDto;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecDto {

    public ProductSpecDto(){
        this.activityDtos = new ArrayList<>();
    }
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

    /**
     * 商品信息
     */
    private ProductDto productDto;

    /**
     * 正在参与的活动
     */
    private List<ActivityDto> activityDtos;

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

    public List<ActivityDto> getActivityDtos() {
        return activityDtos;
    }

    public void setActivityDtos(List<ActivityDto> activityDtos) {
        this.activityDtos = activityDtos;
    }

    public ProductDto getProductDto() {
        return productDto;
    }

    public void setProductDto(ProductDto productDto) {
        this.productDto = productDto;
    }
}
