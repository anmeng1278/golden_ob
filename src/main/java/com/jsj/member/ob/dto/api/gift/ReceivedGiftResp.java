package com.jsj.member.ob.dto.api.gift;

public class ReceivedGiftResp {

    private int giftId;

    public int getGiftId() {
        return giftId;
    }

    public void setGiftId(int giftId) {
        this.giftId = giftId;
    }

    /*
    赠送编号
     */
    private String giftUniqueCode;

    public String getGiftUniqueCode() {
        return giftUniqueCode;
    }

    public void setGiftUniqueCode(String giftUniqueCode) {
        this.giftUniqueCode = giftUniqueCode;
    }
}
