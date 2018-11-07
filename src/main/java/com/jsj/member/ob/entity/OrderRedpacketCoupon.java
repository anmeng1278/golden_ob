package com.jsj.member.ob.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *   @description : OrderRedpacketCoupon 订单礼包代金券表实体类
 *   ---------------------------------
 * 	 @author cc
 *   @since 2018-11-07
 */
@TableName("_order_redpacket_coupon")
public class OrderRedpacketCoupon implements Serializable {

    private static final long serialVersionUID = 1L;

	@TableId(value="order_redpacket_coupon_id", type= IdType.AUTO)
	private Integer orderRedpacketCouponId;
    /**
     * 订单表主键
     */
	@TableField("order_id")
	private Integer orderId;
    /**
     * 礼包代金券表主键
     */
	@TableField("redpacket_coupon_id")
	private Integer redpacketCouponId;
    /**
     * 代金券表主键
     */
	@TableField("coupon_id")
	private Integer couponId;
    /**
     * 类型 1直减券  2折扣券 (值为2时折扣使用amount字段)
     */
	@TableField("type_id")
	private Integer typeId;
    /**
     * 代金券金额
     */
	private Double amount;
    /**
     * 领取人编号
     */
	@TableField("open_id")
	private Integer openId;
    /**
     * 是否领取
     */
	private Boolean ifreceived;
    /**
     * 领取时间
     */
	@TableField("received_time")
	private Integer receivedTime;
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

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getOpenId() {
		return openId;
	}

	public void setOpenId(Integer openId) {
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
		return "OrderRedpacketCoupon{" +
			", orderRedpacketCouponId=" + orderRedpacketCouponId +
			", orderId=" + orderId +
			", redpacketCouponId=" + redpacketCouponId +
			", couponId=" + couponId +
			", typeId=" + typeId +
			", amount=" + amount +
			", openId=" + openId +
			", ifreceived=" + ifreceived +
			", receivedTime=" + receivedTime +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", deleteTime=" + deleteTime +
			"}";
	}
}
