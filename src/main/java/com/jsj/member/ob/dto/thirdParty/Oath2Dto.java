package com.jsj.member.ob.dto.thirdParty;

public class Oath2Dto {

    private int errcode;
    private String errmsg;

    private String access_token;
    private int expires_in;
    private  String refresh_token;
    private  String openid;
    private  String scope;

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}


//{"errcode":40163,"errmsg":"code been used, hints: [ req_id: 5P_i5a0393th31 ]"}"
//{"access_token":"16_h4GfCK0kVJ9dzcOc1sh2vZns5-7y-AjwaKjBBkjdOphFG-8Zv9_z0j0RqzZ4Bzg5lkb-TL99-a6R6F3a2JWeuFDEPwUqIL35q9owsikQcjU","expires_in":7200,"refresh_token":"16_eSQvwy3IZfic2wVUP_JuhaOWi0LlGDiQFm8FUZH1uxnJarYL_2e0n6yoBPhmefHEvrQlsgDJmIhO73j84O2eEsyA9Q-IBz0vpg7TbSohKAo","openid":"o2JcesxmAIQWeqEEqA-vM-i44Miw","scope":"snsapi_userinfo"}
