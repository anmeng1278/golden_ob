package com.jsj.member.ob.dto.api.gift;

import com.jsj.member.ob.enums.PropertyType;
import com.jsj.member.ob.enums.StockStatus;

public class UserDrawDto {

    /**
     * 赠送编号
     */
    private int giftId;

    /**
     * 赠送唯一码
     */
    private String giftUniqueCode;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 赠送时间
     */
    private int createTime;

    /**
     * 领取状态
     */
    private StockStatus stockStatus;

    /**
     * 图片
     */
    private String imgPath;

    /**
     * 商品编号
     */
    private int productId;

    /**
     * 规格编号
     */
    private int productSpecId;

    /**
     * 领取时间
     */
    private int drawTime;

    /**
     * 领取数量
     */
    private int count;

    /**
     * 属性类型
     */
    private PropertyType propertyType;

    public int getGiftId() {
        return giftId;
    }

    public void setGiftId(int giftId) {
        this.giftId = giftId;
    }

    public String getGiftUniqueCode() {
        return giftUniqueCode;
    }

    public void setGiftUniqueCode(String giftUniqueCode) {
        this.giftUniqueCode = giftUniqueCode;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public StockStatus getStockStatus() {
        return stockStatus;
    }

    public void setStockStatus(StockStatus stockStatus) {
        this.stockStatus = stockStatus;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getProductSpecId() {
        return productSpecId;
    }

    public void setProductSpecId(int productSpecId) {
        this.productSpecId = productSpecId;
    }

    public int getDrawTime() {
        return drawTime;
    }

    public void setDrawTime(int drawTime) {
        this.drawTime = drawTime;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }
}
