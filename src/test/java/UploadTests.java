import com.alibaba.fastjson.JSON;
import okhttp3.*;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UploadTests {


    @Test
    public void testUploads() throws IOException {

        String url = "http://img.jsjinfo.cn/upload";

        OkHttpClient mOkHttpClent = new OkHttpClient();
        File file = new File("c:/a.jpg");
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("img", "HeadPortrait.jpg",
                        RequestBody.create(MediaType.parse("image/png"), file));

        RequestBody requestBody = builder.build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();


        Response response = mOkHttpClent.newCall(request).execute();//得到Response 对象
        if (response.isSuccessful()) {
            System.out.println(response.body().string());
        }
        //    <head>
        //    <title>Upload Result</title>
        //    </head>
        //    <body>
        //    <h1>MD5: 0c1eea2c322bc3ca1fb26c0a304cbd36</h1>
        //                    Image upload successfully! You can get this image via this address:<br/><br/>
        //    <a href="/0c1eea2c322bc3ca1fb26c0a304cbd36">http://yourhostname:80/0c1eea2c322bc3ca1fb26c0a304cbd36</a>?w=width&h=height&g=isgray&x=position_x&y=position_y&r=rotate&q=quality&f=format
        //    </body>
        //    </html>

        //<html>


    }


    @Test
    public void TestRegex() {


        String url = "http://img.jsjinfo.cn/upload";

        String str = "    <head>\n" +
                "            <title>Upload Result</title>\n" +
                "            </head>\n" +
                "            <body>\n" +
                "            <h1>MD5: 0c1eea2c322bc3ca1fb26c0a304cbd36</h1>\n" +
                "                            Image upload successfully! You can get this image via this address:<br/><br/>\n" +
                "            <a href=\"/0c1eea2c322bc3ca1fb26c0a304cbd36\">http://yourhostname:80/0c1eea2c322bc3ca1fb26c0a304cbd36</a>?w=width&h=height&g=isgray&x=position_x&y=position_y&r=rotate&q=quality&f=format\n" +
                "            </body>\n" +
                "            </html>\n" +
                "\n" +
                "        <html>";

        System.out.println(str);

        Pattern pattern = Pattern.compile("<a\\s*href=\\\"\\/(?<md5>[^\\\"]*)\\\"");
        Matcher matcher = pattern.matcher(str);
        // 查找字符串中是否有匹配正则表达式的字符/字符串
        boolean rs = matcher.find();

        System.out.println(rs);
        System.out.println(matcher.group("md5").toString());


        HashMap<String, Object> map = new HashMap<>();

        URI uri = URI.create(url);

        String port = (uri.getPort() == -1 || uri.getPort() == 80) ? "" : ":" + uri.getPort() + "";

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

        System.out.println(JSON.toJSONString(map));

    }
}
