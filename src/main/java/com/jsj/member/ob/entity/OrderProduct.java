package com.jsj.member.ob.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

/**
 *   @description : OrderProduct 订单商品表实体类
 *   ---------------------------------
 * 	 @author cc
 *   @since 2019-02-13
 */
@TableName("_order_product")
public class OrderProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId(value="order_product_id", type= IdType.AUTO)
	private Integer orderProductId;
	@TableField("order_id")
	private Integer orderId;
    /**
     * 商品编号
     */
	@TableField("product_id")
	private Integer productId;
    /**
     * 规格编号
     */
	@TableField("product_spec_id")
	private Integer productSpecId;
    /**
     * 商品数
     */
	private Integer number;
    /**
     * 优惠券使用金额
     */
	@TableField("coupon_price")
	private Double couponPrice;
    /**
     * 权益使用金额
     */
	@TableField("equity_price")
	private Double equityPrice;
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


	public Integer getOrderProductId() {
		return orderProductId;
	}

	public void setOrderProductId(Integer orderProductId) {
		this.orderProductId = orderProductId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getProductSpecId() {
		return productSpecId;
	}

	public void setProductSpecId(Integer productSpecId) {
		this.productSpecId = productSpecId;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
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
		return "OrderProduct{" +
			", orderProductId=" + orderProductId +
			", orderId=" + orderId +
			", productId=" + productId +
			", productSpecId=" + productSpecId +
			", number=" + number +
			", couponPrice=" + couponPrice +
			", equityPrice=" + equityPrice +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", deleteTime=" + deleteTime +
			"}";
	}
}
