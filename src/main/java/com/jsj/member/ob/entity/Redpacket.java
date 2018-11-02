package com.jsj.member.ob.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

/**
 *   @description : Redpacket 礼包表实体类
 *   ---------------------------------
 * 	 @author cc
 *   @since 2018-11-02
 */
@TableName("_redpacket")
public class Redpacket implements Serializable {

    private static final long serialVersionUID = 1L;

	@TableId(value="redpacket_id", type= IdType.AUTO)
	private Integer redpacketId;
    /**
     * 礼包名称
     */
	@TableField("redpacket_name")
	private String redpacketName;
    /**
     * 礼包类型 1.券类红包
     */
	@TableField("type_id")
	private Integer typeId;
    /**
     * 是否审核
     */
	private Boolean ifpass;
    /**
     * 排序
     */
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

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
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
			", typeId=" + typeId +
			", ifpass=" + ifpass +
			", sort=" + sort +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", deleteTime=" + deleteTime +
			"}";
	}
}
