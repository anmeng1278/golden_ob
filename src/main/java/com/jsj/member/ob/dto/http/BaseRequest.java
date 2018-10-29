package com.jsj.member.ob.dto.http;

public class BaseRequest {

    private RequestBody requestBody;

    private RequestHead requestHead;

    private String TradeStatus;
    private String Message;
    private String OutTradeId;
    private String TradeAmount;
    private String TradeOrderId;
    private String TradeBillNo;
    private String TradeTime;
    private String TimeStamp;
    private String Sign;

    public RequestBody getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
    }

    public RequestHead getRequestHead() {
        return requestHead;
    }

    public void setRequestHead(RequestHead requestHead) {
        this.requestHead = requestHead;
    }

    public String getSign() {
        return Sign;
    }

    public void setSign(String sign) {
        Sign = sign;
    }

    public String getTradeStatus() {
        return TradeStatus;
    }

    public void setTradeStatus(String tradeStatus) {
        TradeStatus = tradeStatus;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getOutTradeId() {
        return OutTradeId;
    }

    public void setOutTradeId(String outTradeId) {
        OutTradeId = outTradeId;
    }

    public String getTradeAmount() {
        return TradeAmount;
    }

    public void setTradeAmount(String tradeAmount) {
        TradeAmount = tradeAmount;
    }

    public String getTradeOrderId() {
        return TradeOrderId;
    }

    public void setTradeOrderId(String tradeOrderId) {
        TradeOrderId = tradeOrderId;
    }

    public String getTradeBillNo() {
        return TradeBillNo;
    }

    public void setTradeBillNo(String tradeBillNo) {
        TradeBillNo = tradeBillNo;
    }

    public String getTradeTime() {
        return TradeTime;
    }

    public void setTradeTime(String tradeTime) {
        TradeTime = tradeTime;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        TimeStamp = timeStamp;
    }
}
