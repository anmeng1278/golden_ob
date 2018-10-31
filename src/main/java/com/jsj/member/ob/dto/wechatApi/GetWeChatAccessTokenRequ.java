package com.jsj.member.ob.dto.wechatApi;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GetWeChatAccessTokenRequ {

    public GetWeChatAccessTokenRequ(){
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

        @JSONField(name = "Code")
        private String Code;


        public String getCode() {
            return Code;
        }

        public void setCode(String code) {
            Code = code;
        }
    }
}
