package com.jsj.member.ob.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *   @description : Airport 休息厅实体类
 *   ---------------------------------
 * 	 @author cc
 *   @since 2018-12-21
 */
@TableName("_airport")
public class Airport implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId("airport_id")
	private Integer airportId;
    /**
     * 名称
     */
	@TableField("airport_name")
	private String airportName;
    /**
     * 拼音缩写
     */
	@TableField("airport_code")
	private String airportCode;
	private Boolean ifhot;
    /**
     * 首字母
     */
	private String initials;
    /**
     * 状态，0：默认，1：机场，2，高铁
     */
	@TableField("type_id")
	private Integer typeId;
    /**
     * 排序
     */
	private Integer sort;
    /**
     * 城市编码
     */
	@TableField("city_id")
	private Integer cityId;
    /**
     * 城市
     */
	@TableField("city_name")
	private String cityName;
    /**
     * 备注
     */
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


	public Integer getAirportId() {
		return airportId;
	}

	public void setAirportId(Integer airportId) {
		this.airportId = airportId;
	}

	public String getAirportName() {
		return airportName;
	}

	public void setAirportName(String airportName) {
		this.airportName = airportName;
	}

	public String getAirportCode() {
		return airportCode;
	}

	public void setAirportCode(String airportCode) {
		this.airportCode = airportCode;
	}

	public Boolean getIfhot() {
		return ifhot;
	}

	public void setIfhot(Boolean ifhot) {
		this.ifhot = ifhot;
	}

	public String getInitials() {
		return initials;
	}

	public void setInitials(String initials) {
		this.initials = initials;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
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
		return "Airport{" +
			", airportId=" + airportId +
			", airportName=" + airportName +
			", airportCode=" + airportCode +
			", ifhot=" + ifhot +
			", initials=" + initials +
			", typeId=" + typeId +
			", sort=" + sort +
			", cityId=" + cityId +
			", cityName=" + cityName +
			", remarks=" + remarks +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", deleteTime=" + deleteTime +
			"}";
	}
}
