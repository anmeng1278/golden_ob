package com.jsj.member.ob.logic;

import com.jsj.member.ob.App;
import com.jsj.member.ob.enums.SourceType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
public class WechatLogicTest {

    @Test
    public void bindRelation() {

        WechatLogic.BindRelation("oeQDZt6hGZQDY0gUtw2FwNSVTYFw", "oG5Z_4iYeizOQSJ8za5qM0FMseQA", SourceType.AWKMINI);

    }

    @Test
    public void bindJsjId(){
        WechatLogic.BindJSJId("oeQDZt-rcgi9QhWm6F7o2mV3dSYY", 222);
    }
}