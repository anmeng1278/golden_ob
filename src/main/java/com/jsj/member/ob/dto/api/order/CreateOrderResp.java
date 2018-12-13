package com.jsj.member.ob.dto.api.order;

public class CreateOrderResp {

    /**
     * 是否创建成功
     */
    private boolean isSuccess;

    /**
     * 错误消息
     */
    private String message;

    /**
     * 订单支付金额
     */
    private double amount;

    /**
     * 订单号
     */
    private int orderId;

    /**
     * 订单支付超时时间
     */
    public int expiredTime;


    /**
     * 优惠金额
     */
    private double couponAmount;

    /**
     * 原金额
     */
    private double originalAmount;

    public double getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(double couponAmount) {
        this.couponAmount = couponAmount;
    }

    public double getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(double originalAmount) {
        this.originalAmount = originalAmount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }


    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(int expiredTime) {
        this.expiredTime = expiredTime;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
