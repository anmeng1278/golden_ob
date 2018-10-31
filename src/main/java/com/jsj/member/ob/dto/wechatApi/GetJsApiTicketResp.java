package com.jsj.member.ob.dto.wechatApi;

import com.alibaba.fastjson.annotation.JSONField;

public class GetJsApiTicketResp {

    public GetJsApiTicketResp(){
        this.responseHead = new ResponseHead();
    }

    @JSONField(name = "ResponseHead")
    private ResponseHead responseHead;

    @JSONField(name = "RequestBody")
    private ResponseBody responseBody;

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

    public class ResponseBody{

        public String getJsApiTicket() {
            return JsApiTicket;
        }

        public void setJsApiTicket(String jsApiTicket) {
            JsApiTicket = jsApiTicket;
        }

        private String JsApiTicket;

    }
}
