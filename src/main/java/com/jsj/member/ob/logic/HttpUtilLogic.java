package com.jsj.member.ob.logic;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.util.Base64Utils;
import org.springframework.util.DigestUtils;
import sun.misc.BASE64Encoder;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class HttpUtilLogic {

    public static HttpUtilLogic httpUtilLogic;

    @PostConstruct
    public void init() {
        httpUtilLogic = this;
    }

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


    public static String GetMd5(Map<String, String> params, String secret) {
        Map<String, String> map = new LinkedHashMap<>(params);
        StringBuffer sb = new StringBuffer();
        String result = "";
        try {
           for(String key:map.keySet()){
                sb.append(key+"=").append(map.get(key)).append("&");
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
