package com.jsj.member.ob.dto.mini;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

public class MiniUserDecodeRequ {

    @NotBlank(message = "OPENID")
    @ApiModelProperty(value = "OPENID", required = true)
    private String openId;

    /**
     * 加密算法的初始向量
     */
    @NotBlank(message = "加密算法的初始向量不能为空")
    @ApiModelProperty(value = "加密算法的初始向量", required = true)
    private String Iv;

    /**
     * 包括敏感数据在内的完整用户信息的加密数据
     */
    @NotBlank(message = "包括敏感数据在内的完整用户信息的加密数据不能为空")
    @ApiModelProperty(value = "包括敏感数据在内的完整用户信息的加密数据", required = true)
    private String encryptedData;

    public String getIv() {
        return Iv;
    }

    public void setIv(String iv) {
        Iv = iv;
    }

    public String getEncryptedData() {
        return encryptedData;
    }

    public void setEncryptedData(String encryptedData) {
        this.encryptedData = encryptedData;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
