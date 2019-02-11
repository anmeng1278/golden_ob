package com.jsj.member.ob.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *   @description : WechatCoupon 用户领取优惠券表实体类
 *   ---------------------------------
 * 	 @author cc
 *   @since 2019-02-11
 */
@TableName("_wechat_coupon")
public class WechatCoupon implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId(value="wechat_coupon_id", type= IdType.AUTO)
	private Integer wechatCouponId;
    /**
     * 订单红包表主键
     */
	@TableField("order_redpacket_coupon_id")
	private Integer orderRedpacketCouponId;
    /**
     * 微信openid
     */
	@TableField("open_id")
	private String openId;
	@TableField("union_id")
	private String unionId;
    /**
     * 优惠券编号
     */
	@TableField("coupon_id")
	private Integer couponId;
    /**
     * 过期时间
     */
	@TableField("expired_time")
	private Integer expiredTime;
    /**
     * 0未使用 10已使用 60已过期
     */
	private Integer status;
    /**
     * 券的金额, 单位元
     */
	private Double amount;
    /**
     * 类型 1直减券  2折扣券 (值为2时折扣使用amount字段)
     */
	@TableField("type_id")
	private Integer typeId;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Integer createTime;
    /**
     * 更新时间
     */
	@TableField("update_time")
	private Integer updateTime;
    /**
     * 删除时间
     */
	@TableField("delete_time")
	private Integer deleteTime;


	public Integer getWechatCouponId() {
		return wechatCouponId;
	}

	public void setWechatCouponId(Integer wechatCouponId) {
		this.wechatCouponId = wechatCouponId;
	}

	public Integer getOrderRedpacketCouponId() {
		return orderRedpacketCouponId;
	}

	public void setOrderRedpacketCouponId(Integer orderRedpacketCouponId) {
		this.orderRedpacketCouponId = orderRedpacketCouponId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getUnionId() {
		return unionId;
	}

	public void setUnionId(String unionId) {
		this.unionId = unionId;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
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


	@Override
	public String toString() {
		return "WechatCoupon{" +
			", wechatCouponId=" + wechatCouponId +
			", orderRedpacketCouponId=" + orderRedpacketCouponId +
			", openId=" + openId +
			", unionId=" + unionId +
			", couponId=" + couponId +
			", expiredTime=" + expiredTime +
			", status=" + status +
			", amount=" + amount +
			", typeId=" + typeId +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", deleteTime=" + deleteTime +
			"}";
	}
}
