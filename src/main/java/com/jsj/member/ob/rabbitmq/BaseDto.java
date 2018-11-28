package com.jsj.member.ob.rabbitmq;

import java.io.Serializable;

public class BaseDto implements Serializable {

    /**
     * 是否成功
     */
    private Boolean isSuccess;

    /**
     * 处理时间
     */
    private int timeStamp;

    /**
     * 错误消息
     */
    private String message;

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }
}
