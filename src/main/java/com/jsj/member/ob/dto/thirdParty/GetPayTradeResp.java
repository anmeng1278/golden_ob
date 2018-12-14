package com.jsj.member.ob.dto.thirdParty;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import org.apache.commons.lang3.StringUtils;

public class GetPayTradeResp {

    public GetPayTradeResp() {
        this.responseHead = new ResponseHead();
    }

    @JSONField(name = "ResponseHead")
    private ResponseHead responseHead;

    @JSONField(name = "ResponseBody")
    private ResponseBody responseBody;

    @JSONField(name = "ResponseHead")
    public ResponseHead getResponseHead() {
        return responseHead;
    }

    public void setResponseHead(ResponseHead responseHead) {
        this.responseHead = responseHead;
    }

    public ResponseBody getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(ResponseBody responseBody) {
        this.responseBody = responseBody;
    }

    public class ResponseBody {

        @JSONField(name = "ResponseText")
        private String ResponseText;

        public String getResponseText() {
            return ResponseText;
        }

        public void setResponseText(String responseText) {
            ResponseText = responseText;
        }

        private String appId;
        private String timeStamp;
        private String nonceStr;
        @JSONField(name = "package")
        private String _package;
        private String signType;
        private String paySign;

        public void Parse() {
            if (!StringUtils.isEmpty(this.getResponseText())) {
                JSONObject jsonObject = JSON.parseObject(this.getResponseText());
                this.appId = jsonObject.get("appId").toString();
                this.timeStamp = jsonObject.get("timeStamp").toString();
                this.nonceStr = jsonObject.get("nonceStr").toString();
                this._package = jsonObject.get("package").toString();
                this.signType = jsonObject.get("signType").toString();
                this.paySign = jsonObject.get("paySign").toString();
            }
        }

        public String getAppId() {
            return appId;
        }

        public String getTimeStamp() {
            return timeStamp;
        }

        public String getNonceStr() {
            return nonceStr;
        }

        public String get_package() {
            return _package;
        }

        public String getSignType() {
            return signType;
        }

        public String getPaySign() {
            return paySign;
        }

        //"{\"appId\":\"wx555b169abb4d62ce\",\"timeStamp\":\"1544802746\",\"nonceStr\":\"1m40kqqdjzrbtx7yl4vxymroeerxkj84\",\"package\":\"prepay_id=wx141552270715285f9854d2e10606969281\",\"signType\":\"MD5\",\"paySign\":\"13CA39013606BA1062BE1E1517B6EEC1\"}"
    }
}
