package com.jsj.member.ob.dto.api.gift;

import com.jsj.member.ob.dto.BaseRequ;
import com.jsj.member.ob.enums.GiftShareType;

import java.util.ArrayList;
import java.util.List;

public class CreateGiftRequ {

    public CreateGiftRequ() {
        this.baseRequ = new BaseRequ();
        this.giftProductDtos = new ArrayList<>();
    }

    private BaseRequ baseRequ;

    public BaseRequ getBaseRequ() {
        return baseRequ;
    }

    public void setBaseRequ(BaseRequ baseRequ) {
        this.baseRequ = baseRequ;
    }

    /**
     * 赠送商品
     */
    private List<GiftProductDto> giftProductDtos;

    public List<GiftProductDto> getGiftProductDtos() {
        return giftProductDtos;
    }

    public void setGiftProductDtos(List<GiftProductDto> giftProductDtos) {
        this.giftProductDtos = giftProductDtos;
    }

    /**
     * 寄语
     */
    private String blessings;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 赠送类型
     */
    private GiftShareType giftShareType;

    public String getBlessings() {
        return blessings;
    }

    public void setBlessings(String blessings) {
        this.blessings = blessings;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public GiftShareType getGiftShareType() {
        return giftShareType;
    }

    public void setGiftShareType(GiftShareType giftShareType) {
        this.giftShareType = giftShareType;
    }
}
