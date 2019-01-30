package com.jsj.member.ob.dto.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author ljy
 * @create 2018-10-31
 * @desc 请求头
 */
@ApiModel
public class RequestHead {
    /**
     * 请求方式 0 未设置 10 web 20 Wap 30 IOS  40  Android 50 WxApp
     */
    @ApiModelProperty(value = "请求方式 0 未设置 10 web 20 Wap 30 IOS  40  Android 50 WxApp",required = true)
    private int sourceWay;

    /**
     * 来源ID
     */
    @ApiModelProperty(value = "请求来源渠道 300 空铁",required = true)
    private int sourceAppID;

    public int getSourceWay() {
        return sourceWay;
    }

    public void setSourceWay(int sourceWay) {
        this.sourceWay = sourceWay;
    }

    public int getSourceAppID() {
        return sourceAppID;
    }

    public void setSourceAppID(int sourceAppID) {
        this.sourceAppID = sourceAppID;
    }
}
