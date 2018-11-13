package com.jsj.member.ob.dto.api.activity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.jsj.member.ob.enums.ActivityType;

public class ActivityDto {

    /**
     * 活动编号
     */
    private Integer activityId;
    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 活动类型
     */
    private ActivityType activityType;

    /**
     * 活动类型ID
     */
    private Integer typeId;

    /**
     * 团购活动参与人数
     */
    private Integer number;


    /**
     * 开始时间
     */
    private Integer beginTime;
    /**
     * 结束时间
     */
    private Integer endTime;
    /**
     * 是否审核
     */
    private Boolean ifpass;
    /**
     * 销售单价
     */
    private Double salePrice;
    /**
     * 原价
     */
    private Double originalPrice;
    /**
     * 库存
     */
    private Integer stockCount;
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


    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public Integer getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Integer beginTime) {
        this.beginTime = beginTime;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    public Boolean getIfpass() {
        return ifpass;
    }

    public void setIfpass(Boolean ifpass) {
        this.ifpass = ifpass;
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
}
