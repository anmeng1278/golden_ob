package com.jsj.member.ob.dto.thirdParty;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties({"firsT", "remark"})
public class TemplateDto {

    public TemplateDto() {
        this.datas = new ArrayList<>();
        this.data = new HashMap<>();
    }

    /**
     * 接收人
     */
    @JSONField(name = "touser")
    private String toUser;
    /**
     * 模板编号
     */
    @JSONField(name = "template_id")
    private String templateId;
    /**
     * 跳转地址
     */
    private String url;

    /**
     * 描述
     */
    @JSONField(serialize = false)
    private String first;

    /**
     * 颜色
     */
    @JSONField(serialize = false)
    private String firstColor;

    /**
     * 备注
     */
    @JSONField(serialize = false)
    private String remark;

    /**
     * 颜色
     */
    @JSONField(serialize = false)
    private String remarkColor;

    /**
     * 模板数据
     */
    private Map<String, Data> data;

    /**
     * 模板数据
     */
    @JSONField(serialize = false)
    private List<Data> datas;

    /**
     * 小程序
     */
    @JSONField(name = "miniprogram")
    private miniProgram miniProgram;

    private class miniProgram {

        /*
        小程序appid
         */
        private String appid;

        /**
         * 小程序pagepath
         */
        @JSONField(name = "pagepath")
        private String pagePath;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getPagePath() {
            return pagePath;
        }

        public void setPagePath(String pagePath) {
            this.pagePath = pagePath;
        }
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getFirstColor() {
        return firstColor;
    }

    public void setFirstColor(String firstColor) {
        this.firstColor = firstColor;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemarkColor() {
        return remarkColor;
    }

    public void setRemarkColor(String remarkColor) {
        this.remarkColor = remarkColor;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, Data> getData() {

        for (int i = 0; i < this.datas.size(); i++) {
            String key = String.format("keyword%d", i + 1);
            this.data.put(key, this.datas.get(i));
        }

        if (!StringUtils.isBlank(this.first)) {
            Data first = new Data(this.first, this.firstColor);
            this.data.put("first", first);
        }

        if (!StringUtils.isBlank(this.remark)) {
            Data remark = new Data(this.remark, this.remarkColor);
            this.data.put("remark", remark);
        }

        return data;
    }

    public List<Data> getDatas() {
        return datas;
    }

    public static class Data {

        public Data(String value, String color) {
            this.value = value;
            this.color = color;
        }

        /**
         * 值
         */
        private String value;

        /**
         * 颜色
         */
        private String color;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }
}
