package com.jsj.member.ob.dto.http;

public class RequestHead {

    private String TimeStamp;

    private String SourceFrom;

    private String Sign;

    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        TimeStamp = timeStamp;
    }

    public String getSourceFrom() {
        return SourceFrom;
    }

    public void setSourceFrom(String sourceFrom) {
        SourceFrom = sourceFrom;
    }

    public String getSign() {
        return Sign;
    }

    public void setSign(String sign) {
        Sign = sign;
    }
}
