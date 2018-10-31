package com.jsj.member.ob.logic;

import com.jsj.member.ob.App;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
public class HttpUtilLogicTest {


    Map<String ,String> map = new LinkedHashMap<>();


    //获取 access_token
    @Test
    public void getAccessToken() throws IOException {
        //String accessTokenUrl = wechatApiLogic.GetProperties().getProperty("accessTokenUrl");
        //String url = wechatApiLogic.httpUtilLogic.webconfig.getAccessTokenUrl();
        //String url = "http://s5.jsjinfo.cn/awkmanage/v3/ktapi/GetAccessToken";
        //wechatApiLogic.GetAccessToken(url);

    }


    //获取 jsapi_ticket
    @Test
    public void getJsapiTicket(){
        //String url = "http://s5.jsjinfo.cn/awkmanage/v3/ktapi/GetJsApiTicket";
        //wechatApiLogic.GetAccessToken(url);
    }


    //通过code获取网页授权access_token
    @Test
    public void getAccessTokenByCode(){
        //String url = "http://s5.jsjinfo.cn/awkmanage/v3/ktapi/GetWeChatAccessToken";
        //wechatApiLogic.GetAccessToken(url);
    }

    //微信公众号支付
    @Test
    public void getPay(){
        //String url = "http://zs5.jsjinfo.cn/v5/ParTrade.ashx";
        //wechatApiLogic.GetPayTrade(url,"9534634332");
    }


    @Test
    public void payInfo(){
        //BaseRequest baseRequest = new BaseRequest();
        //baseRequest.setTradeStatus("0000");
        //baseRequest.setMessage("SUCCESS");
        //baseRequest.setOutTradeId("4333451");
        //baseRequest.setTradeAmount("0.20");
        //baseRequest.setTradeOrderId("984_43334515");
        //baseRequest.setTradeBillNo("2018030221001004080206230140");
        //baseRequest.setTradeTime("2018/3/2 10:00:39");
        //baseRequest.setTimeStamp("1522233465");
        //
        //map.put("TradeStatus",baseRequest.getTradeStatus());
        //map.put("Message",baseRequest.getMessage());
        //map.put("OutTradeId",baseRequest.getOutTradeId());
        //map.put("TradeAmount",baseRequest.getTradeAmount());
        //map.put("TradeOrderId",baseRequest.getTradeOrderId());
        //map.put("TradeBillNo",baseRequest.getTradeBillNo());
        //map.put("TradeTime",baseRequest.getTradeTime());
        //map.put("TimeStamp",baseRequest.getTimeStamp());
        //
        //baseRequest.setSign(wechatApiLogic.GetMd5(map,"#wugf543sxcv5*$#"));
    }
}