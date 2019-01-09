package com.jsj.member.ob.dto;

public class BaseRequ {

    /**
     * 时间戳
     */
    private int timeStamp;

    /**
     * 公众号OpenID
     */
    private String openId;


    /**
     * 会员编号
     */
    private int jsjId;


    public int getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public int getJsjId() {
        return jsjId;
    }

    public void setJsjId(int jsjId) {
        this.jsjId = jsjId;
    }
}
