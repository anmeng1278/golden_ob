package com.jsj.member.ob.dto.api.redpacket;

import com.jsj.member.ob.dto.api.coupon.CouponDto;
import com.jsj.member.ob.dto.api.coupon.WechatCouponDto;
import com.jsj.member.ob.dto.api.wechat.WechatDto;
import com.jsj.member.ob.enums.CouponType;

public class OrderRedpacketCouponDto {


    private Integer orderRedpacketCouponId;
    /**
     * 订单表主键
     */
    private Integer orderId;
    /**
     * 礼包代金券表主键
     */
    private Integer redpacketCouponId;
    /**
     * 代金券表主键
     */
    private Integer couponId;
    /**
     * 类型 1直减券  2折扣券 (值为2时折扣使用amount字段)
     */
    private CouponType couponType;
    /**
     * 代金券金额
     */
    private Double amount;
    /**
     * 领取人编号
     */
    private String openId;
    /**
     * 是否领取
     */
    private Boolean ifreceived;
    /**
     * 领取时间
     */
    private Integer receivedTime;

    /**
     * 领取券
     */
    public CouponDto couponDto;

    /**
     * 领取人
     */
    public WechatDto wechatDto;

    /**
     * 领取明细
     */
    public WechatCouponDto wechatCouponDto;

    public Integer getOrderRedpacketCouponId() {
        return orderRedpacketCouponId;
    }

    public void setOrderRedpacketCouponId(Integer orderRedpacketCouponId) {
        this.orderRedpacketCouponId = orderRedpacketCouponId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getRedpacketCouponId() {
        return redpacketCouponId;
    }

    public void setRedpacketCouponId(Integer redpacketCouponId) {
        this.redpacketCouponId = redpacketCouponId;
    }

    public Integer getCouponId() {
        return couponId;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    public CouponType getCouponType() {
        return couponType;
    }

    public void setCouponType(CouponType couponType) {
        this.couponType = couponType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Boolean getIfreceived() {
        return ifreceived;
    }

    public void setIfreceived(Boolean ifreceived) {
        this.ifreceived = ifreceived;
    }

    public Integer getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(Integer receivedTime) {
        this.receivedTime = receivedTime;
    }

    public CouponDto getCouponDto() {
        return couponDto;
    }

    public void setCouponDto(CouponDto couponDto) {
        this.couponDto = couponDto;
    }

    public WechatDto getWechatDto() {
        return wechatDto;
    }

    public void setWechatDto(WechatDto wechatDto) {
        this.wechatDto = wechatDto;
    }

    public WechatCouponDto getWechatCouponDto() {
        return wechatCouponDto;
    }

    public void setWechatCouponDto(WechatCouponDto wechatCouponDto) {
        this.wechatCouponDto = wechatCouponDto;
    }
}
