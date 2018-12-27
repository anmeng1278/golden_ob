package com.jsj.member.ob.dto.api.delivery;

public class VerifyActivityCodeResp {

    /**
     * 返回码 0000为成功
     */
    private String respCd;

    /**
     * 错误消息
     */
    private String respMsg;

    public String getRespCd() {
        return respCd;
    }

    public void setRespCd(String respCd) {
        this.respCd = respCd;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }
}
