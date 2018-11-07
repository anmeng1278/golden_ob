package com.jsj.member.ob.logic;

import com.jsj.member.ob.App;
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
    public void getText() {
    }

    @Test
    public void getExpress() throws IOException {
        ExpressRequ requ = new ExpressRequ();
        requ.setText("9893656693848");
        ExpressResp resp = ExpressApiLogic.GetExpress(requ);
        System.out.println(resp);
    }
}