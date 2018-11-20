package com.jsj.member.ob.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *   @description : Copywriter 实体类
 *   ---------------------------------
 * 	 @author cc
 *   @since 2018-11-20
 */
@TableName("_copywriter")
public class Copywriter implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId(value="copywriter_id", type= IdType.AUTO)
	private Integer copywriterId;
    /**
     * 内容
     */
	private String remark;
    /**
     * 是否审核
     */
	private Boolean ifpass;
    /**
     * 方案类型 1.赠送文案
     */
	@TableField("type_id")
	private Integer typeId;
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


	public Integer getCopywriterId() {
		return copywriterId;
	}

	public void setCopywriterId(Integer copywriterId) {
		this.copywriterId = copywriterId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Boolean getIfpass() {
		return ifpass;
	}

	public void setIfpass(Boolean ifpass) {
		this.ifpass = ifpass;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
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
		return "Copywriter{" +
			", copywriterId=" + copywriterId +
			", remark=" + remark +
			", ifpass=" + ifpass +
			", typeId=" + typeId +
			", beginTime=" + beginTime +
			", endTime=" + endTime +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", deleteTime=" + deleteTime +
			"}";
	}
}
