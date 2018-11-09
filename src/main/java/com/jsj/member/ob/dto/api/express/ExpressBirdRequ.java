package com.jsj.member.ob.dto.api.express;

import com.alibaba.fastjson.annotation.JSONField;

public class ExpressBirdRequ {

    @JSONField(name="ShipperCode")
    private String ShipperCode;

    @JSONField(name="LogisticCode")
    private String LogisticCode;

    @Override
    public String toString() {
        return "{" +
                "ShipperCode:'" + ShipperCode + '\'' +
                ", LogisticCode:'" + LogisticCode + '\'' +
                '}';
    }

    public String getShipperCode() {
        return ShipperCode;
    }

    public void setShipperCode(String shipperCode) {
        ShipperCode = shipperCode;
    }

    public String getLogisticCode() {
        return LogisticCode;
    }

    public void setLogisticCode(String logisticCode) {
        LogisticCode = logisticCode;
    }
}
