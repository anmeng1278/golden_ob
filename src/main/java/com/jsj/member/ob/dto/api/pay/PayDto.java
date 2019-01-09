package com.jsj.member.ob.dto.api.pay;

public class PayDto {

    /**
     * openId 用于支付
     */
    private String openId;

    /**
     * platformAppId
     */
    private String platformAppId;

    /**
     * platformToken
     */
    private String platformToken;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getPlatformAppId() {
        return platformAppId;
    }

    public void setPlatformAppId(String platformAppId) {
        this.platformAppId = platformAppId;
    }

    public String getPlatformToken() {
        return platformToken;
    }

    public void setPlatformToken(String platformToken) {
        this.platformToken = platformToken;
    }
}
