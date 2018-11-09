package com.jsj.member.ob.dto.api.express;

import com.alibaba.fastjson.annotation.JSONField;
import sun.management.resources.agent;

public class ExpressBirdHeader {

    public ExpressBirdHeader(){
        this.accept = "*/*";

        this.connection = "Keep-Alive";

        this.user_agent = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)";

        this.Content_Type = "application/x-www-form-urlencoded";
    }

    @JSONField(name="")
    private String accept;

    private String connection;

    @JSONField(name="user-agent")
    private String user_agent;

    @JSONField(name="Content-Type")
    private String Content_Type;

    public String getAccept() {
        return accept;
    }

    public void setAccept(String accept) {
        this.accept = accept;
    }

    public String getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public String getUser_agent() {
        return user_agent;
    }

    public void setUser_agent(String user_agent) {
        this.user_agent = user_agent;
    }

    public String getContent_Type() {
        return Content_Type;
    }

    public void setContent_Type(String content_Type) {
        Content_Type = content_Type;
    }
}
