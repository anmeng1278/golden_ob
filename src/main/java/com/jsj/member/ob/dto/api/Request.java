package com.jsj.member.ob.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;

/**
 * @author ljy
 * @create 2018-10-31
 * @desc 请求参数
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel
public class Request<T> {
    /**
     * 请求头
     */
    @Valid
    @ApiModelProperty(value = "请求头",required = true)
    private RequestHead requestHead;

    /**
     * 请求体
     */
    @Valid
    @ApiModelProperty(value = "请求体",required = true)
    private T requestBody;

    public RequestHead getRequestHead() {
        return requestHead;
    }

    public void setRequestHead(RequestHead requestHead) {
        this.requestHead = requestHead;
    }

    public T getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(T requestBody) {
        this.requestBody = requestBody;
    }
}
