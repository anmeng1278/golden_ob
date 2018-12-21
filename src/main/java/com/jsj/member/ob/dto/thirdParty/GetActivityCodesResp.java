package com.jsj.member.ob.dto.thirdParty;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class GetActivityCodesResp {

    public GetActivityCodesResp(){
        this.baseResponse = new BaseResponse();
    }
    //响应数据
    //{"BaseResponse":{"IsSuccess":true,"ErrorMessage":null,"ErrorCode":null,"Exception":null},"ActivityCodes":["GZYL93d864507390"],"ExpiredDate":"2019-01-17T10:53:16.3075547+08:00"}

    @JSONField(name = "BaseResponse")
    private BaseResponse baseResponse;

    @JSONField(name = "ActivityCodes")
    private List<String> activityCodes;

    @JSONField(name = "ExpiredDate")
    private String expiredDate;

    public BaseResponse getBaseResponse() {
        return baseResponse;
    }

    public void setBaseResponse(BaseResponse baseResponse) {
        this.baseResponse = baseResponse;
    }

    public List<String> getActivityCodes() {
        return activityCodes;
    }

    public void setActivityCodes(List<String> activityCodes) {
        this.activityCodes = activityCodes;
    }

    public String getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(String expiredDate) {
        this.expiredDate = expiredDate;
    }

    public  class  BaseResponse{

        @JSONField(name = "IsSuccess")
        private boolean isSuccess;
        @JSONField(name = "ErrorMessage")
        private String errorMessage;
        @JSONField(name = "ErrorCode")
        private Integer errorCode;

        public boolean isSuccess() {
            return isSuccess;
        }

        public void setSuccess(boolean success) {
            isSuccess = success;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public Integer getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(Integer errorCode) {
            this.errorCode = errorCode;
        }
    }
}
