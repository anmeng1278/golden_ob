package com.jsj.member.ob.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *   @description : Redpacket 实体类
 *   ---------------------------------
 * 	 @author cc
 *   @since 2018-10-30
 */
@TableName("_redpacket")
public class Redpacket implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("redpacket_id")
	private Integer redpacketId;
	@TableField("redpacket_name")
	private String redpacketName;
	private Boolean ifpass;
	private Integer sort;
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


	public Integer getRedpacketId() {
		return redpacketId;
	}

	public void setRedpacketId(Integer redpacketId) {
		this.redpacketId = redpacketId;
	}

	public String getRedpacketName() {
		return redpacketName;
	}

	public void setRedpacketName(String redpacketName) {
		this.redpacketName = redpacketName;
	}

	public Boolean getIfpass() {
		return ifpass;
	}

	public void setIfpass(Boolean ifpass) {
		this.ifpass = ifpass;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
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
		return "Redpacket{" +
			", redpacketId=" + redpacketId +
			", redpacketName=" + redpacketName +
			", ifpass=" + ifpass +
			", sort=" + sort +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", deleteTime=" + deleteTime +
			"}";
	}
}
