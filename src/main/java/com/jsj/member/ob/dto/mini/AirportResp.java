package com.jsj.member.ob.dto.mini;

import com.jsj.member.ob.dto.api.airport.JsAirportDto;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class AirportResp {

    //机场贵宾厅
    @ApiModelProperty(value = "机场贵宾厅", required = true)
    private List<JsAirportDto> airports;

    //火车高铁站
    @ApiModelProperty(value = "火车高铁站", required = true)
    private List<JsAirportDto> trains;


    public List<JsAirportDto> getAirports() {
        return airports;
    }

    public void setAirports(List<JsAirportDto> airports) {
        this.airports = airports;
    }

    public List<JsAirportDto> getTrains() {
        return trains;
    }

    public void setTrains(List<JsAirportDto> trains) {
        this.trains = trains;
    }
}
