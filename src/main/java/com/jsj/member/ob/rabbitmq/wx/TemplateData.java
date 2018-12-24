package com.jsj.member.ob.rabbitmq.wx;

import java.io.Serializable;


public class TemplateData implements Serializable {

    public TemplateData(){

    }

    public TemplateData(String value, String color) {
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