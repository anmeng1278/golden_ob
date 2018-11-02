package com.jsj.member.ob.dto.thirdParty;

import com.alibaba.fastjson.annotation.JSONField;

public class GetWeChatAccessTokenResp {

    public GetWeChatAccessTokenResp() {
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

        @JSONField(name = "Access_token")
        private String Access_token;

        @JSONField(name = "Expires_in")
        private Integer Expires_in;

        @JSONField(name = "Refresh_token")
        private String Refresh_token;

        @JSONField(name = "Openid")
        private String Openid;

        @JSONField(name = "Scope")
        private String Scope;

        public String getAccess_token() {
            return Access_token;
        }

        public void setAccess_token(String access_token) {
            Access_token = access_token;
        }

        public Integer getExpires_in() {
            return Expires_in;
        }

        public void setExpires_in(Integer expires_in) {
            Expires_in = expires_in;
        }

        public String getRefresh_token() {
            return Refresh_token;
        }

        public void setRefresh_token(String refresh_token) {
            Refresh_token = refresh_token;
        }

        public String getOpenid() {
            return Openid;
        }

        public void setOpenid(String openid) {
            Openid = openid;
        }

        public String getScope() {
            return Scope;
        }

        public void setScope(String scope) {
            Scope = scope;
        }
    }
}
