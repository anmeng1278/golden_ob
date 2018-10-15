package com.jsj.member.ob.dto.api.order;

public class CreateOrderResp {

    /**
     * 订单支付金额
     */
    private double amount;

    /**
     * 订单号
     */
    private String orderNumber;

    /**
     * 订单支付超时时间
     */
    public int expiredTime;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(int expiredTime) {
        this.expiredTime = expiredTime;
    }
}
