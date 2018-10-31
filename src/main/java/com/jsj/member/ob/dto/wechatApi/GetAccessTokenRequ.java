package com.jsj.member.ob.dto.wechatApi;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GetAccessTokenRequ {

    public GetAccessTokenRequ(){
        this.requestHead = new RequestHead();
    }

    @JsonProperty
    private RequestHead requestHead;

    @JsonProperty
    @JSONField(name = "RequestHead")
    public RequestHead getRequestHead() {
        return requestHead;
    }

    @JsonProperty
    public void setRequestHead(RequestHead requestHead) {
        this.requestHead = requestHead;
    }
}
