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
     */
    private int giftId;

    public int getGiftId() {
        return giftId;
    }

    public void setGiftId(int giftId) {
        this.giftId = giftId;
    }
}
