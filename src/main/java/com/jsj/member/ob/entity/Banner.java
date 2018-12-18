package com.jsj.member.ob.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

/**
 *   @description : Banner 实体类
 *   ---------------------------------
 * 	 @author cc
 *   @since 2018-12-18
 */
@TableName("_banner")
public class Banner implements Serializable {

    private static final long serialVersionUID = 1L;

	@TableId(value="banner_id", type= IdType.AUTO)
	private Integer bannerId;
    /**
     * 标题
     */
	private String title;
    /**
     * 所属模块编号 1.首页轮播
     */
	@TableField("type_id")
	private Integer typeId;
    /**
     * 是否审核
     */
	private Boolean ifpass;
    /**
     * 图片路径
     */
	@TableField("banner_path")
	private String bannerPath;
    /**
     * 触发链接
     */
	@TableField("navigate_url")
	private String navigateUrl;
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


	public Integer getBannerId() {
		return bannerId;
	}

	public void setBannerId(Integer bannerId) {
		this.bannerId = bannerId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getBannerPath() {
		return bannerPath;
	}

	public void setBannerPath(String bannerPath) {
		this.bannerPath = bannerPath;
	}

	public String getNavigateUrl() {
		return navigateUrl;
	}

	public void setNavigateUrl(String navigateUrl) {
		this.navigateUrl = navigateUrl;
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
		return "Banner{" +
			", bannerId=" + bannerId +
			", title=" + title +
			", typeId=" + typeId +
			", ifpass=" + ifpass +
			", bannerPath=" + bannerPath +
			", navigateUrl=" + navigateUrl +
			", sort=" + sort +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", deleteTime=" + deleteTime +
			"}";
	}
}
