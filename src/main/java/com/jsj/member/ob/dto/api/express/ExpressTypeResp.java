package com.jsj.member.ob.dto.api.express;

import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExpressTypeResp {

    public ExpressTypeResp(){
        this.auto = new ArrayList<>();
    }

    private String comCode;

    private String num;

    private List<T> auto;

    public String getComCode() {
        return comCode;
    }

    public void setComCode(String comCode) {
        this.comCode = comCode;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public List<T> getAuto() {
        return auto;
    }

    public void setAuto(List<T> auto) {
        this.auto = auto;
    }

    public class auto{

        private String comCode;

        private String id;

        private String noCount;

        private String noPre;

        private Date startTime;

        public String getComCode() {
            return comCode;
        }

        public void setComCode(String comCode) {
            this.comCode = comCode;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNoCount() {
            return noCount;
        }

        public void setNoCount(String noCount) {
            this.noCount = noCount;
        }

        public String getNoPre() {
            return noPre;
        }

        public void setNoPre(String noPre) {
            this.noPre = noPre;
        }

        public Date getStartTime() {
            return startTime;
        }

        public void setStartTime(Date startTime) {
            this.startTime = startTime;
        }
    }

}
