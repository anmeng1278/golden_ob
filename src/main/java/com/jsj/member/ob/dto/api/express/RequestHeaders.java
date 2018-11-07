package com.jsj.member.ob.dto.api.express;


import java.util.ArrayList;
import java.util.List;

public class RequestHeaders {
    public RequestHeaders(){
        this.Client = new Client();
        this.Cookies = new Cookies();
        this.MisCellaneous = new MisCellaneous();
        this.Transport = new Transport();
    }

    private Client Client;

    private Cookies Cookies;

    private MisCellaneous MisCellaneous;

    private Transport Transport;

    public RequestHeaders.Client getClient() {
        return Client;
    }

    public void setClient(RequestHeaders.Client client) {
        Client = client;
    }

    public RequestHeaders.Cookies getCookies() {
        return Cookies;
    }

    public void setCookies(RequestHeaders.Cookies cookies) {
        Cookies = cookies;
    }

    public RequestHeaders.MisCellaneous getMisCellaneous() {
        return MisCellaneous;
    }

    public void setMisCellaneous(RequestHeaders.MisCellaneous misCellaneous) {
        MisCellaneous = misCellaneous;
    }

    public RequestHeaders.Transport getTransport() {
        return Transport;
    }

    public void setTransport(RequestHeaders.Transport transport) {
        Transport = transport;
    }

    public class Client{
        private String Accept;

        private String AcceptEncoding;

        private String AcceptLanguage;

        private String UserAgent;

        private String XRequestedWith;

        public Client(){
            this.Accept = "application/json, text/javascript, */*; q=0.01";
            this.AcceptEncoding="gzip, deflate";
            this.AcceptLanguage="zh-CN,zh;q=0.9";
            this.UserAgent="Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3554.0 Safari/537.36";
            this.XRequestedWith="XMLHttpRequest";
        }

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

    public class Cookies {
        public Cookies(){
            this.Cookie = new Cookie();
        }

        public Cookie Cookie;


        public RequestHeaders.Cookies.Cookie getCookie() {
            return Cookie;
        }

        public void setCookie(RequestHeaders.Cookies.Cookie cookie) {
            Cookie = cookie;
        }

        public class Cookie {

            private String WWWID;

            private String Hm_lvt_22ea01af58ba2be0fec7c11b25e88e6c;

            private String Hm_lpvt_22ea01af58ba2be0fec7c11b25e88e6c;

            public String getWWWID() {
                return WWWID;
            }

            public void setWWWID(String WWWID) {
                this.WWWID = WWWID;
            }

            public String getHm_lvt_22ea01af58ba2be0fec7c11b25e88e6c() {
                return Hm_lvt_22ea01af58ba2be0fec7c11b25e88e6c;
            }

            public void setHm_lvt_22ea01af58ba2be0fec7c11b25e88e6c(String hm_lvt_22ea01af58ba2be0fec7c11b25e88e6c) {
                Hm_lvt_22ea01af58ba2be0fec7c11b25e88e6c = hm_lvt_22ea01af58ba2be0fec7c11b25e88e6c;
            }

            public String getHm_lpvt_22ea01af58ba2be0fec7c11b25e88e6c() {
                return Hm_lpvt_22ea01af58ba2be0fec7c11b25e88e6c;
            }

            public void setHm_lpvt_22ea01af58ba2be0fec7c11b25e88e6c(String hm_lpvt_22ea01af58ba2be0fec7c11b25e88e6c) {
                Hm_lpvt_22ea01af58ba2be0fec7c11b25e88e6c = hm_lpvt_22ea01af58ba2be0fec7c11b25e88e6c;
            }

            public Cookie(){
                this.Hm_lpvt_22ea01af58ba2be0fec7c11b25e88e6c = "1541553444";
                this.Hm_lvt_22ea01af58ba2be0fec7c11b25e88e6c = "1540786680,1541488368,1541492864,1541552189";
                this.WWWID = "WWW322F070B5B389CFE30F9D9F44DF82D51";
        }

    }

    }

    public class MisCellaneous{
        private String Referer;

        public String getReferer() {
            return Referer;
        }

        public void setReferer(String referer) {
            Referer = referer;
        }

        public MisCellaneous(){
            this.Referer = "http://www.kuaidi100.com/";
        }
    }

    public class Transport{
        private String Connection;

        private String Host;

        public String getConnection() {
            return Connection;
        }

        public void setConnection(String connection) {
            Connection = connection;
        }

        public String getHost() {
            return Host;
        }

        public void setHost(String host) {
            Host = host;
        }

        public Transport(){
            this.Connection = "keep-alive";
            this.Host = "www.kuaidi100.com";
        }
    }

}
