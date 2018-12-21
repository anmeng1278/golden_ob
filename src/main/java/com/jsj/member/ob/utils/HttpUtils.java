package com.jsj.member.ob.utils;

import com.jsj.member.ob.config.Webconfig;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class HttpUtils {

    public static HttpUtils httpUtils;

    @Autowired
    Webconfig webconfig;

    // 初始化的时候，将本类中的sysConfigManager赋值给静态的本类变量
    @PostConstruct
    public void init() {
        httpUtils = this;
        httpUtils.webconfig = this.webconfig;
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
     * 发送json请求
     * @param url
     * @param json
     * @param methodName
     * @return
     */
    public static String json(String url, String json, String methodName) {
        String result = null;
        BufferedReader reader = null;
        try {

            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            //注：这里的Content-Type必须是"application/json"
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("MethodName", methodName);
            byte[] writeBytes = json.getBytes();
            connection.setRequestProperty("Content-Length", String.valueOf(writeBytes.length));
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(json.getBytes());
            outputStream.flush();
            outputStream.close();

            if (connection.getResponseCode() == 200) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                result = reader.readLine();
            }
        } catch (Exception ex) {

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;

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
     *
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

    /**
     * 上传图片
     *
     * @param contents
     * @return
     */
    public static Map<String, Object> uploadImg(byte[] contents) {

        Pattern pattern = Pattern.compile("<a\\s*href=\\\"\\/(?<md5>[^\\\"]*)\\\"");
        URI uri = URI.create(httpUtils.webconfig.getImgServerURL());

        OkHttpClient mOkHttpClent = new OkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("img", "a.jpg",
                        RequestBody.create(MediaType.parse("image/png"), contents));

        RequestBody requestBody = builder.build();

        Request request = new Request.Builder()
                .url(uri.toString())
                .post(requestBody)
                .build();

        HashMap<String, Object> map = new HashMap<>();

        try {
            Response response = mOkHttpClent.newCall(request).execute();//得到Response 对象
            String str = response.body().string();
            String port = (uri.getPort() == -1 || uri.getPort() == 80) ? "" : ":" + uri.getPort() + "";

            Matcher matcher = pattern.matcher(str);
            boolean rs = matcher.find();
            if (rs) {
                String md5 = matcher.group("md5").toString();

                map.put("Issuccess", true);
                map.put("MD5", md5);
                map.put("Address", String.format("http://%s%s/", uri.getHost(), port));
                map.put("Url", String.format("http://%s%s/%s", uri.getHost(), port, md5));

            } else {
                map.put("Issuccess", false);
                map.put("ErrorMessage", "上传失败");
            }

        } catch (IOException e) {
            e.printStackTrace();

            map.put("Issuccess", false);
            map.put("ErrorMessage", "上传失败");
        }

        return map;

    }
}
