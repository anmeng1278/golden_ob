package com.jsj.member.ob.dto.thirdParty;

import com.alibaba.fastjson.annotation.JSONField;

public class ResponseHead {

    @JSONField(name = "Code")
    private String Code;

    @JSONField(name = "Message")
    private String Message;

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
