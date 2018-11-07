package com.jsj.member.ob.dto.api.express;

import com.alibaba.fastjson.annotation.JSONField;
import org.apache.poi.ss.formula.functions.T;

import java.util.Date;
import java.util.List;

public class ExpressRequ {

    public ExpressRequ(){
        this.requestHeaders = new RequestHeaders();
    }
    @JSONField(name="RequestHeaders")
   private RequestHeaders requestHeaders;

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public RequestHeaders getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestHeaders(RequestHeaders requestHeaders) {
        this.requestHeaders = requestHeaders;
    }
}
