package com.jsj.member.ob.dto.api.redpacket;


import com.jsj.member.ob.dto.api.coupon.CouponDto;
import com.jsj.member.ob.entity.Coupon;

import java.util.List;

public class RedpacketCouponDto {

    private Integer redpacketCouponId;
    /**
     * 代金券表主键
     */
    private Integer couponId;
    /**
     * 礼包表主键
     */
    private Integer redpacketId;
    /**
     */
    private Integer number;
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

    private CouponDto couponDto;

    public CouponDto getCouponDto() {
        return couponDto;
    }

    public void setCouponDto(CouponDto couponDto) {
        this.couponDto = couponDto;
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

    public Integer getRedpacketId() {
        return redpacketId;
    }

    public void setRedpacketId(Integer redpacketId) {
        this.redpacketId = redpacketId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
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
