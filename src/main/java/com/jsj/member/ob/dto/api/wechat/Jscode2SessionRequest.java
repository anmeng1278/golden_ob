package com.jsj.member.ob.dto.api.wechat;

import com.jsj.member.ob.dto.BaseRequ;

public class Jscode2SessionRequest {

    private BaseRequ baseRequ;

    /**
     * 登录时获取的 code
     */
    private String jsCode;

    public BaseRequ getBaseRequ() {
        return baseRequ;
    }

    public void setBaseRequ(BaseRequ baseRequ) {
        this.baseRequ = baseRequ;
    }

    public String getJsCode() {
        return jsCode;
    }

    public void setJsCode(String jsCode) {
        this.jsCode = jsCode;
    }
}
