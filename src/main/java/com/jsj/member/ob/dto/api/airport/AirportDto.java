package com.jsj.member.ob.dto.api.airport;

import com.jsj.member.ob.enums.AirportType;

public class AirportDto {

    /**
     * id
     */
    private Integer airportId;
    /**
     * 名称
     */
    private String airportName;
    /**
     * 拼音缩写
     */
    private String airportCode;

    private Boolean ifhot;
    /**
     * 首字母
     */
    private String initials;
    /**
     * 状态，0：默认，1：机场，2，高铁
     */
    private AirportType airportType;
    /**
     * 城市编码
     */
    private Integer cityId;
    /**
     * 城市
     */
    private String cityName;


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

    public AirportType getAirportType() {
        return airportType;
    }

    public void setAirportType(AirportType airportType) {
        this.airportType = airportType;
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


}
