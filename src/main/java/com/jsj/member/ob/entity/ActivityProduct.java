package com.jsj.member.ob.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *   @description : ActivityProduct 活动商品表实体类
 *   ---------------------------------
 * 	 @author cc
 *   @since 2019-02-11
 */
@TableName("_activity_product")
public class ActivityProduct implements Serializable {

    private static final long serialVersionUID = 1L;

	@TableId(value="activity_product_id", type= IdType.AUTO)
	private Integer activityProductId;
	@TableField("activity_id")
	private Integer activityId;
	@TableField("product_id")
	private Integer productId;
	@TableField("product_spec_id")
	private Integer productSpecId;
	private Integer sort;
    /**
     * 售价(秒杀时使用)
     */
	@TableField("sale_price")
	private Double salePrice;
    /**
     * 库存,秒杀时使用
     */
	@TableField("stock_count")
	private Integer stockCount;
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


	public Integer getActivityProductId() {
		return activityProductId;
	}

	public void setActivityProductId(Integer activityProductId) {
		this.activityProductId = activityProductId;
	}

	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
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

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}

	public Integer getStockCount() {
		return stockCount;
	}

	public void setStockCount(Integer stockCount) {
		this.stockCount = stockCount;
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
		return "ActivityProduct{" +
			", activityProductId=" + activityProductId +
			", activityId=" + activityId +
			", productId=" + productId +
			", productSpecId=" + productSpecId +
			", sort=" + sort +
			", salePrice=" + salePrice +
			", stockCount=" + stockCount +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", deleteTime=" + deleteTime +
			"}";
	}
}
