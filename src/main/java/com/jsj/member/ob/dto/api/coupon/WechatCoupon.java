package com.jsj.member.ob.dto.api.coupon;

import com.jsj.member.ob.dto.api.BaseDto;
import com.jsj.member.ob.enums.CouponStatus;
import com.jsj.member.ob.enums.CouponType;

public class WechatCoupon extends BaseDto {

    /**
     * 主键
     */
    private Integer wechatCouponId;
    /**
     * 微信openid
     */
    private String openId;
    /**
     * 优惠券编号
     */
    private Integer couponId;
    /**
     * 过期时间
     */
    private Integer expiredTime;
    /**
     * 0未使用 10已使用 60已过期
     */
    private CouponStatus couponStatus;
    /**
     * 券的金额, 单位元
     */
    private double amount;
    /**
     * 类型 1直减券  2折扣券 (值为2时折扣使用amount字段)
     */
    private CouponType couponType;


    public Integer getWechatCouponId() {
        return wechatCouponId;
    }

    public void setWechatCouponId(Integer wechatCouponId) {
        this.wechatCouponId = wechatCouponId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Integer getCouponId() {
        return couponId;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    public Integer getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Integer expiredTime) {
        this.expiredTime = expiredTime;
    }

    public CouponStatus getCouponStatus() {
        return couponStatus;
    }

    public void setCouponStatus(CouponStatus couponStatus) {
        this.couponStatus = couponStatus;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public CouponType getCouponType() {
        return couponType;
    }

    public void setCouponType(CouponType couponType) {
        this.couponType = couponType;
    }
}
