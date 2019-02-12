package com.jsj.member.ob.dto.mini;

import com.jsj.member.ob.dto.api.gift.UserDrawDto;
import com.jsj.member.ob.dto.api.gift.UserGiftDto;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class GiftsResp {

    //用户所有赠送的
    @ApiModelProperty(value = "赠送列表", required = true)
    private List<UserGiftDto> giftDtos;

    //用户所有领取的
    @ApiModelProperty(value = "领取列表", required = true)
    private List<UserDrawDto> drawDtos;

    public List<UserGiftDto> getGiftDtos() {
        return giftDtos;
    }

    public void setGiftDtos(List<UserGiftDto> giftDtos) {
        this.giftDtos = giftDtos;
    }

    public List<UserDrawDto> getDrawDtos() {
        return drawDtos;
    }

    public void setDrawDtos(List<UserDrawDto> drawDtos) {
        this.drawDtos = drawDtos;
    }
}
