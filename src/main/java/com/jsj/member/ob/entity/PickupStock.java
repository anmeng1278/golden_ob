package com.jsj.member.ob.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *   @description : PickupStock 提货库存表实体类
 *   ---------------------------------
 * 	 @author cc
 *   @since 2018-10-15
 */
@TableName("_pickup_stock")
public class PickupStock implements Serializable {

    private static final long serialVersionUID = 1L;

	@TableId(value="pickup_stock_id", type= IdType.AUTO)
	private Integer pickupStockId;
    /**
     * 提取表主键
     */
	@TableField("pickup_id")
	private Integer pickupId;
    /**
     * 用户库存表主键
     */
	@TableField("stock_id")
	private Integer stockId;
    /**
     *  商品编号
     */
	@TableField("product_id")
	private Integer productId;
    /**
     * 订单编号
     */
	@TableField("order_id")
	private Integer orderId;
    /**
     * _order_product表主键
     */
	@TableField("order_product_id")
	private Integer orderProductId;
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


	public Integer getPickupStockId() {
		return pickupStockId;
	}

	public void setPickupStockId(Integer pickupStockId) {
		this.pickupStockId = pickupStockId;
	}

	public Integer getPickupId() {
		return pickupId;
	}

	public void setPickupId(Integer pickupId) {
		this.pickupId = pickupId;
	}

	public Integer getStockId() {
		return stockId;
	}

	public void setStockId(Integer stockId) {
		this.stockId = stockId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getOrderProductId() {
		return orderProductId;
	}

	public void setOrderProductId(Integer orderProductId) {
		this.orderProductId = orderProductId;
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
		return "PickupStock{" +
			", pickupStockId=" + pickupStockId +
			", pickupId=" + pickupId +
			", stockId=" + stockId +
			", productId=" + productId +
			", orderId=" + orderId +
			", orderProductId=" + orderProductId +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", deleteTime=" + deleteTime +
			"}";
	}
}
