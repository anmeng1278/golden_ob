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

    @ApiModelProperty(value = "会员帐户余额")
    private double balance;

    @ApiModelProperty(value = "购物车数")
    private int cartCount;

    @ApiModelProperty(value = "用户未支付订单数")
    private int unPayCount;

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getCartCount() {
        return cartCount;
    }

    public void setCartCount(int cartCount) {
        this.cartCount = cartCount;
    }

    public int getUnPayCount() {
        return unPayCount;
    }

    public void setUnPayCount(int unPayCount) {
        this.unPayCount = unPayCount;
    }
}
