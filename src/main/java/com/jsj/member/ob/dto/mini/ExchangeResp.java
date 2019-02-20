package com.jsj.member.ob.dto.mini;

import com.jsj.member.ob.dto.api.activity.ActivityDto;
import com.jsj.member.ob.dto.api.activity.ActivityProductDto;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class ExchangeResp {

    @ApiModelProperty(value = "兑换专区商品")
    private List<ActivityProductDto> exchangeProducts;

    @ApiModelProperty(value = "兑换专区活动")
    private ActivityDto exchange;

    public List<ActivityProductDto> getExchangeProducts() {
        return exchangeProducts;
    }

    public void setExchangeProducts(List<ActivityProductDto> exchangeProducts) {
        this.exchangeProducts = exchangeProducts;
    }

    public ActivityDto getExchange() {
        return exchange;
    }

    public void setExchange(ActivityDto exchange) {
        this.exchange = exchange;
    }

}
