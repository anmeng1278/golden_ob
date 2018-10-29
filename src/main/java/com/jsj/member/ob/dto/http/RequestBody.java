package com.jsj.member.ob.dto.http;

import org.apache.poi.ss.formula.functions.T;

public class RequestBody {

    private String Code;

    private String AccessToken;

    private String PlatformAppId;

    private String PlatformToken;

    private String SourceWay ="20" ;
    private String SourceApp ="600";
    private String PayMethod ="22" ;
    private String OutTradeId ;
    private String PayAmount;
    private String OpenId ;
    private String OrderTimeOut;

    public String getAccessToken() {
        return AccessToken;
    }

    public void setAccessToken(String accessToken) {
        AccessToken = accessToken;
    }

    public String getPlatformAppId() {
        return PlatformAppId;
    }

    public void setPlatformAppId(String platformAppId) {
        PlatformAppId = platformAppId;
    }

    public String getPlatformToken() {
        return PlatformToken;
    }

    public void setPlatformToken(String platformToken) {
        PlatformToken = platformToken;
    }

    public String getSourceWay() {
        return SourceWay;
    }

    public void setSourceWay(String sourceWay) {
        SourceWay = sourceWay;
    }

    public String getSourceApp() {
        return SourceApp;
    }

    public void setSourceApp(String sourceApp) {
        SourceApp = sourceApp;
    }

    public String getPayMethod() {
        return PayMethod;
    }

    public void setPayMethod(String payMethod) {
        PayMethod = payMethod;
    }

    public String getOutTradeId() {
        return OutTradeId;
    }

    public void setOutTradeId(String outTradeId) {
        OutTradeId = outTradeId;
    }

    public String getPayAmount() {
        return PayAmount;
    }

    public void setPayAmount(String payAmount) {
        PayAmount = payAmount;
    }

    public String getOpenId() {
        return OpenId;
    }

    public void setOpenId(String openId) {
        OpenId = openId;
    }

    public String getOrderTimeOut() {
        return OrderTimeOut;
    }

    public void setOrderTimeOut(String orderTimeOut) {
        OrderTimeOut = orderTimeOut;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }
}
