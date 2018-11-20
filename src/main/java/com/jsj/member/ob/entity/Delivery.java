package com.jsj.member.ob.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *   @description : Delivery 提货表实体类
 *   ---------------------------------
 * 	 @author cc
 *   @since 2018-11-20
 */
@TableName("_delivery")
public class Delivery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId(value="delivery_id", type= IdType.AUTO)
	private Integer deliveryId;
    /**
     * 公众号open_id
     */
	@TableField("open_id")
	private String openId;
    /**
     * 快递号
     */
	@TableField("express_number")
	private String expressNumber;
    /**
     * 状态 0未发货 10已发货 20已签收
     */
	private Integer status;
    /**
     * 配送区分，1：自提，2：配送
     */
	@TableField("type_id")
	private Integer typeId;
    /**
     * 联系人
     */
	@TableField("contact_name")
	private String contactName;
    /**
     * 手机号
     */
	private Integer mobile;
    /**
     * 详细地址
     */
	private String address;
    /**
     * 备注
     */
	private String remarks;
    /**
     * 省
     */
	@TableField("province_id")
	private Integer provinceId;
    /**
     * 市
     */
	@TableField("city_id")
	private Integer cityId;
    /**
     * 区
     */
	@TableField("district_id")
	private Integer districtId;
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


	public Integer getDeliveryId() {
		return deliveryId;
	}

	public void setDeliveryId(Integer deliveryId) {
		this.deliveryId = deliveryId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getExpressNumber() {
		return expressNumber;
	}

	public void setExpressNumber(String expressNumber) {
		this.expressNumber = expressNumber;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public Integer getMobile() {
		return mobile;
	}

	public void setMobile(Integer mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public Integer getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Integer districtId) {
		this.districtId = districtId;
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
		return "Delivery{" +
			", deliveryId=" + deliveryId +
			", openId=" + openId +
			", expressNumber=" + expressNumber +
			", status=" + status +
			", typeId=" + typeId +
			", contactName=" + contactName +
			", mobile=" + mobile +
			", address=" + address +
			", remarks=" + remarks +
			", provinceId=" + provinceId +
			", cityId=" + cityId +
			", districtId=" + districtId +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", deleteTime=" + deleteTime +
			"}";
	}
}
