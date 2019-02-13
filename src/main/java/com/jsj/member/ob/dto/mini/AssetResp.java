package com.jsj.member.ob.dto.mini;

import io.swagger.annotations.ApiModelProperty;

public class AssetResp {

    @ApiModelProperty(value = "礼品券余额")
    private double giftBalance;


    public double getGiftBalance() {
        return giftBalance;
    }

    public void setGiftBalance(double giftBalance) {
        this.giftBalance = giftBalance;
    }
}
