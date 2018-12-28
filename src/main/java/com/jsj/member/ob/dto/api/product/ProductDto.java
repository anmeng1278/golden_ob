package com.jsj.member.ob.dto.api.product;

import com.jsj.member.ob.enums.PropertyType;

import java.util.ArrayList;
import java.util.List;

public class ProductDto {

    public ProductDto() {
        this.productImgDtos = new ArrayList<>();
        this.productSpecDtos = new ArrayList<>();
    }

    /**
     * 主键
     */
    private Integer productId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品分类,字典表主键
     */
    private Integer typeId;

    /**
     * 商品属性
     */
    private PropertyType propertyType;

    /**
     * 简介
     */
    private String introduce;

    /**
     * 销售单价
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
     * 秒杀价(0时不支持秒杀 >0时支持秒杀)
     */
    private double secPrice;

    /**
     * 父编号(组团商品使用)
     */
    private int parentProductId;

    /**
     * 卡类型
     */
    private int cardTypeId;

    /**
     * 使用说明
     */
    private String useIntro;

    /**
     * 单位
     */
    private String unit;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 赠送文案
     */
    private String giftCopywriting;

    /**
     * 是否支持自提，0：不支持，1：支持
     */
    private Boolean ifpickup;

    /**
     * 是否审核
     */
    private Boolean ifpass;

    /**
     * 是否支持配送
     */
    private Boolean ifdistribution;

    /**
     * 商品图片
     */
    private List<ProductImgDto> productImgDtos;

    /**
     * 商品尺寸
     */
    private List<ProductSpecDto> productSpecDtos;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
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

    public double getSecPrice() {
        return secPrice;
    }

    public void setSecPrice(double secPrice) {
        this.secPrice = secPrice;
    }

    public int getParentProductId() {
        return parentProductId;
    }

    public void setParentProductId(int parentProductId) {
        this.parentProductId = parentProductId;
    }

    public String getUseIntro() {
        return useIntro;
    }

    public void setUseIntro(String useIntro) {
        this.useIntro = useIntro;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getGiftCopywriting() {
        return giftCopywriting;
    }

    public void setGiftCopywriting(String giftCopywriting) {
        this.giftCopywriting = giftCopywriting;
    }

    public Boolean getIfpickup() {
        return ifpickup;
    }

    public void setIfpickup(Boolean ifpickup) {
        this.ifpickup = ifpickup;
    }

    public Boolean getIfpass() {
        return ifpass;
    }

    public void setIfpass(Boolean ifpass) {
        this.ifpass = ifpass;
    }

    public Boolean getIfdistribution() {
        return ifdistribution;
    }

    public void setIfdistribution(Boolean ifdistribution) {
        this.ifdistribution = ifdistribution;
    }

    public List<ProductImgDto> getProductImgDtos() {
        return productImgDtos;
    }

    public void setProductImgDtos(List<ProductImgDto> productImgDtos) {
        this.productImgDtos = productImgDtos;
    }

    public List<ProductSpecDto> getProductSpecDtos() {
        return productSpecDtos;
    }

    public void setProductSpecDtos(List<ProductSpecDto> productSpecDtos) {
        this.productSpecDtos = productSpecDtos;
    }

    public int getCardTypeId() {
        return cardTypeId;
    }

    public void setCardTypeId(int cardTypeId) {
        this.cardTypeId = cardTypeId;
    }
}
