package com.jsj.member.ob.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *   @description : Stock 用户库存表实体类
 *   ---------------------------------
 * 	 @author cc
 *   @since 2018-10-29
 */
@TableName("_stock")
public class Stock implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId(value="stock_id", type= IdType.AUTO)
	private Integer stockId;
    /**
     * 公众号open_id
     */
	@TableField("open_id")
	private String openId;
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
     * 1直接购买 2获赠
     */
	@TableField("type_id")
	private Integer typeId;
    /**
     * 赠送库存表主键
     */
	@TableField("gift_stock_id")
	private Integer giftStockId;
    /**
     * 获赠编号,该表主键,为空时直接购买
     */
	@TableField("parent_stock_id")
	private Integer parentStockId;
    /**
     * 0 未使用  10 已赠送(未领取)  11 已领取  20 已使用(提货)  30 已发货
     */
	private Integer status;
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


	public Integer getStockId() {
		return stockId;
	}

	public void setStockId(Integer stockId) {
		this.stockId = stockId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
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

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public Integer getGiftStockId() {
		return giftStockId;
	}

	public void setGiftStockId(Integer giftStockId) {
		this.giftStockId = giftStockId;
	}

	public Integer getParentStockId() {
		return parentStockId;
	}

	public void setParentStockId(Integer parentStockId) {
		this.parentStockId = parentStockId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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
		return "Stock{" +
			", stockId=" + stockId +
			", openId=" + openId +
			", productId=" + productId +
			", productSpecId=" + productSpecId +
			", orderId=" + orderId +
			", orderProductId=" + orderProductId +
			", typeId=" + typeId +
			", giftStockId=" + giftStockId +
			", parentStockId=" + parentStockId +
			", status=" + status +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", deleteTime=" + deleteTime +
			"}";
	}
}
