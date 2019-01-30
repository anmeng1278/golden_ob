package com.jsj.member.ob.dto.mini;

/**
 * 微信用户加密数据解密后的信息
 */
public class WxWatermark {
    /**
     * 敏感数据归属 appId，开发者可校验此参数与自身 appId 是否一致
     */
    private String appid;

    /**
     * 敏感数据获取的时间戳, 开发者可以用于数据时效性校验
     */
    private int timestamp;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }
}
