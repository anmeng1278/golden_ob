package com.jsj.member.ob.dto.mini;

import com.jsj.member.ob.dto.api.gift.GiftDto;
import com.jsj.member.ob.entity.Dict;

import java.util.List;

public class GetGiftConfirmResp {

    private List<Dict> dicts;

    private GiftDto giftDto;


    public List<Dict> getDicts() {
        return dicts;
    }

    public void setDicts(List<Dict> dicts) {
        this.dicts = dicts;
    }

    public GiftDto getGiftDto() {
        return giftDto;
    }

    public void setGiftDto(GiftDto giftDto) {
        this.giftDto = giftDto;
    }
}

