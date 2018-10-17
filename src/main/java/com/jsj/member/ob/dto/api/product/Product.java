package com.jsj.member.ob.dto.api.product;

import java.util.ArrayList;
import java.util.List;

public class Product {

    public Product() {
        this.productImgs = new ArrayList<>();
        this.productSizes = new ArrayList<>();
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
     * 商品属性  实物 次卡 月卡
     */
    private Integer propertyTypeId;

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
     * 秒杀库存
     */
    private int secStockCount;

    /**
     * 秒杀编号
     */
    private int seckillId;

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
    private List<ProductImg> productImgs;

    /**
     * 商品尺寸
     */
    private List<ProductSize> productSizes;


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

    public Integer getPropertyTypeId() {
        return propertyTypeId;
    }

    public void setPropertyTypeId(Integer propertyTypeId) {
        this.propertyTypeId = propertyTypeId;
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

    public List<ProductImg> getProductImgs() {
        return productImgs;
    }

    public void setProductImgs(List<ProductImg> productImgs) {
        this.productImgs = productImgs;
    }

    public List<ProductSize> getProductSizes() {
        return productSizes;
    }

    public void setProductSizes(List<ProductSize> productSizes) {
        this.productSizes = productSizes;
    }

    public int getSecStockCount() {
        return secStockCount;
    }

    public void setSecStockCount(int secStockCount) {
        this.secStockCount = secStockCount;
    }

    public int getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(int seckillId) {
        this.seckillId = seckillId;
    }
}
