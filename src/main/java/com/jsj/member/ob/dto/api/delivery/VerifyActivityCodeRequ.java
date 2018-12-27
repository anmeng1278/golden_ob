package com.jsj.member.ob.dto.api.delivery;

public class VerifyActivityCodeRequ {

    /**
     * 时间戳
     */
    private int timeStamp;

    /**
     * 活动码
     */
    private String activityCode;

    /**
     * 签名
     */
    private String sign;

    /**
     * 使用机场
     */
    private String airportName;

    /**
     * 使用贵宾厅
     */
    private String vipHallName;

    public int getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getAirportName() {
        return airportName;
    }

    public void setAirportName(String airportName) {
        this.airportName = airportName;
    }

    public String getVipHallName() {
        return vipHallName;
    }

    public void setVipHallName(String vipHallName) {
        this.vipHallName = vipHallName;
    }
}
