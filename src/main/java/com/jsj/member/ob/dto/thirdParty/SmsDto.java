package com.jsj.member.ob.dto.thirdParty;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jsj.member.ob.enums.SmsLevel;

public class SmsDto {

    /**
     * 手机号
     */
    private String Mobile;

    /**
     * 短信内容
     */
    private String Contents;

    /**
     * 短信级别
     */
    private SmsLevel SmsLevel;

    /**
     * 订单号
     */
    private String OrderID;

    /**
     * 准备发送时间，为空时立即发送
     */
    private String ReadySendTime;

    private int SendCompany;

    private int SmsSouce;

    @JsonProperty
    @JSONField(name = "SmsSouce")
    public int getSmsSouce() {
        return SmsSouce;
    }

    public void setSmsSouce(int smsSouce) {
        SmsSouce = smsSouce;
    }

    @JsonProperty
    @JSONField(name = "SendCompany")
    public int getSendCompany() {
        return SendCompany;
    }

    public void setSendCompany(int sendCompany) {
        SendCompany = sendCompany;
    }

    @JsonProperty
    @JSONField(name = "Mobile")
    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    @JsonProperty
    @JSONField(name = "Contents")
    public String getContents() {
        return Contents;
    }

    public void setContents(String contents) {
        Contents = contents;
    }

    @JsonProperty
    @JSONField(name = "SmsLevel")
    public int getSmsLevel() {
        return SmsLevel.getValue();
    }

    public void setSmsLevel(com.jsj.member.ob.enums.SmsLevel smsLevel) {
        SmsLevel = smsLevel;
    }

    @JsonProperty
    @JSONField(name = "OrderID")
    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    @JsonProperty
    @JSONField(name = "ReadySendTime")
    public String getReadySendTime() {
        return ReadySendTime;
    }

    public void setReadySendTime(String readySendTime) {
        ReadySendTime = readySendTime;
    }
}
