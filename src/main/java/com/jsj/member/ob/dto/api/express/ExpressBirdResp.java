package com.jsj.member.ob.dto.api.express;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExpressBirdResp {

    public ExpressBirdResp(){
        this.Traces = new ArrayList<>();
        this.EBusinessID = "1400214";
    }

    @JSONField(name="EBusinessID")
    private String EBusinessID;

    @JSONField(name="ShipperCode")
    private String ShipperCode;

    @JSONField(name="LogisticCode")
    private String LogisticCode;

    @JSONField(name="Success")
    private boolean Success;

    @JSONField(name="Reason")
    private String Reason;

    @JSONField(name="State")
    private String State;

    @JSONField(name="Traces")
    private List<Traces> Traces;

    public String getEBusinessID() {
        return EBusinessID;
    }

    public void setEBusinessID(String EBusinessID) {
        this.EBusinessID = EBusinessID;
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

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean success) {
        Success = success;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public List<ExpressBirdResp.Traces> getTraces() {
        return Traces;
    }

    public void setTraces(List<ExpressBirdResp.Traces> traces) {
        Traces = traces;
    }

    public class Traces {
        @JSONField(name="AcceptTime")
        private Date AcceptTime;
        @JSONField(name="AcceptStation")
        private String AcceptStation;
        @JSONField(name="Remark")
        private String Remark;

        public Date getAcceptTime() {
            return AcceptTime;
        }

        public void setAcceptTime(Date acceptTime) {
            AcceptTime = acceptTime;
        }

        public String getAcceptStation() {
            return AcceptStation;
        }

        public void setAcceptStation(String acceptStation) {
            AcceptStation = acceptStation;
        }

        public String getRemark() {
            return Remark;
        }

        public void setRemark(String remark) {
            Remark = remark;
        }
    }


}
