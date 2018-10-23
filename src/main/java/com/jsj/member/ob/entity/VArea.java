package com.jsj.member.ob.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *   @description : VArea VIEW实体类
 *   ---------------------------------
 * 	 @author cc
 *   @since 2018-10-23
 */
@TableName("_v_area")
public class VArea implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableField("dict_id")
	private Integer dictId;
	@TableField("province_id")
	private Long provinceId;
	@TableField("city_id")
	private Long cityId;
	@TableField("area_id")
	private Long areaId;
    /**
     * 名称
     */
	@TableField("dict_name")
	private String dictName;


	public Integer getDictId() {
		return dictId;
	}

	public void setDictId(Integer dictId) {
		this.dictId = dictId;
	}

	public Long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public String getDictName() {
		return dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	}


	@Override
	public String toString() {
		return "VArea{" +
			", dictId=" + dictId +
			", provinceId=" + provinceId +
			", cityId=" + cityId +
			", areaId=" + areaId +
			", dictName=" + dictName +
			"}";
	}
}
