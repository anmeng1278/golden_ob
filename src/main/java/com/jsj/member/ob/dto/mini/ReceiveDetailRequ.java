package com.jsj.member.ob.dto.mini;

import io.swagger.annotations.ApiModelProperty;

public class ReceiveDetailRequ {


    @ApiModelProperty(value = "赠送唯一编号", required = true)
    private String giftUniqueCode;

    public String getGiftUniqueCode() {
        return giftUniqueCode;
    }

    public void setGiftUniqueCode(String giftUniqueCode) {
        this.giftUniqueCode = giftUniqueCode;
    }
}
