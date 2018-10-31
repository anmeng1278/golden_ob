package com.jsj.member.ob.dto.wechatApi;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.poi.ss.formula.functions.T;

public class GetPayTradeRequ {

    public GetPayTradeRequ(){
        this.requestHead = new RequestHead();
        this.requestBody = new RequestBody();
    }

    @JsonProperty
    private RequestHead requestHead;

    @JsonProperty
    private RequestBody requestBody;

    @JsonProperty
    @JSONField(name = "RequestBody")
    public RequestBody getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
    }

    @JsonProperty
    @JSONField(name = "RequestHead")
    public RequestHead getRequestHead() {
        return requestHead;
    }

    @JsonProperty
    public void setRequestHead(RequestHead requestHead) {
        this.requestHead = requestHead;
    }

    public class RequestBody{

        @JSONField(name = "RequestHead")
        private String PlatformAppId;

        @JSONField(name = "RequestHead")
        private String PlatformToken;

        @JSONField(name = "RequestHead")
        private String  SourceWay;

        @JSONField(name = "RequestHead")
        private String SourceApp;

        @JSONField(name = "RequestHead")
        private String PayMethod;

        @JSONField(name = "RequestHead")
        private String OutTradeId;

        @JSONField(name = "RequestHead")
        private String PayAmount;

        @JSONField(name = "RequestHead")
        private String OpenId;

        @JSONField(name = "RequestHead")
        private String OrderTimeOu;

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

        public String getOrderTimeOu() {
            return OrderTimeOu;
        }

        public void setOrderTimeOu(String orderTimeOu) {
            OrderTimeOu = orderTimeOu;
        }
    }
}
