package com.jsj.member.ob.dto.thirdParty;

import com.alibaba.fastjson.annotation.JSONField;

public class GetActivityCodesRequ {

    @JSONField(name = "Count")
    private int count;

    @JSONField(name = "PartnerLoginName")
    private String partnerLoginName;

    @JSONField(name = "PartnerLoginPassword")
    private String partnerLoginPassword;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getPartnerLoginName() {
        return partnerLoginName;
    }

    public void setPartnerLoginName(String partnerLoginName) {
        this.partnerLoginName = partnerLoginName;
    }

    public String getPartnerLoginPassword() {
        return partnerLoginPassword;
    }

    public void setPartnerLoginPassword(String partnerLoginPassword) {
        this.partnerLoginPassword = partnerLoginPassword;
    }
}
