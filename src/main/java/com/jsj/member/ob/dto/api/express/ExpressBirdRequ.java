package com.jsj.member.ob.dto.api.express;

import com.alibaba.fastjson.annotation.JSONField;

public class ExpressBirdRequ {

    public ExpressBirdRequ(){
        this.expressBirdHeader = new ExpressBirdHeader();
    }

    @JSONField(name="ShipperCode")
    private String ShipperCode;

    @JSONField(name="LogisticCode")
    private String LogisticCode;

    @JSONField(name="ExpressBirdHeader")
    private ExpressBirdHeader expressBirdHeader;

    @Override
    public String toString() {
        return "{" +
                "ShipperCode:'" + ShipperCode + '\'' +
                ", LogisticCode:'" + LogisticCode + '\'' +
                '}';
    }

    public ExpressBirdHeader getExpressBirdHeader() {
        return expressBirdHeader;
    }

    public void setExpressBirdHeader(ExpressBirdHeader expressBirdHeader) {
        this.expressBirdHeader = expressBirdHeader;
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
