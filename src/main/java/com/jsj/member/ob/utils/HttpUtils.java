package com.jsj.member.ob.utils;

import okhttp3.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

@Component
public class HttpUtils {

    public static HttpUtils httpUtils;

    // 初始化的时候，将本类中的sysConfigManager赋值给静态的本类变量
    @PostConstruct
    public void init() {
        httpUtils = this;
    }


    /**
     * 发送json请求
     *
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public static String json(String url, String json) throws IOException {

        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                , json);

        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
        Request request = new Request.Builder()
                .post(requestBody)
                .url(url)//请求接口。如果需要传参拼接到接口后面。
                .build();//创建Request 对象
        Response response = client.newCall(request).execute();//得到Response 对象
        if (response.isSuccessful()) {
            return response.body().string();
        }
        return "";
    }

    /**
     * 发送get请求
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String get(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
        Request request = new Request.Builder()
                .url(url)//请求接口。如果需要传参拼接到接口后面。
                .build();//创建Request 对象
        Response response = client.newCall(request).execute();//得到Response 对象
        if (response.isSuccessful()) {
            return response.body().string();
        }
        return "";
    }

    /**
     * 发送post请求
     *
     * @param url
     * @param map
     * @return
     * @throws IOException
     */
    public static String form(String url, Map<String, String> map) throws IOException {

        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        for (Map.Entry<String, String> entry : map.entrySet()) {
            formBody.add(entry.getKey(), entry.getValue());//传递键值对参数
        }

        Request request = new Request.Builder()//创建Request 对象。
                .url(url)
                .post(formBody.build())//传递请求体
                .build();

        Response response = client.newCall(request).execute();//得到Response 对象
        if (response.isSuccessful()) {
            return response.body().string();
        }
        return "";

    }

    /**
     * 二进制读取
     * @param request
     * @return
     */
    public static byte[] readAsBytes(HttpServletRequest request) {

        int len = request.getContentLength();
        byte[] buffer = new byte[len];
        ServletInputStream in = null;

        try {
            in = request.getInputStream();
            in.read(buffer, 0, len);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return buffer;
    }
}
