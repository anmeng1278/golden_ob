package com.jsj.member.ob.logic;

import com.alibaba.fastjson.JSON;
import com.jsj.member.ob.App;
import com.jsj.member.ob.dto.BaseResp;
import com.jsj.member.ob.dto.http.BaseRequest;
import com.jsj.member.ob.dto.http.RequestBody;
import com.jsj.member.ob.dto.http.RequestHead;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
public class HttpUtilLogicTest {


    Map<String ,String> map = new LinkedHashMap<>();


    //获取 access_token
    @Test
    public void getAccessToken(){
        String url = "http://s5.jsjinfo.cn/awkmanage/v3/ktapi/GetAccessToken";
        BaseRequest baseRequest = new BaseRequest();
        RequestHead requestHead = new RequestHead();
        RequestBody requestBody = new RequestBody();

        requestHead.setTimeStamp("1111");
        requestHead.setSourceFrom("JSJ");
        map.put("TimeStamp","1111");
        map.put("SourceFrom","JSJ");
        requestHead.setSign(HttpUtilLogic.GetMd5(map,"#wugf543sxcv5*$#").toLowerCase());


        baseRequest.setRequestBody(requestBody);
        baseRequest.setRequestHead(requestHead);
        String s = HttpUtilLogic.DoPost(url, JSON.toJSONString(baseRequest), "application/json");
        System.out.println(s);
    }


    //获取 jsapi_ticket
    @Test
    public void getJsapiTicket(){
        String url = "http://s5.jsjinfo.cn/awkmanage/v3/ktapi/GetJsApiTicket";
        BaseRequest baseRequest = new BaseRequest();
        RequestHead requestHead = new RequestHead();
        RequestBody requestBody = new RequestBody();

        requestBody.setAccessToken("8_3vsXBiKJmPaceCK4vf2byiMmS86yDj__jYlLCsyNtluQ0RaFND-pITIu3gRtS\n" +
                "BBnXlcUuvUT3A0RRRjjLO5jIvHZm1UdZ4NPbPO7uN9KDFaC4C3bQNXuKumOtpWmdCgc\n" +
                "21-drAjqXFFHlqo6YZNdACASTH");

        requestHead.setTimeStamp("1111");
        requestHead.setSourceFrom("JSJ");
        map.put("TimeStamp","1111");
        map.put("SourceFrom","JSJ");
        map.put("AccessToken",requestBody.getAccessToken());
        requestHead.setSign(HttpUtilLogic.GetMd5(map,"#wugf543sxcv5*$#").toLowerCase());


        baseRequest.setRequestBody(requestBody);
        baseRequest.setRequestHead(requestHead);
        String s = HttpUtilLogic.DoPost(url, JSON.toJSONString(baseRequest), "application/json");
        System.out.println(s);
    }


    //通过code获取网页授权access_token
    @Test
    public void getAccessTokenByCode(){
        String url = "http://s5.jsjinfo.cn/awkmanage/v3/ktapi/GetWeChatAccessToken";
        BaseRequest baseRequest = new BaseRequest();
        RequestHead requestHead = new RequestHead();
        RequestBody requestBody = new RequestBody();

        requestBody.setCode("021AxV5w1vUDw90vTY5w991I2S5w1AxV5x");

        requestHead.setTimeStamp("1111");
        requestHead.setSourceFrom("JSJ");

        map.put("TimeStamp","1111");
        map.put("SourceFrom","JSJ");
        map.put("Code",requestBody.getCode());
        requestHead.setSign(HttpUtilLogic.GetMd5(map,"#wugf543sxcv5*$#").toLowerCase());

        baseRequest.setRequestBody(requestBody);
        baseRequest.setRequestHead(requestHead);
        String s = HttpUtilLogic.DoPost(url, JSON.toJSONString(baseRequest), "application/json");
        System.out.println(s);
    }

    //微信公众号支付
    @Test
    public void getPay(){
        String url = "http://zs5.jsjinfo.cn/v5/ParTrade.ashx";
        BaseRequest baseRequest = new BaseRequest();
        RequestHead requestHead = new RequestHead();
        RequestBody requestBody = new RequestBody();

        requestBody.setPlatformAppId("wssccaf9-2341-1234-87c1-6ababc10aab3");
        requestBody.setPlatformToken("wsscr6zfppd8v46b5678f8fxh2xdhv0d22xlx08h8btn6ttxxx4aab3");
        requestBody.setSourceWay("20");
        requestBody.setSourceApp("600");
        requestBody.setPayMethod("22");
        requestBody.setOutTradeId("9534634332");
        requestBody.setPayAmount("0.1");
        requestBody.setOpenId("oeQDZtzvDoD-0mTmyJH6rGAX8odc");
        requestBody.setOrderTimeOut("20180328173342");


        requestHead.setTimeStamp("1111");
        requestHead.setSourceFrom("JSJ");
        map.put("TimeStamp","1111");
        map.put("SourceFrom","JSJ");
        map.put("PlatformAppId",requestBody.getPlatformAppId());
        map.put("PlatformToken",requestBody.getPlatformToken());
        map.put("SourceWay",requestBody.getSourceWay());
        map.put("SourceApp",requestBody.getSourceApp());
        map.put("PayMethod",requestBody.getPayMethod());
        map.put("OutTradeId",requestBody.getOutTradeId());
        map.put("PayAmount",requestBody.getPayAmount());
        map.put("OpenId",requestBody.getOpenId());
        map.put("OrderTimeOut",requestBody.getOrderTimeOut());
        requestHead.setSign(HttpUtilLogic.GetMd5(map,"#wugf543sxcv5*$#"));


        baseRequest.setRequestBody(requestBody);
        baseRequest.setRequestHead(requestHead);
        String s = HttpUtilLogic.DoPost(url, JSON.toJSONString(baseRequest), "application/json");
        System.out.println(s);
    }


    @Test
    public void payInfo(){
        BaseRequest baseRequest = new BaseRequest();
        baseRequest.setTradeStatus("0000");
        baseRequest.setMessage("SUCCESS");
        baseRequest.setOutTradeId("4333451");
        baseRequest.setTradeAmount("0.20");
        baseRequest.setTradeOrderId("984_43334515");
        baseRequest.setTradeBillNo("2018030221001004080206230140");
        baseRequest.setTradeTime("2018/3/2 10:00:39");
        baseRequest.setTimeStamp("1522233465");

        map.put("TradeStatus",baseRequest.getTradeStatus());
        map.put("Message",baseRequest.getMessage());
        map.put("OutTradeId",baseRequest.getOutTradeId());
        map.put("TradeAmount",baseRequest.getTradeAmount());
        map.put("TradeOrderId",baseRequest.getTradeOrderId());
        map.put("TradeBillNo",baseRequest.getTradeBillNo());
        map.put("TradeTime",baseRequest.getTradeTime());
        map.put("TimeStamp",baseRequest.getTimeStamp());

        baseRequest.setSign(HttpUtilLogic.GetMd5(map,"#wugf543sxcv5*$#"));
    }
}