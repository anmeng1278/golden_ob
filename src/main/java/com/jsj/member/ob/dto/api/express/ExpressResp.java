package com.jsj.member.ob.dto.api.express;

import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExpressResp<T> {
    public ExpressResp(){
        this.data = new ArrayList<>();
    }

    private String com;

    private String condition;

    private List<T> data;

    private String isCheck;

    private String message;

    private String nu;

    private String state;

    private String status;

    public String getCom() {
        return com;
    }

    public void setCom(String com) {
        this.com = com;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public String getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(String isCheck) {
        this.isCheck = isCheck;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNu() {
        return nu;
    }

    public void setNu(String nu) {
        this.nu = nu;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public class data{

        private String context;

        private Date ftime;

        private String location;

        private Date  time;

        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
        }

        public Date getFtime() {
            return ftime;
        }

        public void setFtime(Date ftime) {
            this.ftime = ftime;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public Date getTime() {
            return time;
        }

        public void setTime(Date time) {
            this.time = time;
        }
    }

}
