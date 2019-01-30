package com.jsj.member.ob.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author ljy
 * @create 2018-10-31
 * @desc 响应
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel
public class Response<T> {
    /**
     * 响应头
     */
    @ApiModelProperty(value = "响应头",required = true)
    private ResponseHead responseHead;

    /**
     * 响应体
     */
    @ApiModelProperty(value = "响应体",required = true)
    private T responseBody;

    public ResponseHead getResponseHead() {
        return responseHead;
    }

    public void setResponseHead(ResponseHead responseHead) {
        this.responseHead = responseHead;
    }

    public T getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(T responseBody) {
        this.responseBody = responseBody;
    }

    public Response(ResponseHead responseHead, T responseBody) {
        this.responseHead = responseHead;
        this.responseBody = responseBody;
    }

    public Response(ResponseHead responseHead) {
        this(responseHead, null);
    }

    /**
     * 返回成功
     *
     * @param responseBody
     * @param <T>
     * @return
     */
    public static <T> Response ok(T responseBody) {
        return new Response(new ResponseHead("0000", ""), responseBody);
    }

    /**
     * 返回成功
     *
     * @return
     */
    public static Response ok() {
        return new Response(new ResponseHead("0000", ""));
    }

    /**
     * 返回失败
     *
     * @param errorMessage
     * @return
     */
    public static Response fail(String errorMessage) {
        return new Response(new ResponseHead("0001", errorMessage));
    }

    /**
     * 返回失败
     *
     * @param errorMessage
     * @return
     */
    public static Response fail(String errorMessage, String errorCode) {
        return new Response(new ResponseHead(errorCode, errorMessage));
    }
}
