package com.jsj.member.ob.dto.api.express;


import com.alibaba.fastjson.annotation.JSONField;


public class RequestHeaders {
    public RequestHeaders(){
        this.Accept = "application/json, text/javascript, */*; q=0.01";
        this.AcceptEncoding="gzip, deflate";
        this.AcceptLanguage="zh-CN,zh;q=0.9";
        this.UserAgent="Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3554.0 Safari/537.36";
        this.XRequestedWith="XMLHttpRequest";
    }


        @JSONField(name="Accept")
        private String Accept;

        @JSONField(name="Accept-Encoding")
        private String AcceptEncoding;

        @JSONField(name="Accept-Language")
        private String AcceptLanguage;

        @JSONField(name="User-Agent")
        private String UserAgent;

        @JSONField(name="X-Requested-With")
        private String XRequestedWith;

        public String getAccept() {
            return Accept;
        }

        public void setAccept(String accept) {
            Accept = accept;
        }

        public String getAcceptEncoding() {
            return AcceptEncoding;
        }

        public void setAcceptEncoding(String acceptEncoding) {
            AcceptEncoding = acceptEncoding;
        }

        public String getAcceptLanguage() {
            return AcceptLanguage;
        }

        public void setAcceptLanguage(String acceptLanguage) {
            AcceptLanguage = acceptLanguage;
        }

        public String getUserAgent() {
            return UserAgent;
        }

        public void setUserAgent(String userAgent) {
            UserAgent = userAgent;
        }

        public String getXRequestedWith() {
            return XRequestedWith;
        }

        public void setXRequestedWith(String XRequestedWith) {
            this.XRequestedWith = XRequestedWith;
        }
    }

