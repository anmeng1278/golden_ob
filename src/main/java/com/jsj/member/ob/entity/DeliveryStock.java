package com.jsj.member.ob.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *   @description : DeliveryStock 提货库存表实体类
 *   ---------------------------------
 * 	 @author cc
 *   @since 2018-10-25
 */
@TableName("_delivery_stock")
public class DeliveryStock implements Serializable {

    private static final long serialVersionUID = 1L;

	@TableId(value="delivery_stock_id", type= IdType.AUTO)
	private Integer deliveryStockId;
    /**
     * 提取表主键
     */
	@TableField("delivery_id")
	private Integer deliveryId;
    /**
     * 用户库存表主键
     */
	@TableField("stock_id")
	private Integer stockId;
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


	public Integer getDeliveryStockId() {
		return deliveryStockId;
	}

	public void setDeliveryStockId(Integer deliveryStockId) {
		this.deliveryStockId = deliveryStockId;
	}

	public Integer getDeliveryId() {
		return deliveryId;
	}

	public void setDeliveryId(Integer deliveryId) {
		this.deliveryId = deliveryId;
	}

	public Integer getStockId() {
		return stockId;
	}

	public void setStockId(Integer stockId) {
		this.stockId = stockId;
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
		return "DeliveryStock{" +
			", deliveryStockId=" + deliveryStockId +
			", deliveryId=" + deliveryId +
			", stockId=" + stockId +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", deleteTime=" + deleteTime +
			"}";
	}
}
