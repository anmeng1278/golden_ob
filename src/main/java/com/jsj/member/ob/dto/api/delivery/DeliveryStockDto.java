package com.jsj.member.ob.dto.api.delivery;

public class DeliveryStockDto {

    private Integer deliveryStockId;
    /**
     * 提取表主键
     */
    private Integer deliveryId;
    /**
     * 用户库存表主键
     */
    private Integer stockId;
    /**
     * 活动码
     */
    private String activityCode;

    public Integer getDeliveryStockId() {
        return deliveryStockId;
    }

    public void setDeliveryStockId(Integer deliveryStockId) {
        this.deliveryStockId = deliveryStockId;
    }

    public Integer getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Integer deliveryId) {
        this.deliveryId = deliveryId;
    }

    public Integer getStockId() {
        return stockId;
    }

    public void setStockId(Integer stockId) {
        this.stockId = stockId;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }
}
