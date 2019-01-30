package com.jsj.member.ob.dto.mini;

import io.swagger.annotations.ApiModelProperty;

public class ExchangeRequ {

    @ApiModelProperty(value = "会员编号，可为空")
    private int jsjId;

    @ApiModelProperty(value = "openId，必填", required = true)
    private String openId;

    public int getJsjId() {
        return jsjId;
    }

    public void setJsjId(int jsjId) {
        this.jsjId = jsjId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
