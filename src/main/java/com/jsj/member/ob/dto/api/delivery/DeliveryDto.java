package com.jsj.member.ob.dto.api.delivery;


import com.jsj.member.ob.dto.api.product.ProductDto;
import com.jsj.member.ob.dto.api.stock.StockDto;
import com.jsj.member.ob.enums.DeliveryStatus;
import com.jsj.member.ob.enums.DeliveryType;
import com.jsj.member.ob.enums.PropertyType;

import java.util.List;

public class DeliveryDto {

    /**
     * 主键
     */
    private Integer deliveryId;
    /**
     * 公众号open_id
     */
    private String openId;
    /**
     * 快递号
     */
    private String expressNumber;
    /**
     * 状态 0未发货 10已发货 20已签收
     */
    private DeliveryStatus deliveryStatus;
    /**
     * 配送区分，1：自提，2：配送
     */
    private DeliveryType deliveryType;

    /**
     * 商品属性
     */
    private PropertyType propertyType;

    /**
     * 联系人
     */
    private String contactName;
    /**
     * 手机号
     */
    private Long mobile;
    /**
     * 省
     */
    private Integer provinceId;
    /**
     * 市
     */
    private Integer cityId;
    /**
     * 区
     */
    private Integer districtId;
    /**
     * 详细地址
     */
    private String address;
    /**
     * 备注
     */
    private String remarks;


    /**
     * 证件号(开卡时使用)
     */
    private String idNumber;

    /**
     * 证件类型
     */
    private Integer idTypeId;


    /**
     * 航班号
     */
    private String flightNumber;

    /**
     * 机场三字码
     */
    private String airportCode;

    /**
     * 机场名称
     */
    private String airportName;

    /**
     * 创建时间
     */
    private Integer createTime;

    /**
     * 修改时间
     */
    private Integer updateTime;

    /**
     * 配送的库存信息
     */
    private List<StockDto> stockDtos;

    //生效日期
    private int effectiveDate;

    /**
     * 配送的商品信息
     */
    private List<ProductDto> productDtos;


    /**
     * 删除时间
     */
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


    public DeliveryType getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(DeliveryType deliveryType) {
        this.deliveryType = deliveryType;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Long getMobile() {
        return mobile;
    }

    public void setMobile(Long mobile) {
        this.mobile = mobile;
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

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getAirportCode() {
        return airportCode;
    }

    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }

    public String getAirportName() {
        return airportName;
    }

    public void setAirportName(String airportName) {
        this.airportName = airportName;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public List<StockDto> getStockDtos() {
        return stockDtos;
    }

    public void setStockDtos(List<StockDto> stockDtos) {
        this.stockDtos = stockDtos;
    }

    public int getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(int effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public List<ProductDto> getProductDtos() {
        return productDtos;
    }

    public void setProductDtos(List<ProductDto> productDtos) {
        this.productDtos = productDtos;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getIdTypeId() {
        return idTypeId;
    }

    public void setIdTypeId(Integer idTypeId) {
        this.idTypeId = idTypeId;
    }

    public Integer getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Integer deleteTime) {
        this.deleteTime = deleteTime;
    }
}
