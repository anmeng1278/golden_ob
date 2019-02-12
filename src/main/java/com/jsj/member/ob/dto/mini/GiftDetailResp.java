package com.jsj.member.ob.dto.mini;

import com.jsj.member.ob.dto.api.gift.GiftDto;
import com.jsj.member.ob.dto.api.stock.StockDto;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class GiftDetailResp {


    @ApiModelProperty(value = "分享图片", required = true)
    private String imgUrl;

    @ApiModelProperty(value = "赠送的库存", required = true)
    private List<StockDto> giveStocks;

    @ApiModelProperty(value = "领取的库存", required = true)
    private List<StockDto> receiveStocks;

    @ApiModelProperty(value = "赠送详情", required = true)
    private GiftDto giftDto;


    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public List<StockDto> getGiveStocks() {
        return giveStocks;
    }

    public void setGiveStocks(List<StockDto> giveStocks) {
        this.giveStocks = giveStocks;
    }

    public List<StockDto> getReceiveStocks() {
        return receiveStocks;
    }

    public void setReceiveStocks(List<StockDto> receiveStocks) {
        this.receiveStocks = receiveStocks;
    }

    public GiftDto getGiftDto() {
        return giftDto;
    }

    public void setGiftDto(GiftDto giftDto) {
        this.giftDto = giftDto;
    }
}
