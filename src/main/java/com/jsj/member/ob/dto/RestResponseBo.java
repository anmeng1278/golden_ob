package com.jsj.member.ob.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * rest返回对象
 *^
 * @param <T>
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class RestResponseBo<T> {


    /**
     * 服务器响应数据
     */
    private T datas;

    public BaseResp getBaseResp() {
        return baseResp;
    }

    public void setBaseResp(BaseResp baseResp) {
        this.baseResp = baseResp;
    }

    /**
     * 基础响应
     */
    private  BaseResp baseResp;


    public RestResponseBo() {
        this.baseResp = new BaseResp();
    }


    public RestResponseBo(boolean success, T datas) {
        this.datas = datas;
    }


    public T getDatas() {
        return datas;
    }

    public void setDatas(T datas) {
        this.datas = datas;
    }


    @SuppressWarnings("rawtypes")
	public static RestResponseBo ok() {
    	RestResponseBo resp = new RestResponseBo();
        resp.getBaseResp().setSuccess(true);
    	return resp;
    }
    
    @SuppressWarnings("rawtypes")
	public static RestResponseBo ok(String msg) {
    	RestResponseBo resp = new RestResponseBo();
        resp.getBaseResp().setSuccess(true);
        resp.getBaseResp().setMessage(msg);
    	return resp;
    }

    public static RestResponseBo ok(String msg, String url) {
        RestResponseBo resp = new RestResponseBo();
        resp.getBaseResp().setSuccess(true);
        resp.getBaseResp().setMessage(msg);
        resp.getBaseResp().setUrl(url);
        return resp;
    }


    public static  <T>  RestResponseBo ok(T data) {
        RestResponseBo resp = new RestResponseBo();
        resp.getBaseResp().setSuccess(true);
        resp.setDatas(data);
        return resp;
    }

    @SuppressWarnings("rawtypes")
	public static RestResponseBo fail(String msg) {
        RestResponseBo resp = new RestResponseBo();
        resp.getBaseResp().setMessage(msg);
        resp.getBaseResp().setCode(-999);
        return resp;
    }

    public static  <T>  RestResponseBo fail(String msg,T data) {
        RestResponseBo resp = new RestResponseBo();
        resp.getBaseResp().setMessage(msg);
        resp.getBaseResp().setCode(-999);
        resp.setDatas(data);
        return resp;
    }

}