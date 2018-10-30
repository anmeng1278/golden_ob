package com.jsj.member.ob.dto.api.gift;

import com.jsj.member.ob.dto.BaseRequ;

public class CancelGiftRequ {

    public CancelGiftRequ() {
        this.baseRequ = new BaseRequ();
    }

    private BaseRequ baseRequ;

    public BaseRequ getBaseRequ() {
        return baseRequ;
    }

    public void setBaseRequ(BaseRequ baseRequ) {
        this.baseRequ = baseRequ;
    }

    /*
    赠送编号
    此编号用于url上使用，以防止网址被刷
     */
    private String giftUniqueCode;

    public String getGiftUniqueCode() {
        return giftUniqueCode;
    }

    public void setGiftUniqueCode(String giftUniqueCode) {
        this.giftUniqueCode = giftUniqueCode;
    }
}
