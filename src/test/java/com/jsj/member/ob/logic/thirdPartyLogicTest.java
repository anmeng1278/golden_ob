package com.jsj.member.ob.logic;

import com.alibaba.fastjson.JSON;
import com.jsj.member.ob.App;
import com.jsj.member.ob.dto.thirdParty.*;
import com.jsj.member.ob.enums.GoldenCardType;
import com.jsj.member.ob.enums.SmsLevel;
import com.jsj.member.ob.rabbitmq.wx.TemplateData;
import com.jsj.member.ob.rabbitmq.wx.TemplateDto;
import com.jsj.member.ob.utils.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

//
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
public class thirdPartyLogicTest {

    //获取token
    @Test
    public void getAccessToken() {

        GetAccessTokenRequ requ = new GetAccessTokenRequ();
        GetAccessTokenResp resp = ThirdPartyLogic.GetAccessToken(requ);
        System.out.println(JSON.toJSONString(resp));
    }

    @Test
    public void getAccessTokenDev() {

        GetAccessTokenRequ requ = new GetAccessTokenRequ();
        GetAccessTokenResp resp = ThirdPartyLogic.GetAccessTokenDev(requ);
        System.out.println(JSON.toJSONString(resp));
    }

    //获取ticket
    @Test
    public void getJsApiTicket() {

        //获取accessToken
        GetAccessTokenRequ tokenRequ = new GetAccessTokenRequ();
        GetAccessTokenResp tokenResp = ThirdPartyLogic.GetAccessToken(tokenRequ);
        String accessToken = tokenResp.getResponseBody().getAccessToken();

        GetJsApiTicketRequ ticketRequ = new GetJsApiTicketRequ();
        ticketRequ.getRequestBody().setAccessToken(accessToken);
        GetJsApiTicketResp ticketResp = ThirdPartyLogic.GetJsApiTicket(ticketRequ);
        System.out.println(JSON.toJSONString(ticketResp));
    }


    //通过code获取网页授权access_token
    @Test
    public void getWeChatAccessToken() {


        GetWeChatAccessTokenRequ weChatRequ = new GetWeChatAccessTokenRequ();
        weChatRequ.getRequestBody().setCode("021AxV5w1vUDw90vTY5w991I2S5w1AxV5x");

        GetWeChatAccessTokenResp weChatResp = ThirdPartyLogic.GetWeChatAccessToken(weChatRequ);
        System.out.println(JSON.toJSONString(weChatResp));
    }

    @Test
    public void getPayTrade() {


        GetPayTradeRequ requ = new GetPayTradeRequ();
        requ.getRequestHead().setTimeStamp(DateUtils.getCurrentUnixTime() + "");
        requ.getRequestHead().setSourceFrom("KTGJ");

        requ.getRequestBody().setSourceWay("20");
        requ.getRequestBody().setSourceApp("600");
        requ.getRequestBody().setPayMethod("22");
        requ.getRequestBody().setOutTradeId("95346343321");
        requ.getRequestBody().setPayAmount("0.1");
        requ.getRequestBody().setOpenId("oeQDZtzvDoD-0mTmyJH6rGAX8odc");
        requ.getRequestBody().setOrderTimeOut("20181212175900");

        GetPayTradeResp resp = ThirdPartyLogic.GetPayTrade(requ);
        System.out.println(JSON.toJSONString(resp));

        System.out.println(resp.getResponseBody().getAppId());
        System.out.println(resp.getResponseBody().get_package());
        System.out.println(resp.getResponseBody().getNonceStr());
        System.out.println(resp.getResponseBody().getPaySign());
        System.out.println(resp.getResponseBody().getSignType());
        System.out.println(resp.getResponseBody().getTimeStamp());

    }


    @Test
    public void sendSms() {

        SmsDto smsDto = new SmsDto();
        smsDto.setMobile("15210860133");
        smsDto.setContents("您的登录验证码为：1101");
        smsDto.setSmsLevel(SmsLevel.IMPORTANCE);

        ThirdPartyLogic.SendSms(smsDto);

    }

    @Test
    public void sendWxTemplate() {

        TemplateDto dto = new TemplateDto();

        //dto.setToUser("o2JcesyPo-ogMyVD58egyvRfAfYg");
        dto.setToUser("o2JcesxmAIQWeqEEqA-vM-i44Miw");
        dto.setTemplateId("H4LO95pkHyQFN6QEGQSZ5eFcbQ5Wgutk8wuH4Nq0j3I");

        dto.getData().put("first", new TemplateData("今日关注", "#173177"));

        dto.setRemark("备注信息");
        dto.setRemarkColor("#173177");

        dto.getData().put("keyword1", new TemplateData("哈哈", "#173177"));
        ThirdPartyLogic.SendWxTemplate(dto);

    }

    @Test
    public void TestGetActivityCodes() {
        GetActivityCodesResp getActivityCodesResp = ThirdPartyLogic.GetActivityCodes(null);

        System.out.println(JSON.toJSONString(getActivityCodesResp));
    }

    @Test
    public void testOpenCard() {

        CreateGoldenCardRequ requ = new CreateGoldenCardRequ();

        requ.getRequestBody().setCardType(GoldenCardType.getApiCode(GoldenCardType.MONTH));
        requ.getRequestBody().setMemberIdNumber("210781198610161816");
        requ.getRequestBody().setMemberIDType("1");
        requ.getRequestBody().setMemberMobile("15210860133");
        requ.getRequestBody().setMemberName("张宁");
        requ.getRequestBody().setSalePrice("20");

        CreateGoldenCardResp resp = ThirdPartyLogic.CreateGoldenCard(requ);
        System.out.println(JSON.toJSONString(resp));

    }


}