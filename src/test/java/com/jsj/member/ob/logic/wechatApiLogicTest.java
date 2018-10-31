package com.jsj.member.ob.logic;

import com.alibaba.fastjson.JSON;
import com.jsj.member.ob.App;
import com.jsj.member.ob.dto.wechatApi.GetAccessTokenRequ;
import com.jsj.member.ob.dto.wechatApi.GetAccessTokenResp;
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

    @Test
    public void getAccessToken() {

        GetAccessTokenRequ requ = new GetAccessTokenRequ();
        GetAccessTokenResp resp = WechatApiLogic.GetAccessToken(requ);
        System.out.println(JSON.toJSONString(resp));
    }

    @Test
    public void testService() {
        GetAccessTokenRequ requ = new GetAccessTokenRequ();
        requ.getRequestHead().setSign("123");

        System.out.println(JSON.toJSONString(requ));
    }
}