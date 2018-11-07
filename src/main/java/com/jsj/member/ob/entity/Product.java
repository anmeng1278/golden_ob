package com.jsj.member.ob.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *   @description : Product 商品表实体类
 *   ---------------------------------
 * 	 @author cc
 *   @since 2018-11-07
 */
@TableName("_product")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId(value="product_id", type= IdType.AUTO)
	private Integer productId;
    /**
     * 商品名称
     */
	@TableField("product_name")
	private String productName;
    /**
     * 商品分类,字典表主键
     */
	@TableField("type_id")
	private Integer typeId;
    /**
     * 商品属性  实物 次卡 月卡 组合商品
     */
	@TableField("property_type_id")
	private Integer propertyTypeId;
    /**
     * 简介
     */
	private String introduce;
    /**
     * 使用说明
     */
	@TableField("use_intro")
	private String useIntro;
    /**
     * 单位
     */
	private String unit;
    /**
     * 备注
     */
	private String remarks;
    /**
     * 赠送文案
     */
	@TableField("gift_copywriting")
	private String giftCopywriting;
    /**
     * 是否支持自提，0：不支持，1：支持
     */
	private Boolean ifpickup;
    /**
     * 是否审核
     */
	private Boolean ifpass;
    /**
     * 是否支持配送
     */
	private Boolean ifdistribution;
	private Boolean iftop;
    /**
     * 操作人编号
     */
	@TableField("opemployee_id")
	private Integer opemployeeId;
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


	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public Integer getPropertyTypeId() {
		return propertyTypeId;
	}

	public void setPropertyTypeId(Integer propertyTypeId) {
		this.propertyTypeId = propertyTypeId;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getUseIntro() {
		return useIntro;
	}

	public void setUseIntro(String useIntro) {
		this.useIntro = useIntro;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getGiftCopywriting() {
		return giftCopywriting;
	}

	public void setGiftCopywriting(String giftCopywriting) {
		this.giftCopywriting = giftCopywriting;
	}

	public Boolean getIfpickup() {
		return ifpickup;
	}

	public void setIfpickup(Boolean ifpickup) {
		this.ifpickup = ifpickup;
	}

	public Boolean getIfpass() {
		return ifpass;
	}

	public void setIfpass(Boolean ifpass) {
		this.ifpass = ifpass;
	}

	public Boolean getIfdistribution() {
		return ifdistribution;
	}

	public void setIfdistribution(Boolean ifdistribution) {
		this.ifdistribution = ifdistribution;
	}

	public Boolean getIftop() {
		return iftop;
	}

	public void setIftop(Boolean iftop) {
		this.iftop = iftop;
	}

	public Integer getOpemployeeId() {
		return opemployeeId;
	}

	public void setOpemployeeId(Integer opemployeeId) {
		this.opemployeeId = opemployeeId;
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
		return "Product{" +
			", productId=" + productId +
			", productName=" + productName +
			", typeId=" + typeId +
			", propertyTypeId=" + propertyTypeId +
			", introduce=" + introduce +
			", useIntro=" + useIntro +
			", unit=" + unit +
			", remarks=" + remarks +
			", giftCopywriting=" + giftCopywriting +
			", ifpickup=" + ifpickup +
			", ifpass=" + ifpass +
			", ifdistribution=" + ifdistribution +
			", iftop=" + iftop +
			", opemployeeId=" + opemployeeId +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", deleteTime=" + deleteTime +
			"}";
	}
}
