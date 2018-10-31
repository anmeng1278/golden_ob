package com.jsj.member.ob.logic;

import com.alibaba.fastjson.JSON;
import com.jsj.member.ob.config.Webconfig;
import com.jsj.member.ob.dto.wechatApi.GetAccessTokenRequ;
import com.jsj.member.ob.dto.wechatApi.GetAccessTokenResp;
import com.jsj.member.ob.utils.HttpUtils;
import com.jsj.member.ob.utils.Md5Utils;
import org.apache.commons.collections4.map.LinkedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
public class WechatApiLogic {

    public static WechatApiLogic wechatApiLogic;

    @Autowired
    Webconfig webconfig;

    @PostConstruct
    public void init() {
        wechatApiLogic = this;
        wechatApiLogic.webconfig = this.webconfig;
    }

    public static GetAccessTokenResp GetAccessToken(GetAccessTokenRequ requ) {

        GetAccessTokenResp resp = new GetAccessTokenResp();

        try {

            System.out.println( wechatApiLogic.webconfig.getJsApiTicketUrl());
            System.out.println( wechatApiLogic.webconfig.getImgServerURL());

            LinkedMap<String, String> map = new LinkedMap<>();
            map.put("TimeStamp", requ.getRequestHead().getTimeStamp());
            map.put("SourceFrom", requ.getRequestHead().getSourceFrom());

            String sign = WechatApiLogic.GetMd5str(map, "#wugf543sxcv5*$#");
            sign = Md5Utils.MD5(sign);
            requ.getRequestHead().setSign(sign);

            //设置签名
            String result = HttpUtils.json("http://s5.jsjinfo.cn/awkmanage/v3/ktapi/GetAccessToken", JSON.toJSONString(requ));
            resp = JSON.parseObject(result, GetAccessTokenResp.class);

        } catch (Exception ex) {
            resp.getResponseHead().setCode("0001");
            resp.getResponseHead().setMessage(ex.getMessage());
        }

        return resp;

    }


    //static BaseRequest baseRequest = null;
    //static RequestHead requestHead = null;
    //static RequestBody requestBody = null;
    //static Map<String, String> map = null;
    //
    //
    ////获取 access_token
    //public static String GetAccessToken(String url) {
    //    String result = "";
    //    try {
    //        if (url.equals(wechatApiLogic.GetProperties().getProperty("accessTokenUrl"))) {
    //
    //            BaseRequest baseRequest = wechatApiLogic.GetBaseRequest(1);
    //
    //        } else if (url.equals(wechatApiLogic.GetProperties().getProperty("jsApiTicketUrl"))) {
    //
    //            BaseRequest baseRequest = wechatApiLogic.GetBaseRequest(2);
    //
    //        } else if (url.equals(wechatApiLogic.GetProperties().getProperty("weChatAccessTokenUrl"))) {
    //
    //            BaseRequest baseRequest = wechatApiLogic.GetBaseRequest(3);
    //
    //        } else if (url.equals(wechatApiLogic.GetProperties().getProperty("payTradeUrl"))) {
    //
    //            BaseRequest baseRequest = wechatApiLogic.GetBaseRequest(4);
    //
    //        }
    //        result = wechatApiLogic.DoPost(url, JSON.toJSONString(baseRequest), "application/json");
    //        System.out.println(result);
    //    } catch (Exception e) {
    //        return null;
    //    }
    //    return result;
    //}
    //
    ////微信公众号支付
    //public static String GetPayTrade(String url, String orderId) {
    //    baseRequest = new BaseRequest();
    //    requestHead = new RequestHead();
    //    requestBody = new RequestBody();
    //    map = new LinkedHashMap<>();
    //    String result = "";
    //
    //    Order order = OrderLogic.orderLogic.orderService.selectOne(new EntityWrapper<Order>().where("order_id={0}", orderId));
    //    try {
    //
    //        Properties prop = wechatApiLogic.GetProperties();
    //        requestHead.setTimeStamp(prop.getProperty("TimeStamp"));
    //        requestHead.setSourceFrom(prop.getProperty("SourceFrom"));
    //
    //        requestBody.setPlatformAppId(prop.getProperty("PlatformAppId"));
    //        requestBody.setPlatformToken(prop.getProperty("PlatformToken"));
    //        requestBody.setSourceWay(prop.getProperty("SourceWay"));
    //        requestBody.setSourceApp(prop.getProperty("SourceApp"));
    //        requestBody.setPayMethod(prop.getProperty("PayMethod"));
    //        requestBody.setOutTradeId(orderId);
    //        requestBody.setPayAmount(String.valueOf(order.getPayAmount()));
    //        requestBody.setOpenId(order.getOpenId());
    //        requestBody.setOrderTimeOut(String.valueOf(order.getExpiredTime()));
    //
    //        map.put("TimeStamp", prop.getProperty("TimeStamp"));
    //        map.put("SourceFrom", prop.getProperty("SourceFrom"));
    //        map.put("PlatformAppId", requestBody.getPlatformAppId());
    //        map.put("PlatformToken", requestBody.getPlatformToken());
    //        map.put("SourceWay", requestBody.getSourceWay());
    //        map.put("SourceApp", requestBody.getSourceApp());
    //        map.put("PayMethod", requestBody.getPayMethod());
    //        map.put("OutTradeId", orderId);
    //        map.put("PayAmount", String.valueOf(order.getPayAmount()));
    //        map.put("OpenId", order.getOpenId());
    //        map.put("OrderTimeOut", String.valueOf(order.getExpiredTime()));
    //
    //        requestHead.setSign(wechatApiLogic.GetMd5(map, prop.getProperty("token")).toLowerCase());
    //        baseRequest.setRequestBody(requestBody);
    //        baseRequest.setRequestHead(requestHead);
    //
    //        result = wechatApiLogic.DoPost(url, JSON.toJSONString(baseRequest), "application/json");
    //        System.out.println(result);
    //    } catch (Exception e) {
    //        return null;
    //    }
    //    return result;
    //}
    //
    //
    ///**
    // * 获得配置文件
    // *
    // * @return
    // * @throws IOException
    // */
    //public static Properties GetProperties() throws IOException {
    //    String fileName = "application.yml";
    //    Properties prop = new Properties();// 属性集合对象
    //    //获取文件流
    //    InputStream fis = wechatApiLogic.class.getClassLoader().getResourceAsStream(fileName);
    //    prop.load(fis);
    //    fis.close();
    //    return prop;
    //}
    //
    //
    ///**
    // * 获得请求体对象
    // *
    // * @return
    // * @throws IOException
    // */
    //public static BaseRequest GetBaseRequest(int type) throws IOException {
    //    baseRequest = new BaseRequest();
    //    requestHead = new RequestHead();
    //    requestBody = new RequestBody();
    //    map = new LinkedHashMap<>();
    //    Properties prop = wechatApiLogic.GetProperties();
    //    requestHead.setTimeStamp(prop.getProperty("TimeStamp"));
    //    requestHead.setSourceFrom(prop.getProperty("SourceFrom"));
    //
    //    if (type == 1) {
    //        map.put("TimeStamp", prop.getProperty("TimeStamp"));
    //        map.put("SourceFrom", prop.getProperty("SourceFrom"));
    //    }
    //    if (type == 2) {
    //        map.put("TimeStamp", prop.getProperty("TimeStamp"));
    //        map.put("SourceFrom", prop.getProperty("SourceFrom"));
    //        map.put("AccessToken", prop.getProperty("AccessToken"));
    //        requestBody.setAccessToken(prop.getProperty("AccessToken"));
    //    }
    //    if (type == 3) {
    //        map.put("TimeStamp", prop.getProperty("TimeStamp"));
    //        map.put("SourceFrom", prop.getProperty("SourceFrom"));
    //        map.put("Code", prop.getProperty("Code"));
    //        requestBody.setCode(prop.getProperty("Code"));
    //    }
    //
    //    requestHead.setSign(wechatApiLogic.GetMd5(map, prop.getProperty("token")).toLowerCase());
    //    baseRequest.setRequestBody(requestBody);
    //    baseRequest.setRequestHead(requestHead);
    //    return baseRequest;
    //}
    //
    ///**
    // * 处理post请求
    // *
    // * @param url
    // * @param params
    // * @param contentType
    // * @return
    // */
    //public static String DoPost(String url, String params, String contentType) {
    //    CloseableHttpClient httpclient = HttpClients.createDefault();
    //    HttpPost httpPost = new HttpPost(url);// 创建httpPost
    //    httpPost.setHeader("Content-Type", contentType);
    //    String charSet = "UTF-8";
    //    StringEntity entity = new StringEntity(params, charSet);
    //    httpPost.setEntity(entity);
    //    CloseableHttpResponse response = null;
    //    String jsonString = null;
    //    try {
    //        response = httpclient.execute(httpPost);
    //        StatusLine status = response.getStatusLine();
    //        int state = status.getStatusCode();
    //        if (state == HttpStatus.SC_OK) {
    //            HttpEntity responseEntity = response.getEntity();
    //            jsonString = EntityUtils.toString(responseEntity);
    //        }
    //        if (response != null) {
    //            response.close();
    //        }
    //        httpclient.close();
    //    } catch (Exception e) {
    //        jsonString = null;
    //    }
    //    return jsonString;
    //}
    //
    ///**
    // * md5加密
    // *
    // * @param params
    // * @param secret
    // * @return
    // */
    private static String GetMd5str(LinkedMap<String, String> map, String secret) {
        StringBuilder sb = new StringBuilder();
        try {
            for (String key : map.keySet()) {
                sb.append(key + "=").append(map.get(key)).append("&");
            }
            sb.append(secret);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
