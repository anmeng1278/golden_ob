package com.jsj.member.ob.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *   @description : Order 订单表实体类
 *   ---------------------------------
 * 	 @author cc
 *   @since 2019-02-11
 */
@TableName("_order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单主键
     */
	@TableId(value="order_id", type= IdType.AUTO)
	private Integer orderId;
    /**
     * 订单编号
     */
	@TableField("order_unique_code")
	private String orderUniqueCode;
    /**
     * 公众号open_id
     */
	@TableField("open_id")
	private String openId;
	@TableField("union_id")
	private String unionId;
    /**
     * 订单金额
     */
	private Double amount;
    /**
     * 需要支付金额 = amount - coupon_price
     */
	@TableField("pay_amount")
	private Double payAmount;
    /**
     * 订单状态，0：待支付 10：支付成功 20：支付失败 60：取消订单
     */
	private Integer status;
    /**
     * 微信的支付订单号
     */
	@TableField("transaction_id")
	private String transactionId;
    /**
     * 领取的优惠券ID
     */
	@TableField("wechat_coupon_id")
	private Integer wechatCouponId;
    /**
     * 优惠券使用金额
     */
	@TableField("coupon_price")
	private Double couponPrice;
	@TableField("equity_price")
	private Double equityPrice;
    /**
     * 备注
     */
	private String remarks;
    /**
     * 活动编号
     */
	@TableField("activity_id")
	private Integer activityId;
    /**
     * 活动订单编号(团购时不为空)
     */
	@TableField("activity_order_id")
	private Integer activityOrderId;
    /**
     * 0 普通订单  10团单  20秒杀单 30商品组合单
     */
	@TableField("type_id")
	private Integer typeId;
    /**
     * 订单来源 0空铁公众号 1空铁小程序 2金色世纪小程序 3金色世纪公众号
     */
	@TableField("order_source_id")
	private Integer orderSourceId;
    /**
     * 支付时间
     */
	@TableField("pay_time")
	private Integer payTime;
    /**
     * 支付有效时间
     */
	@TableField("expired_time")
	private Integer expiredTime;
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


	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getOrderUniqueCode() {
		return orderUniqueCode;
	}

	public void setOrderUniqueCode(String orderUniqueCode) {
		this.orderUniqueCode = orderUniqueCode;
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

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(Double payAmount) {
		this.payAmount = payAmount;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public Integer getWechatCouponId() {
		return wechatCouponId;
	}

	public void setWechatCouponId(Integer wechatCouponId) {
		this.wechatCouponId = wechatCouponId;
	}

	public Double getCouponPrice() {
		return couponPrice;
	}

	public void setCouponPrice(Double couponPrice) {
		this.couponPrice = couponPrice;
	}

	public Double getEquityPrice() {
		return equityPrice;
	}

	public void setEquityPrice(Double equityPrice) {
		this.equityPrice = equityPrice;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}

	public Integer getActivityOrderId() {
		return activityOrderId;
	}

	public void setActivityOrderId(Integer activityOrderId) {
		this.activityOrderId = activityOrderId;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public Integer getOrderSourceId() {
		return orderSourceId;
	}

	public void setOrderSourceId(Integer orderSourceId) {
		this.orderSourceId = orderSourceId;
	}

	public Integer getPayTime() {
		return payTime;
	}

	public void setPayTime(Integer payTime) {
		this.payTime = payTime;
	}

	public Integer getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(Integer expiredTime) {
		this.expiredTime = expiredTime;
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
		return "Order{" +
			", orderId=" + orderId +
			", orderUniqueCode=" + orderUniqueCode +
			", openId=" + openId +
			", unionId=" + unionId +
			", amount=" + amount +
			", payAmount=" + payAmount +
			", status=" + status +
			", transactionId=" + transactionId +
			", wechatCouponId=" + wechatCouponId +
			", couponPrice=" + couponPrice +
			", equityPrice=" + equityPrice +
			", remarks=" + remarks +
			", activityId=" + activityId +
			", activityOrderId=" + activityOrderId +
			", typeId=" + typeId +
			", orderSourceId=" + orderSourceId +
			", payTime=" + payTime +
			", expiredTime=" + expiredTime +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", deleteTime=" + deleteTime +
			"}";
	}
}
