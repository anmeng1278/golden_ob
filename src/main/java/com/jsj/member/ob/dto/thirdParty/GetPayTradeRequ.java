package com.jsj.member.ob.dto.thirdParty;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

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

        @JSONField(name = "PlatformAppId")
        private String PlatformAppId;

        @JSONField(name = "PlatformToken")
        private String PlatformToken;

        @JSONField(name = "SourceWay")
        private String  SourceWay;

        @JSONField(name = "SourceApp")
        private String SourceApp;

        @JSONField(name = "PayMethod")
        private String PayMethod;

        @JSONField(name = "OutTradeId")
        private String OutTradeId;

        @JSONField(name = "PayAmount")
        private String PayAmount;

        @JSONField(name = "OpenId")
        private String OpenId;

        @JSONField(name = "OrderTimeOut")
        private String OrderTimeOut;

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
    }
}
