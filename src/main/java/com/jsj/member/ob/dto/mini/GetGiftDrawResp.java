package com.jsj.member.ob.dto.mini;

import com.jsj.member.ob.dto.api.gift.GiftDto;
import com.jsj.member.ob.dto.api.gift.GiftStockDto;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class GetGiftDrawResp {

    @ApiModelProperty(value = "分享信息", required = true)
    private GiftDto giftDto;

    @ApiModelProperty(value = "是否我已领取", required = true)
    private boolean isMyDraw;


    //待领取列表
    @ApiModelProperty(value = "待领取列表", required = true)
    private List<GiftStockDto> unReceiveds;

    //已领取列表
    @ApiModelProperty(value = "已领取列表", required = true)
    private List<GiftStockDto> receiveds;

    @ApiModelProperty(value = "总赠送库存", required = true)
    private List<GiftStockDto> giftStockDtos;


    public GiftDto getGiftDto() {
        return giftDto;
    }

    public void setGiftDto(GiftDto giftDto) {
        this.giftDto = giftDto;
    }

    public boolean isMyDraw() {
        return isMyDraw;
    }

    public void setMyDraw(boolean myDraw) {
        isMyDraw = myDraw;
    }

    public List<GiftStockDto> getUnReceiveds() {
        return unReceiveds;
    }

    public void setUnReceiveds(List<GiftStockDto> unReceiveds) {
        this.unReceiveds = unReceiveds;
    }

    public List<GiftStockDto> getReceiveds() {
        return receiveds;
    }

    public void setReceiveds(List<GiftStockDto> receiveds) {
        this.receiveds = receiveds;
    }

    public List<GiftStockDto> getGiftStockDtos() {
        return giftStockDtos;
    }

    public void setGiftStockDtos(List<GiftStockDto> giftStockDtos) {
        this.giftStockDtos = giftStockDtos;
    }
}

