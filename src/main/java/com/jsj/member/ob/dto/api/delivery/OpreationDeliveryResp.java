package com.jsj.member.ob.dto.api.delivery;

public class OpreationDeliveryResp {

    /**
     * 是否成功
     */
    private boolean isSuccess;

    /**
     * 错误消息
     */
    private String message;

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
}
