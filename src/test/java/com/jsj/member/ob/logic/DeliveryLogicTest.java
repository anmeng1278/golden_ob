package com.jsj.member.ob.logic;

import com.jsj.member.ob.App;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
public class DeliveryLogicTest {

    @Test
    public void getDeliveryOrder() {
        DeliveryLogic.GetDeliveryStock(1);
    }
}