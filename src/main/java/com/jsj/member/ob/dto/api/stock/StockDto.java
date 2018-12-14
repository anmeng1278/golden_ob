package com.jsj.member.ob.dto.api.stock;

import com.jsj.member.ob.dto.api.gift.GiftDto;
import com.jsj.member.ob.dto.api.product.ProductDto;
import com.jsj.member.ob.dto.api.product.ProductSpecDto;
import com.jsj.member.ob.dto.api.wechat.WechatDto;
import com.jsj.member.ob.enums.StockStatus;
import com.jsj.member.ob.enums.StockType;

import java.util.Objects;

public class StockDto {

    /**
     * 库存id
     */
    private Integer stockId;

    /**
     * 用户openId
     */
    private String openId;

    /**
     * 商品id
     */
    private Integer productId;

    /**
     * 商品规格id
     */
    private int productSpecId;

    /**
     * 订单id
     */
    private Integer orderId;

    /**
     * 商品数量
     */
    private Integer number;

    /**
     * 规格
     */
    private ProductSpecDto productSpecDto;

    /**
     * 赠送详情
     */
    private GiftDto giftDto;


    /**
     * 库存状态
     */
    private StockStatus stockStatus;

    /**
     * 获取方式
     */
    private StockType stockType;

    /**
     * 创建时间
     */
    private Integer createTime;

    /**
     * 用户
     */
    private WechatDto wechatDto;

    /**
     * 父编号
     */
    private int parentStockId;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockDto stockDto = (StockDto) o;
        return Objects.equals(productId, stockDto.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }

    public int getParentStockId() {
        return parentStockId;
    }

    public void setParentStockId(int parentStockId) {
        this.parentStockId = parentStockId;
    }

    public Integer getStockId() {
        return stockId;
    }

    public void setStockId(Integer stockId) {
        this.stockId = stockId;
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

    public int getProductSpecId() {
        return productSpecId;
    }

    public void setProductSpecId(int productSpecId) {
        this.productSpecId = productSpecId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public ProductSpecDto getProductSpecDto() {
        return productSpecDto;
    }

    public void setProductSpecDto(ProductSpecDto productSpecDto) {
        this.productSpecDto = productSpecDto;
    }

    public GiftDto getGiftDto() {
        return giftDto;
    }

    public void setGiftDto(GiftDto giftDto) {
        this.giftDto = giftDto;
    }

    public StockStatus getStockStatus() {
        return stockStatus;
    }

    public void setStockStatus(StockStatus stockStatus) {
        this.stockStatus = stockStatus;
    }

    public StockType getStockType() {
        return stockType;
    }

    public void setStockType(StockType stockType) {
        this.stockType = stockType;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public WechatDto getWechatDto() {
        return wechatDto;
    }

    public void setWechatDto(WechatDto wechatDto) {
        this.wechatDto = wechatDto;
    }
}
