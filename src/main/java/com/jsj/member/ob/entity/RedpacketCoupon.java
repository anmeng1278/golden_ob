package com.jsj.member.ob.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *   @description : RedpacketCoupon 礼包代金券表实体类
 *   ---------------------------------
 * 	 @author cc
 *   @since 2018-11-26
 */
@TableName("_redpacket_coupon")
public class RedpacketCoupon implements Serializable {

    private static final long serialVersionUID = 1L;

	@TableId(value="redpacket_coupon_id", type= IdType.AUTO)
	private Integer redpacketCouponId;
    /**
     * 代金券表主键
     */
	@TableField("coupon_id")
	private Integer couponId;
    /**
     * 礼包表主键
     */
	@TableField("redpacket_id")
	private Integer redpacketId;
    /**
     * 数量 ,最小1个
     */
	private Integer number;
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

	public Integer getRedpacketId() {
		return redpacketId;
	}

	public void setRedpacketId(Integer redpacketId) {
		this.redpacketId = redpacketId;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
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
		return "RedpacketCoupon{" +
			", redpacketCouponId=" + redpacketCouponId +
			", couponId=" + couponId +
			", redpacketId=" + redpacketId +
			", number=" + number +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", deleteTime=" + deleteTime +
			"}";
	}
}
