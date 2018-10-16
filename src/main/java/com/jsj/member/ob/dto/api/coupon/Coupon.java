package com.jsj.member.ob.dto.api.coupon;

import com.jsj.member.ob.dto.api.BaseDto;
import com.jsj.member.ob.enums.CouponType;
import com.jsj.member.ob.enums.CouponUseRange;

public class Coupon extends BaseDto {

    /**
     * 券编号
     */
    private int couponId;

    /**
     * 券名称
     */
    private String couponName;

    /**
     * 券的金额, 单位元
     */
    private Double amount;
    /**
     * 类型 1直减券  2折扣券 (值为2时折扣使用amount字段)
     */
    private CouponType couponType;
    /**
     * 使用说明
     */
    private String instruction;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 使用范围 1全部商品  2指定商品(值为2时范围表_coupon_product表)
     */
    private CouponUseRange couponUseRange;
    /**
     * 有效天数
     */
    private Integer expiredDays;

    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public CouponType getCouponType() {
        return couponType;
    }

    public void setCouponType(CouponType couponType) {
        this.couponType = couponType;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public CouponUseRange getCouponUseRange() {
        return couponUseRange;
    }

    public void setCouponUseRange(CouponUseRange couponUseRange) {
        this.couponUseRange = couponUseRange;
    }

    public Integer getExpiredDays() {
        return expiredDays;
    }

    public void setExpiredDays(Integer expiredDays) {
        this.expiredDays = expiredDays;
    }
}
