package com.jsj.member.ob.dto;

public class BaseResp {

    public  BaseResp(){
        this.timeStamp = System.currentTimeMillis() / 1000;
    }

    private int code = 0;
    private boolean isSuccess;
    private String message;
    private String url;

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    /**
     * 服务器响应时间
     */

    private long timeStamp;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
