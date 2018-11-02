package com.jsj.member.ob.dto.thirdParty;

import com.alibaba.fastjson.annotation.JSONField;

public class GetPayTradeResp {

    public GetPayTradeResp() {
        this.responseHead = new ResponseHead();
    }

    @JSONField(name = "ResponseHead")
    private ResponseHead responseHead;

    @JSONField(name = "ResponseBody")
    private ResponseBody responseBody;

    @JSONField(name = "ResponseHead")
    public ResponseHead getResponseHead() {
        return responseHead;
    }

    public void setResponseHead(ResponseHead responseHead) {
        this.responseHead = responseHead;
    }

    public ResponseBody getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(ResponseBody responseBody) {
        this.responseBody = responseBody;
    }

    public class ResponseBody {

        @JSONField(name = "ResponseText")
        private String ResponseText;

        public String getResponseText() {
            return ResponseText;
        }

        public void setResponseText(String responseText) {
            ResponseText = responseText;
        }
    }
}
