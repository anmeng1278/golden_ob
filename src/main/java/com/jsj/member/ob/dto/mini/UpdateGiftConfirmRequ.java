package com.jsj.member.ob.dto.mini;

import com.jsj.member.ob.enums.GiftShareType;
import io.swagger.annotations.ApiModelProperty;

public class UpdateGiftConfirmRequ {

    @ApiModelProperty(value = "openId，必填", required = true)
    private String openId;

    @ApiModelProperty(value = "unionId，必填", required = true)
    private String unionId;

    @ApiModelProperty(value = "操作方法，ready准备分享  success分享成功 必填", required = true)
    private String op;

    @ApiModelProperty(value = "赠送唯一编码，必填", required = true)
    private String giftUniqueCode;

    @ApiModelProperty(value = "分享类型")
    private GiftShareType giftShareType;

    @ApiModelProperty(value = "分享祝福语")
    private String blessings;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public String getGiftUniqueCode() {
        return giftUniqueCode;
    }

    public void setGiftUniqueCode(String giftUniqueCode) {
        this.giftUniqueCode = giftUniqueCode;
    }

    public GiftShareType getGiftShareType() {
        return giftShareType;
    }

    public void setGiftShareType(GiftShareType giftShareType) {
        this.giftShareType = giftShareType;
    }

    public String getBlessings() {
        return blessings;
    }

    public void setBlessings(String blessings) {
        this.blessings = blessings;
    }
}
