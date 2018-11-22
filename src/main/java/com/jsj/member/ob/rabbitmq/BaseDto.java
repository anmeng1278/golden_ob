package com.jsj.member.ob.rabbitmq;

public class BaseDto {

    /**
     * 是否成功
     */
    private Boolean isSuccess;

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


}
