package com.jsj.member.ob.dto.api.delivery;


import com.jsj.member.ob.dto.api.product.ProductDto;
import com.jsj.member.ob.dto.api.stock.StockDto;
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
    private Integer status;
    /**
     * 配送区分，1：自提，2：配送
     */
    private Integer typeId;

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
     * 商品属性  1.实物 2.活动码 3.卡
     */
    private Integer propertyTypeId;

    /**
     * 证件号(开卡时使用)
     */
    private String idNumber;


    /**
     * 航班号
     */
    private String flightNumber;

    /**
     * 贵宾厅编号
     */
    private Integer viphallId;

    /**
     * 贵宾厅名称
     */
    private String viphallName;

    /**
     * 创建时间
     */
    private Integer createTime;

    /**
     * 配送的库存信息
     */
    private List<StockDto> stockDtos;

    //生效日期
    private String effectiveDate;

    /**
     * 配送的商品信息
     */
    private List<ProductDto> productDtos;

    public List<ProductDto> getProductDtos() {
        return productDtos;
    }

    public void setProductDtos(List<ProductDto> productDtos) {
        this.productDtos = productDtos;
    }

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

    public Long getMobile() {
        return mobile;
    }

    public void setMobile(Long mobile) {
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

    public List<StockDto> getStockDtos() {
        return stockDtos;
    }

    public void setStockDtos(List<StockDto> stockDtos) {
        this.stockDtos = stockDtos;
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

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public Integer getPropertyTypeId() {
        return propertyTypeId;
    }

    public void setPropertyTypeId(Integer propertyTypeId) {
        this.propertyTypeId = propertyTypeId;
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

    public Integer getViphallId() {
        return viphallId;
    }

    public void setViphallId(Integer viphallId) {
        this.viphallId = viphallId;
    }

    public String getViphallName() {
        return viphallName;
    }

    public void setViphallName(String viphallName) {
        this.viphallName = viphallName;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
}
