package com.jsj.member.ob.dto.wechatApi;

import com.alibaba.fastjson.annotation.JSONField;

public class GetAccessTokenResp {

    public GetAccessTokenResp(){
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
