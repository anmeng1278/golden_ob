package com.jsj.member.ob.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *   @description : WechatRelation 实体类
 *   ---------------------------------
 * 	 @author cc
 *   @since 2019-02-11
 */
@TableName("_wechat_relation")
public class WechatRelation implements Serializable {

    private static final long serialVersionUID = 1L;

	@TableId(value="relation_id", type= IdType.AUTO)
	private Integer relationId;
	@TableField("open_id")
	private String openId;
	@TableField("relation_open_id")
	private String relationOpenId;
    /**
     * 1 空铁小程序 2 金色世纪小程序 3金色世纪公众号 4.金色严选
     */
	@TableField("type_id")
	private Integer typeId;
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


	public Integer getRelationId() {
		return relationId;
	}

	public void setRelationId(Integer relationId) {
		this.relationId = relationId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getRelationOpenId() {
		return relationOpenId;
	}

	public void setRelationOpenId(String relationOpenId) {
		this.relationOpenId = relationOpenId;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
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
		return "WechatRelation{" +
			", relationId=" + relationId +
			", openId=" + openId +
			", relationOpenId=" + relationOpenId +
			", typeId=" + typeId +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", deleteTime=" + deleteTime +
			"}";
	}
}
