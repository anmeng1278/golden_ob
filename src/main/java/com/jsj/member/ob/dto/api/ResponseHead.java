package com.jsj.member.ob.dto.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 响应基类
 */
@ApiModel
public class ResponseHead {
    /**
     * 响应类型 0000请求成功 0001 请求失败
     */
    @ApiModelProperty(value = "响应类型 0000请求成功 0001 请求失败", required = true)
    private String code;

    /**
     * 响应消息
     */
    @ApiModelProperty(value = "响应消息", required = true)
    private String message;

    public ResponseHead() {
    }


    public ResponseHead(String code, String message) {
        this.code = code;
        this.message = message;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
