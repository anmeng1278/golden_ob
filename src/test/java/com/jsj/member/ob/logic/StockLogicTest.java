package com.jsj.member.ob.logic;

import com.jsj.member.ob.App;
import com.jsj.member.ob.dto.BaseRequ;
import com.jsj.member.ob.dto.api.stock.GetMyStockRequ;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;



@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
public class StockLogicTest {

    @Test
    public void getMyStock() {
        GetMyStockRequ requ = new GetMyStockRequ();
        BaseRequ baseRequ = new BaseRequ();
        baseRequ.setOpenId("111");
        requ.setBaseRequ(baseRequ);

        StockLogic.GetMyStock(requ);
    }
}