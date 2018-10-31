package com.jsj.member.ob.logic;

import com.alibaba.fastjson.JSON;
import com.jsj.member.ob.App;
import com.jsj.member.ob.dto.wechatApi.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

//
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
public class wechatApiLogicTest {

    //获取token
    @Test
    public void getAccessToken() {

        GetAccessTokenRequ requ = new GetAccessTokenRequ();
        GetAccessTokenResp resp = WechatApiLogic.GetAccessToken(requ);
        System.out.println(JSON.toJSONString(resp));
    }

    //获取ticket
    @Test
    public void getJsApiTicket() {

        //获取accessToken
        GetAccessTokenRequ tokenRequ = new GetAccessTokenRequ();
        GetAccessTokenResp tokenResp = WechatApiLogic.GetAccessToken(tokenRequ);
        String accessToken = tokenResp.getResponseBody().getAccessToken();

        GetJsApiTicketRequ ticketRequ = new GetJsApiTicketRequ();
        ticketRequ.getRequestBody().setAccessToken(accessToken);
        GetJsApiTicketResp ticketResp = WechatApiLogic.GetJsApiTicket(ticketRequ);
        System.out.println(JSON.toJSONString(ticketResp));
    }


    //通过code获取网页授权access_token
    @Test
    public void getWeChatAccessToken() {


        GetWeChatAccessTokenRequ weChatRequ = new GetWeChatAccessTokenRequ();
        weChatRequ.getRequestBody().setCode("021AxV5w1vUDw90vTY5w991I2S5w1AxV5x");

        GetWeChatAccessTokenResp weChatResp = WechatApiLogic.GetWeChatAccessToken(weChatRequ);
        System.out.println(JSON.toJSONString(weChatResp));
    }

    @Test
    public void getPayTrade() {


        GetPayTradeRequ requ = new GetPayTradeRequ();
        requ.getRequestBody().setPlatformAppId(WechatApiLogic.wechatApiLogic.webconfig.getPlatformAppId());
        requ.getRequestBody().setPlatformToken(WechatApiLogic.wechatApiLogic.webconfig.getPlatformToken());
        requ.getRequestBody().setOpenId("oeQDZtzvDoD-0mTmyJH6rGAX8odc");
        requ.getRequestBody().setSourceWay("20");
        requ.getRequestBody().setPayAmount("0.1");
        requ.getRequestBody().setPayMethod("22");
        requ.getRequestBody().setOutTradeId("9534634332");
        requ.getRequestBody().setOrderTimeOu("20180328173342");
        requ.getRequestBody().setSourceApp("600");

        GetPayTradeResp resp = WechatApiLogic.GetPayTrade(requ);
        System.out.println(JSON.toJSONString(resp));
    }

}