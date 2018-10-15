package com.jsj.member.ob.dto.api.coupon;

public class UseCouponResp {

    /**
     * 支付金额
     */
    private double payAmount;

    /**
     * 抵扣金额
     */
    private double discountAmount;

    /**
     * 使用优惠券编号
     */
    private int wechatCouponId;

    public double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(double payAmount) {
        this.payAmount = payAmount;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public int getWechatCouponId() {
        return wechatCouponId;
    }

    public void setWechatCouponId(int wechatCouponId) {
        this.wechatCouponId = wechatCouponId;
    }
}
