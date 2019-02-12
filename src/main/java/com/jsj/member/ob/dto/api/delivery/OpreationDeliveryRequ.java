package com.jsj.member.ob.dto.api.delivery;

/**
 * 发货、开卡、创建活动码操作
 */
public class OpreationDeliveryRequ {

    /**
     * 配送编号
     */
    private int deliveryId;

    /**
     * 快递号
     */
    private String expressNumber;

    /**
     * 会员编号
     */
    private int jsjId;

    public int getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(int deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getExpressNumber() {
        return expressNumber;
    }

    public void setExpressNumber(String expressNumber) {
        this.expressNumber = expressNumber;
    }

    public int getJsjId() {
        return jsjId;
    }

    public void setJsjId(int jsjId) {
        this.jsjId = jsjId;
    }
}
