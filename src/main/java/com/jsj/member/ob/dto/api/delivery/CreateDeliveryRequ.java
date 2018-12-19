package com.jsj.member.ob.dto.api.delivery;

import com.jsj.member.ob.dto.BaseRequ;
import com.jsj.member.ob.dto.api.stock.UseProductDto;
import com.jsj.member.ob.enums.DeliveryType;
import com.jsj.member.ob.enums.PropertyType;

import java.util.ArrayList;
import java.util.List;

public class CreateDeliveryRequ {

    public CreateDeliveryRequ() {
        this.baseRequ = new BaseRequ();
        this.useProductDtos = new ArrayList<>();
    }

    private BaseRequ baseRequ;

    public BaseRequ getBaseRequ() {
        return baseRequ;
    }

    public void setBaseRequ(BaseRequ baseRequ) {
        this.baseRequ = baseRequ;
    }

    private List<UseProductDto> useProductDtos;

    private DeliveryType deliveryType;

    private PropertyType propertyType;

    private String contactName;

    private String mobile;

    private String idNumber;

    private String flightNumber;

    private String airportCode;

    private String airportName;

    private String address;

    private String remarks;

    private int provinceId;

    private int cityId;

    private int districtId;

    //自提时间、生效日期
    private String effectiveDate;

    public List<UseProductDto> getUseProductDtos() {
        return useProductDtos;
    }

    public void setUseProductDtos(List<UseProductDto> useProductDtos) {
        this.useProductDtos = useProductDtos;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
}
