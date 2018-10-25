package com.jsj.member.ob.dto.api.activity;

public class ActivityProductDto {

    /**
     * 主键
     */
    private Integer activityProductId;

    /**
     * 活动编号
     */
    private Integer activityId;

    /**
     * 商品编号
     */
    private Integer productId;

    /**
     * 售价(秒杀价)
     */
    private double salePrice;

    /**
     * 规格编号
     */
    private Integer productSpecId;
    /**
     * 创建时间
     */
    private Integer createTime;
    /**
     * 更新时间
     */
    private Integer updateTime;
    /**
     * 删除时间
     */
    private Integer deleteTime;

    public Integer getActivityProductId() {
        return activityProductId;
    }

    public void setActivityProductId(Integer activityProductId) {
        this.activityProductId = activityProductId;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getProductSpecId() {
        return productSpecId;
    }

    public void setProductSpecId(Integer productSpecId) {
        this.productSpecId = productSpecId;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Integer deleteTime) {
        this.deleteTime = deleteTime;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }
}