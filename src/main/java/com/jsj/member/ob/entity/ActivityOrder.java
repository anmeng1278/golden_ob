package com.jsj.member.ob.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *   @description : ActivityOrder 活动订单表,暂时团单使用实体类
 *   ---------------------------------
 * 	 @author cc
 *   @since 2018-11-20
 */
@TableName("_activity_order")
public class ActivityOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId(value="activity_order_id", type= IdType.AUTO)
	private Integer activityOrderId;
    /**
     * 活动编号
     */
	@TableField("activity_id")
	private Integer activityId;
    /**
     * 微信openid
     */
	@TableField("open_id")
	private String openId;
    /**
     * 过期时间
     */
	@TableField("expire_time")
	private Integer expireTime;
    /**
     * 父编号,为null时openid为团长
     */
	@TableField("parent_activity_order_id")
	private Integer parentActivityOrderId;
    /**
     * 组团状态 0 组团中  10 组团成功  20 组团失败 30 创建订单成功   60 已取消
     */
	private Integer status;
	private String remarks;
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


	public Integer getActivityOrderId() {
		return activityOrderId;
	}

	public void setActivityOrderId(Integer activityOrderId) {
		this.activityOrderId = activityOrderId;
	}

	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public Integer getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Integer expireTime) {
		this.expireTime = expireTime;
	}

	public Integer getParentActivityOrderId() {
		return parentActivityOrderId;
	}

	public void setParentActivityOrderId(Integer parentActivityOrderId) {
		this.parentActivityOrderId = parentActivityOrderId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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
		return "ActivityOrder{" +
			", activityOrderId=" + activityOrderId +
			", activityId=" + activityId +
			", openId=" + openId +
			", expireTime=" + expireTime +
			", parentActivityOrderId=" + parentActivityOrderId +
			", status=" + status +
			", remarks=" + remarks +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", deleteTime=" + deleteTime +
			"}";
	}
}
