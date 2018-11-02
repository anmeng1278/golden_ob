package com.jsj.member.ob.dto.thirdParty;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GetJsApiTicketRequ {

    public GetJsApiTicketRequ(){
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

        @JSONField(name = "AccessToken")
        private String AccessToken;

        public String getAccessToken() {
            return AccessToken;
        }

        public void setAccessToken(String accessToken) {
            AccessToken = accessToken;
        }
    }
}
