package com.jsj.member.ob.dto.wechatApi;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jsj.member.ob.utils.DateUtils;

public class RequestHead {

    public RequestHead() {
        this.TimeStamp = DateUtils.getCurrentUnixTime() + "";
        this.SourceFrom = "JSJ";
    }

    @JsonProperty
    private String TimeStamp;

    @JsonProperty
    private String SourceFrom;

    @JsonProperty
    private String Sign;

    @JsonIgnore
    @JSONField(name = "TimeStamp")
    public String getTimeStamp() {
        return TimeStamp;
    }

    @JsonIgnore
    public void setTimeStamp(String timeStamp) {
        TimeStamp = timeStamp;
    }

    @JsonIgnore
    @JSONField(name = "SourceFrom")
    public String getSourceFrom() {
        return SourceFrom;
    }

    @JsonIgnore
    public void setSourceFrom(String sourceFrom) {
        SourceFrom = sourceFrom;
    }

    @JsonIgnore
    @JSONField(name = "Sign")
    public String getSign() {
        return Sign;
    }

    @JsonIgnore
    public void setSign(String sign) {
        Sign = sign;
    }
}
