import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;

public class Res {

    @JSONField(name = "BaseResponse")
    private BaseResponse baseResponse;

    @JSONField(name = "ActivityCodes")
    private ArrayList<String> ActivityCodes;

    @JSONField(name = "ExpiredDate")
    private String ExpiredDate;

    public BaseResponse getBaseResponse() {
        return baseResponse;
    }

    public void setBaseResponse(BaseResponse baseResponse) {
        this.baseResponse = baseResponse;
    }

    public ArrayList<String> getActivityCodes() {
        return ActivityCodes;
    }

    public void setActivityCodes(ArrayList<String> activityCodes) {
        ActivityCodes = activityCodes;
    }

    public String getExpiredDate() {
        return ExpiredDate;
    }

    public void setExpiredDate(String expiredDate) {
        ExpiredDate = expiredDate;
    }

    public class BaseResponse {

        @JSONField(name = "IsSuccess")
        private boolean isSuccess;

        public boolean isSuccess() {
            return isSuccess;
        }

        public void setSuccess(boolean success) {
            isSuccess = success;
        }
    }

}
