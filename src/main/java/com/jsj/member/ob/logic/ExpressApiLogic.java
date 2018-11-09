package com.jsj.member.ob.logic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jsj.member.ob.config.Webconfig;
import com.jsj.member.ob.dto.api.express.ExpressBirdRequ;
import com.jsj.member.ob.dto.api.express.ExpressBirdResp;
import com.jsj.member.ob.dto.api.express.ExpressRequ;
import com.jsj.member.ob.dto.api.express.ExpressResp;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.utils.HttpUtils;
import com.jsj.member.ob.utils.Md5Utils;
import org.apache.commons.collections4.map.LinkedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.*;

import static com.jsj.member.ob.logic.ThirdPartyLogic.thirdPartyLogic;

@Component
public class ExpressApiLogic {

    private static ExpressApiLogic expressApiLogic;

    @Autowired
    Webconfig webconfig;

    @PostConstruct
    public void init() {
        expressApiLogic = this;
        this.webconfig = expressApiLogic.webconfig;
    }


    /**
     * 快递100获取快递物流信息
     *
     * @param requ
     * @return
     * @throws IOException
     */
    public static ExpressResp GetExpressHundred(ExpressRequ requ) throws IOException {
        if (StringUtils.isBlank(requ.getText())) {
            throw new TipException("快递单号不能为空！");
        }
        ExpressResp resp = new ExpressResp();

        String getText = GetText(requ.getText());

        // 通过快递公司及快递单号获取物流信息。
        String url = String.format("http://www.kuaidi100.com/query?type=" + getText + "&postid=" + requ.getText() + "&temp=" + new Random().nextDouble());

        String expressResult = HttpUtils.get(url);
        resp = JSON.parseObject(expressResult, ExpressResp.class);
        return resp;
    }


    /**
     * 快递鸟获取快递物流信息
     * @param
     * @return
     * @throws IOException
     */
    public static ExpressBirdResp GetExpressBird(ExpressBirdRequ requ) throws Exception{

        ExpressBirdResp resp = new ExpressBirdResp();

        String requestData = requ.toString();

        LinkedMap<String, String> map = new LinkedMap<>();

        map.put("RequestData", URLEncoder.encode(requestData, "UTF-8"));
        map.put("EBusinessID", expressApiLogic.webconfig.getEBusinessID());
        map.put("RequestType", "1002");

        String dataSign=encrypt(requestData, expressApiLogic.webconfig.getAppKey(), "UTF-8");
        map.put("DataSign",  URLEncoder.encode((dataSign), "UTF-8"));

       map.put("DataType", "2");

       // String form = sendPost(expressApiLogic.webconfig.getReqURL(), map);
        String form = HttpUtils.form(expressApiLogic.webconfig.getReqURL(), map);

        resp = JSON.parseObject(form, ExpressBirdResp.class);

        return resp;
    }

    /**
     * 快递100获取是哪家快递
     *
     * @param text
     * @return
     */
    public static String GetText(String text) throws IOException {
        //ExpressTypeResp resp = new ExpressTypeResp();
        String type = null;
        if (StringUtils.isBlank(text)) {
            return null;
        }
        //通过快递单号获得是哪家快递
        String url = String.format("http://www.kuaidi100.com/autonumber/autoComNum?text=" + text);

        String resultType = HttpUtils.get(url);

        JSONObject typeJsonObject = (JSONObject) JSON.parse(resultType);
        JSONArray typeDataArray = (JSONArray) typeJsonObject.get("auto");
        if (typeDataArray.isEmpty()) {
            return null;
        }
        JSONObject typeObject = (JSONObject) typeDataArray.get(0);
        type = typeObject.getString("comCode");
        return type;
    }



    /**
     * base64编码
     * @param str 内容
     * @param charset 编码方式
     * @throws UnsupportedEncodingException
     */
    private static String base64(String str, String charset) throws UnsupportedEncodingException {
        String encoded = base64Encode(str.getBytes(charset));
        return encoded;
    }

    /**
     * 电商Sign签名生成
     * @param content 内容
     * @param keyValue Appkey
     * @param charset 编码方式
     * @throws UnsupportedEncodingException ,Exception
     * @return DataSign签名
     */
    @SuppressWarnings("unused")
    private static String encrypt (String content, String keyValue, String charset) throws UnsupportedEncodingException, Exception
    {
        if (keyValue != null)
        {
            return base64(Md5Utils.MD5(content + keyValue), charset);
        }
        return base64(Md5Utils.MD5(content), charset);
    }

    private static char[] base64EncodeChars = new char[] {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
            'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
            'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
            'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
            'w', 'x', 'y', 'z', '0', '1', '2', '3',
            '4', '5', '6', '7', '8', '9', '+', '/' };

    public static String base64Encode(byte[] data) {
        StringBuffer sb = new StringBuffer();
        int len = data.length;
        int i = 0;
        int b1, b2, b3;
        while (i < len) {
            b1 = data[i++] & 0xff;
            if (i == len)
            {
                sb.append(base64EncodeChars[b1 >>> 2]);
                sb.append(base64EncodeChars[(b1 & 0x3) << 4]);
                sb.append("==");
                break;
            }
            b2 = data[i++] & 0xff;
            if (i == len)
            {
                sb.append(base64EncodeChars[b1 >>> 2]);
                sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
                sb.append(base64EncodeChars[(b2 & 0x0f) << 2]);
                sb.append("=");
                break;
            }
            b3 = data[i++] & 0xff;
            sb.append(base64EncodeChars[b1 >>> 2]);
            sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
            sb.append(base64EncodeChars[((b2 & 0x0f) << 2) | ((b3 & 0xc0) >>> 6)]);
            sb.append(base64EncodeChars[b3 & 0x3f]);
        }
        return sb.toString();
    }



}
