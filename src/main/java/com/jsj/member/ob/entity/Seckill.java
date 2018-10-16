package com.jsj.member.ob.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

/**
 *   @description : Seckill 秒杀表实体类
 *   ---------------------------------
 * 	 @author cc
 *   @since 2018-10-16
 */
@TableName("_seckill")
public class Seckill implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId(value="seckill_id", type= IdType.AUTO)
	private Integer seckillId;
    /**
     * 商品编号
     */
	@TableField("product_id")
	private Integer productId;
    /**
     * 开始时间
     */
	@TableField("begin_time")
	private Integer beginTime;
    /**
     * 结束时间
     */
	@TableField("end_time")
	private Integer endTime;
    /**
     * 秒杀价
     */
	@TableField("seckill_price")
	private Double seckillPrice;
    /**
     * 库存
     */
	@TableField("stock_count")
	private Integer stockCount;
    /**
     * 是否审核
     */
	private Boolean ifpass;
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


	public Integer getSeckillId() {
		return seckillId;
	}

	public void setSeckillId(Integer seckillId) {
		this.seckillId = seckillId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Integer beginTime) {
		this.beginTime = beginTime;
	}

	public Integer getEndTime() {
		return endTime;
	}

	public void setEndTime(Integer endTime) {
		this.endTime = endTime;
	}

	public Double getSeckillPrice() {
		return seckillPrice;
	}

	public void setSeckillPrice(Double seckillPrice) {
		this.seckillPrice = seckillPrice;
	}

	public Integer getStockCount() {
		return stockCount;
	}

	public void setStockCount(Integer stockCount) {
		this.stockCount = stockCount;
	}

	public Boolean getIfpass() {
		return ifpass;
	}

	public void setIfpass(Boolean ifpass) {
		this.ifpass = ifpass;
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
		return "Seckill{" +
			", seckillId=" + seckillId +
			", productId=" + productId +
			", beginTime=" + beginTime +
			", endTime=" + endTime +
			", seckillPrice=" + seckillPrice +
			", stockCount=" + stockCount +
			", ifpass=" + ifpass +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", deleteTime=" + deleteTime +
			"}";
	}
}
