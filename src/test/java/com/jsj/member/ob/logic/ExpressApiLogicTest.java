package com.jsj.member.ob.logic;

import com.jsj.member.ob.App;
import com.jsj.member.ob.dto.api.express.ExpressBirdRequ;
import com.jsj.member.ob.dto.api.express.ExpressBirdResp;
import com.jsj.member.ob.dto.api.express.ExpressRequ;
import com.jsj.member.ob.dto.api.express.ExpressResp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
public class ExpressApiLogicTest {

    @Test
    public void getExpress() throws IOException {
        ExpressRequ requ = new ExpressRequ();
        requ.setText("71428315216895");
        ExpressResp resp = ExpressApiLogic.GetExpressHundred(requ);
        System.out.println(resp);
    }

    @Test
    public void getExpressBird() throws Exception{

        ExpressBirdRequ requ = new ExpressBirdRequ();

        //订单号
       // requ.setOrderCode("");

        //快递公司代码
        requ.setShipperCode("YD");

        //物流单号
        requ.setLogisticCode("3861152096216");

        ExpressBirdResp resp = ExpressApiLogic.GetExpressBird(requ);
        System.out.println(resp);

    }
}