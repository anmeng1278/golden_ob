import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class testAwk {


    @Test
    public void test() {

        //测试环境地址
        //String url = "http://106.38.39.180/awk/Third/ThirdApi/API2Partner.aspx";
        //String url = "http://172.16.6.134/awk/Third/ThirdApi/API2Partner.aspx";

        String url = "https://s5.jsjinfo.cn/third/v1/ThirdApi/API2Partner.aspx";

        //请求方法名
        String methodName = "_GetActivityCode";

        //合作商登录密码
        //String PartnerLoginName = "guangxiyinlian";
        //String PartnerLoginPassword = "gxylceshi";

        String PartnerLoginName = "ylgxnx";
        String PartnerLoginPassword = "ylgxnx_123";


        HashMap<String, Object> map = new HashMap<>();

        map.put("PartnerLoginName", PartnerLoginName);
        map.put("PartnerLoginPassword", PartnerLoginPassword);

        //获取活动码数量
        map.put("Count", 1);

        //请求报文
        String json = JSON.toJSONString(map);

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

        System.out.println(result);

        //响应数据
        //{"BaseResponse":{"IsSuccess":true,"ErrorMessage":null,"ErrorCode":null,"Exception":null},"ActivityCodes":["GZYL93d864507390"],"ExpiredDate":"2019-01-17T10:53:16.3075547+08:00"}


    }


    @Test
    public void tet2() throws ParseException {
        String r = "{\"BaseResponse\":{\"IsSuccess\":true,\"ErrorMessage\":null,\"ErrorCode\":null,\"Exception\":null},\"ActivityCodes\":[\"GZYL93d864507390\"],\"ExpiredDate\":\"2019-01-17T10:53:16.3075547+08:00\"}";

        Res res = JSON.parseObject(r, Res.class);

        System.out.println(res.getActivityCodes());
        System.out.println(dealDateFormat(res.getExpiredDate()));
    }

    //{"BaseResponse":{"IsSuccess":true,"ErrorMessage":null,"ErrorCode":null,"Exception":null},"ActivityCodes":["GZYL93d864507390"],"ExpiredDate":"2019-01-17T10:53:16.3075547+08:00"}

    public static String dealDateFormat(String oldDateStr) throws ParseException {
        //此格式只有  jdk 1.7才支持  yyyy-MM-dd'T'HH:mm:ss.SSSXXX
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");  //yyyy-MM-dd'T'HH:mm:ss.SSSZ
        Date date = df.parse(oldDateStr);
        SimpleDateFormat df1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
        Date date1 = df1.parse(date.toString());
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//  Date date3 =  df2.parse(date1.toString());
        return df2.format(date1);
    }

}

