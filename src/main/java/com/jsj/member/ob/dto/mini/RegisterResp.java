package com.jsj.member.ob.dto.mini;

import io.swagger.annotations.ApiModelProperty;

public class RegisterResp {

    @ApiModelProperty(value = "unionId", required = true)
    private String unionId;

    @ApiModelProperty(value = "openId", required = true)
    private String openId;

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
}
