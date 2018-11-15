package com.jsj.member.ob.dto.api.delivery;


import com.jsj.member.ob.dto.api.stock.StockDto;

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
     * 联系人
     */
    private String contactName;
    /**
     * 手机号
     */
    private Integer mobile;
    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 区
     */
    private String district;
    /**
     * 详细地址
     */
    private String address;
    /**
     * 备注
     */
    private String remarks;

    private List<StockDto> stockDto;

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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
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

    public List<StockDto> getStockDto() {
        return stockDto;
    }

    public void setStockDto(List<StockDto> stockDto) {
        this.stockDto = stockDto;
    }
}
