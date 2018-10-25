package com.jsj.member.ob.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *   @description : Gift 赠送表实体类
 *   ---------------------------------
 * 	 @author cc
 *   @since 2018-10-25
 */
@TableName("_gift")
public class Gift implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId(value="gift_id", type= IdType.AUTO)
	private Integer giftId;
    /**
     * 公众号open_id
     */
	@TableField("open_id")
	private String openId;
    /**
     * 祝福语
     */
	private String blessings;
    /**
     * 赠送状态，0：未分享，10：已分享  60：取消赠送
     */
	private Integer status;
    /**
     * 备注
     */
	private String remarks;
    /**
     * 赠送区分，0:未赠送(此状态下不允许领取) 1：赠送好友，2：群赠送
     */
	@TableField("share_type")
	private Integer shareType;
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


	public Integer getGiftId() {
		return giftId;
	}

	public void setGiftId(Integer giftId) {
		this.giftId = giftId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getBlessings() {
		return blessings;
	}

	public void setBlessings(String blessings) {
		this.blessings = blessings;
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

	public Integer getShareType() {
		return shareType;
	}

	public void setShareType(Integer shareType) {
		this.shareType = shareType;
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
		return "Gift{" +
			", giftId=" + giftId +
			", openId=" + openId +
			", blessings=" + blessings +
			", status=" + status +
			", remarks=" + remarks +
			", shareType=" + shareType +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", deleteTime=" + deleteTime +
			"}";
	}
}
