package com.jsj.member.ob.dto.thirdParty;

import com.alibaba.fastjson.annotation.JSONField;

public class CreateGoldenCardResp {

    public CreateGoldenCardResp() {
        this.responseHead = new ResponseHead();
    }

    @JSONField(name = "ResponseHead")
    private ResponseHead responseHead;


    public ResponseHead getResponseHead() {
        return responseHead;
    }

    public void setResponseHead(ResponseHead responseHead) {
        this.responseHead = responseHead;
    }

}
