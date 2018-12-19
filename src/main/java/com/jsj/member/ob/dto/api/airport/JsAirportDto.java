package com.jsj.member.ob.dto.api.airport;

import java.util.List;

public class JsAirportDto {

    /**
     * 首字母
     */
    private String initials;

    /**
     * 贵宾厅列表
     */
    private List<AirportDto> airportDtos;

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public List<AirportDto> getAirportDtos() {
        return airportDtos;
    }

    public void setAirportDtos(List<AirportDto> airportDtos) {
        this.airportDtos = airportDtos;
    }
}
