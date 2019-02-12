package com.jsj.member.ob.dto.mini;

import com.jsj.member.ob.dto.api.airport.JsAirportDto;
import com.jsj.member.ob.dto.api.stock.StockDto;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class ReadyDeliveryResp {


    //支持自提
    @ApiModelProperty(value = "是否支持自提", required = true)
    private boolean unSupportPickup;

    //机场贵宾厅
    @ApiModelProperty(value = "机场贵宾厅", required = true)
    private List<JsAirportDto> airports;

    //火车高铁站
    @ApiModelProperty(value = "火车高铁站", required = true)
    private List<JsAirportDto> trains;

    @ApiModelProperty(value = "使用库存信息，仅展示", required = true)
    private  List<StockDto> stockDtos;

    @ApiModelProperty(value = "卡号", required = true)
    private String cardId;

    @ApiModelProperty(value = "卡类型", required = true)
    private String cardTypeIdName;

    @ApiModelProperty(value = "手机号", required = true)
    private String mobile;

    @ApiModelProperty(value = "卡有效期", required = true)
    private String cardInvalidDate;

    @ApiModelProperty(value = "会员名称", required = true)
    private String customerName;


    public boolean isUnSupportPickup() {
        return unSupportPickup;
    }

    public void setUnSupportPickup(boolean unSupportPickup) {
        this.unSupportPickup = unSupportPickup;
    }

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

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardTypeIdName() {
        return cardTypeIdName;
    }

    public void setCardTypeIdName(String cardTypeIdName) {
        this.cardTypeIdName = cardTypeIdName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCardInvalidDate() {
        return cardInvalidDate;
    }

    public void setCardInvalidDate(String cardInvalidDate) {
        this.cardInvalidDate = cardInvalidDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<StockDto> getStockDtos() {
        return stockDtos;
    }

    public void setStockDtos(List<StockDto> stockDtos) {
        this.stockDtos = stockDtos;
    }
}
