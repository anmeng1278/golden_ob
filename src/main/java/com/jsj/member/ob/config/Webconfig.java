package com.jsj.member.ob.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "webconfig")
@Component
public class Webconfig {

    private boolean imgServerSwitch;

    private String imgServerURL;

    //获取 access_token
    private String accessTokenUrl;

    //获取 jsapi_ticket
    private String jsApiTicketUrl;

    //通过code获取网页授权access_token
    private String weChatAccessTokenUrl;

    //微信公众号支付
    private String payTradeUrl;

    public String getAccessTokenUrl() {
        return accessTokenUrl;
    }

    public void setAccessTokenUrl(String accessTokenUrl) {
        this.accessTokenUrl = accessTokenUrl;
    }

    public String getJsApiTicketUrl() {
        return jsApiTicketUrl;
    }

    public void setJsApiTicketUrl(String jsApiTicketUrl) {
        this.jsApiTicketUrl = jsApiTicketUrl;
    }

    public String getWeChatAccessTokenUrl() {
        return weChatAccessTokenUrl;
    }

    public void setWeChatAccessTokenUrl(String weChatAccessTokenUrl) {
        this.weChatAccessTokenUrl = weChatAccessTokenUrl;
    }

    public String getPayTradeUrl() {
        return payTradeUrl;
    }

    public void setPayTradeUrl(String payTradeUrl) {
        this.payTradeUrl = payTradeUrl;
    }

    public boolean isImgServerSwitch() {
        return imgServerSwitch;
    }

    public void setImgServerSwitch(boolean imgServerSwitch) {
        this.imgServerSwitch = imgServerSwitch;
    }

    public String getImgServerURL() {
        return imgServerURL;
    }

    public void setImgServerURL(String imgServerURL) {
        this.imgServerURL = imgServerURL;
    }
}
