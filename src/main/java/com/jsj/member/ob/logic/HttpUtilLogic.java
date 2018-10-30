package com.jsj.member.ob.logic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jsj.member.ob.config.Webconfig;
import com.jsj.member.ob.dto.http.BaseRequest;
import com.jsj.member.ob.dto.http.RequestBody;
import com.jsj.member.ob.dto.http.RequestHead;
import com.jsj.member.ob.entity.Order;
import com.jsj.member.ob.utils.HttpUtils;
import com.jsj.member.ob.utils.Md5Utils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.*;

public class HttpUtilLogic {

    public static HttpUtilLogic httpUtilLogic;

    @Autowired
    Webconfig webconfig;

    @PostConstruct
    public void init() {
        httpUtilLogic = this;
        httpUtilLogic.webconfig = this.webconfig;
    }

    static BaseRequest baseRequest = null;
    static RequestHead requestHead = null;
    static RequestBody requestBody = null;
    static Map<String, String> map = null;


    //获取 access_token
    public static String GetAccessToken(String url) {
        String result = "";
        try {
            if (url.equals(HttpUtilLogic.GetProperties().getProperty("accessTokenUrl"))) {

                BaseRequest baseRequest = HttpUtilLogic.GetBaseRequest(1);

            } else if (url.equals(HttpUtilLogic.GetProperties().getProperty("jsApiTicketUrl"))) {

                BaseRequest baseRequest = HttpUtilLogic.GetBaseRequest(2);

            } else if (url.equals(HttpUtilLogic.GetProperties().getProperty("weChatAccessTokenUrl"))) {

                BaseRequest baseRequest = HttpUtilLogic.GetBaseRequest(3);

            } else if (url.equals(HttpUtilLogic.GetProperties().getProperty("payTradeUrl"))) {

                BaseRequest baseRequest = HttpUtilLogic.GetBaseRequest(4);

            }
            result = HttpUtilLogic.DoPost(url, JSON.toJSONString(baseRequest), "application/json");
            System.out.println(result);
        } catch (Exception e) {
            return null;
        }
        return result;
    }

    //微信公众号支付
    public static String GetPayTrade(String url, String orderId) {
        baseRequest = new BaseRequest();
        requestHead = new RequestHead();
        requestBody = new RequestBody();
        map = new LinkedHashMap<>();
        String result = "";

        Order order = OrderLogic.orderLogic.orderService.selectOne(new EntityWrapper<Order>().where("order_id={0}", orderId));
        try {

            Properties prop = HttpUtilLogic.GetProperties();
            requestHead.setTimeStamp(prop.getProperty("TimeStamp"));
            requestHead.setSourceFrom(prop.getProperty("SourceFrom"));

            requestBody.setPlatformAppId(prop.getProperty("PlatformAppId"));
            requestBody.setPlatformToken(prop.getProperty("PlatformToken"));
            requestBody.setSourceWay(prop.getProperty("SourceWay"));
            requestBody.setSourceApp(prop.getProperty("SourceApp"));
            requestBody.setPayMethod(prop.getProperty("PayMethod"));
            requestBody.setOutTradeId(orderId);
            requestBody.setPayAmount(String.valueOf(order.getPayAmount()));
            requestBody.setOpenId(order.getOpenId());
            requestBody.setOrderTimeOut(String.valueOf(order.getExpiredTime()));

            map.put("TimeStamp", prop.getProperty("TimeStamp"));
            map.put("SourceFrom", prop.getProperty("SourceFrom"));
            map.put("PlatformAppId", requestBody.getPlatformAppId());
            map.put("PlatformToken", requestBody.getPlatformToken());
            map.put("SourceWay", requestBody.getSourceWay());
            map.put("SourceApp", requestBody.getSourceApp());
            map.put("PayMethod", requestBody.getPayMethod());
            map.put("OutTradeId", orderId);
            map.put("PayAmount", String.valueOf(order.getPayAmount()));
            map.put("OpenId", order.getOpenId());
            map.put("OrderTimeOut", String.valueOf(order.getExpiredTime()));

            requestHead.setSign(HttpUtilLogic.GetMd5(map, prop.getProperty("token")).toLowerCase());
            baseRequest.setRequestBody(requestBody);
            baseRequest.setRequestHead(requestHead);

            result = HttpUtilLogic.DoPost(url, JSON.toJSONString(baseRequest), "application/json");
            System.out.println(result);
        } catch (Exception e) {
            return null;
        }
        return result;
    }


    /**
     * 获得配置文件
     *
     * @return
     * @throws IOException
     */
    public static Properties GetProperties() throws IOException {
        String fileName = "application.yml";
        Properties prop = new Properties();// 属性集合对象
        //获取文件流
        InputStream fis = HttpUtilLogic.class.getClassLoader().getResourceAsStream(fileName);
        prop.load(fis);
        fis.close();
        return prop;
    }


    /**
     * 获得请求体对象
     *
     * @return
     * @throws IOException
     */
    public static BaseRequest GetBaseRequest(int type) throws IOException {
        baseRequest = new BaseRequest();
        requestHead = new RequestHead();
        requestBody = new RequestBody();
        map = new LinkedHashMap<>();
        Properties prop = HttpUtilLogic.GetProperties();
        requestHead.setTimeStamp(prop.getProperty("TimeStamp"));
        requestHead.setSourceFrom(prop.getProperty("SourceFrom"));

        if (type == 1) {
            map.put("TimeStamp", prop.getProperty("TimeStamp"));
            map.put("SourceFrom", prop.getProperty("SourceFrom"));
        }
        if (type == 2) {
            map.put("TimeStamp", prop.getProperty("TimeStamp"));
            map.put("SourceFrom", prop.getProperty("SourceFrom"));
            map.put("AccessToken", prop.getProperty("AccessToken"));
            requestBody.setAccessToken(prop.getProperty("AccessToken"));
        }
        if (type == 3) {
            map.put("TimeStamp", prop.getProperty("TimeStamp"));
            map.put("SourceFrom", prop.getProperty("SourceFrom"));
            map.put("Code", prop.getProperty("Code"));
            requestBody.setCode(prop.getProperty("Code"));
        }

        requestHead.setSign(HttpUtilLogic.GetMd5(map, prop.getProperty("token")).toLowerCase());
        baseRequest.setRequestBody(requestBody);
        baseRequest.setRequestHead(requestHead);
        return baseRequest;
    }

    /**
     * 处理post请求
     *
     * @param url
     * @param params
     * @param contentType
     * @return
     */
    public static String DoPost(String url, String params, String contentType) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);// 创建httpPost
        httpPost.setHeader("Content-Type", contentType);
        String charSet = "UTF-8";
        StringEntity entity = new StringEntity(params, charSet);
        httpPost.setEntity(entity);
        CloseableHttpResponse response = null;
        String jsonString = null;
        try {
            response = httpclient.execute(httpPost);
            StatusLine status = response.getStatusLine();
            int state = status.getStatusCode();
            if (state == HttpStatus.SC_OK) {
                HttpEntity responseEntity = response.getEntity();
                jsonString = EntityUtils.toString(responseEntity);
            }
            if (response != null) {
                response.close();
            }
            httpclient.close();
        } catch (Exception e) {
            jsonString = null;
        }
        return jsonString;
    }

    /**
     * md5加密
     *
     * @param params
     * @param secret
     * @return
     */
    public static String GetMd5(Map<String, String> params, String secret) {
        Map<String, String> map = new LinkedHashMap<>(params);
        StringBuffer sb = new StringBuffer();
        String result = "";
        try {
            for (String key : map.keySet()) {
                sb.append(key + "=").append(map.get(key)).append("&");
            }
            sb.append(secret);
            MessageDigest md = MessageDigest.getInstance("md5");
            byte[] digest = md.digest(sb.toString().getBytes());

            result = DigestUtils.md5DigestAsHex(new String(sb).getBytes());
           /* BASE64Encoder base64 = new BASE64Encoder();
            result = base64.encode(md.digest(sb.toString().getBytes()));*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
