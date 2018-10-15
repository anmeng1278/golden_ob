package com.jsj.member.ob.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *   @description : TeamOrder 团见订单表实体类
 *   ---------------------------------
 * 	 @author cc
 *   @since 2018-10-15
 */
@TableName("_team_order")
public class TeamOrder implements Serializable {

    private static final long serialVersionUID = 1L;

	@TableId(value="team_order_id", type= IdType.AUTO)
	private Integer teamOrderId;
    /**
     * 团单号
     */
	@TableField("team_order_number")
	private String teamOrderNumber;
    /**
     * 组团编号
     */
	@TableField("team_id")
	private Integer teamId;
    /**
     * 微信openid
     */
	@TableField("open_id")
	private Integer openId;
    /**
     * 过期时间
     */
	@TableField("expire_time")
	private Integer expireTime;
    /**
     * 父编号,为null时openid为团长
     */
	@TableField("parent_team_order_id")
	private Integer parentTeamOrderId;
    /**
     * 组团状态 0 组团中  10 组团成功  20 组团失败   60 已取消
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


	public Integer getTeamOrderId() {
		return teamOrderId;
	}

	public void setTeamOrderId(Integer teamOrderId) {
		this.teamOrderId = teamOrderId;
	}

	public String getTeamOrderNumber() {
		return teamOrderNumber;
	}

	public void setTeamOrderNumber(String teamOrderNumber) {
		this.teamOrderNumber = teamOrderNumber;
	}

	public Integer getTeamId() {
		return teamId;
	}

	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}

	public Integer getOpenId() {
		return openId;
	}

	public void setOpenId(Integer openId) {
		this.openId = openId;
	}

	public Integer getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Integer expireTime) {
		this.expireTime = expireTime;
	}

	public Integer getParentTeamOrderId() {
		return parentTeamOrderId;
	}

	public void setParentTeamOrderId(Integer parentTeamOrderId) {
		this.parentTeamOrderId = parentTeamOrderId;
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
		return "TeamOrder{" +
			", teamOrderId=" + teamOrderId +
			", teamOrderNumber=" + teamOrderNumber +
			", teamId=" + teamId +
			", openId=" + openId +
			", expireTime=" + expireTime +
			", parentTeamOrderId=" + parentTeamOrderId +
			", status=" + status +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", deleteTime=" + deleteTime +
			"}";
	}
}
