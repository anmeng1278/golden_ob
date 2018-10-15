package com.jsj.member.ob.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *   @description : Coupon 优惠券表实体类
 *   ---------------------------------
 * 	 @author cc
 *   @since 2018-10-15
 */
@TableName("_coupon")
public class Coupon implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId(value="coupon_id", type= IdType.AUTO)
	private Integer couponId;
    /**
     * 券名称
     */
	@TableField("coupon_name")
	private String couponName;
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
     * 使用说明
     */
	private String instruction;
    /**
     * 备注
     */
	private String remarks;
    /**
     * 使用范围 1全部商品  2指定商品(值为2时范围表_coupon_product表)
     */
	@TableField("user_range")
	private Integer userRange;
    /**
     * 有效天数
     */
	@TableField("expired_days")
	private Integer expiredDays;
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


	public Integer getCouponId() {
		return couponId;
	}

	public void setCouponId(Integer couponId) {
		this.couponId = couponId;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
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

	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getUserRange() {
		return userRange;
	}

	public void setUserRange(Integer userRange) {
		this.userRange = userRange;
	}

	public Integer getExpiredDays() {
		return expiredDays;
	}

	public void setExpiredDays(Integer expiredDays) {
		this.expiredDays = expiredDays;
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
		return "Coupon{" +
			", couponId=" + couponId +
			", couponName=" + couponName +
			", amount=" + amount +
			", typeId=" + typeId +
			", instruction=" + instruction +
			", remarks=" + remarks +
			", userRange=" + userRange +
			", expiredDays=" + expiredDays +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", deleteTime=" + deleteTime +
			"}";
	}
}
