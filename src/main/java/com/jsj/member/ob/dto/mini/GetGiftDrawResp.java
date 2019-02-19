package com.jsj.member.ob.dto.mini;

import com.jsj.member.ob.dto.api.gift.GiftDto;
import com.jsj.member.ob.dto.api.gift.GiftStockDto;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class GetGiftDrawResp {

    @ApiModelProperty(value = "分享信息", required = true)
    private GiftDto giftDto;

    @ApiModelProperty(value = "本人领取数量", required = true)
    private long myDrawCount;

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

    public long getMyDrawCount() {
        return myDrawCount;
    }

    public void setMyDrawCount(long myDrawCount) {
        this.myDrawCount = myDrawCount;
    }
}

