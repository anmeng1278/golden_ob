package com.jsj.member.ob.dto.mini;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

public class RegisterRequ {

    @NotBlank(message = "OPENID")
    @ApiModelProperty(value = "OPENID", required = true)
    private String openId;

    @NotBlank(message = "小程序授权后的原始数据")
    @ApiModelProperty(value = "小程序授权后的原始数据", required = true)
    private String rawData;

    @NotBlank(message = "小程序授权后的原始数据")
    @ApiModelProperty(value = "小程序授权后的原始数据", required = true)
    private String encryptedData;

    @NotBlank(message = "小程序授权后的原始数据")
    @ApiModelProperty(value = "小程序授权后的原始数据", required = true)
    private String iv;

    @ApiModelProperty(value = "会员编号，可选")
    private int jsjId;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getRawData() {
        return rawData;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }

    public int getJsjId() {
        return jsjId;
    }

    public void setJsjId(int jsjId) {
        this.jsjId = jsjId;
    }

    public String getEncryptedData() {
        return encryptedData;
    }

    public void setEncryptedData(String encryptedData) {
        this.encryptedData = encryptedData;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }
}
