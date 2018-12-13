package com.jsj.member.ob.logic;

import com.jsj.member.ob.App;
import com.jsj.member.ob.dto.api.delivery.DeliveryDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
public class DeliveryLogicTest {

    @Test
    public void getDeliveryOrder() {
        DeliveryLogic.GetDeliveryStock(2);
    }

    @Test
    public void getMyDelivery(){
        List<DeliveryDto> deliveryDtos = DeliveryLogic.GetDelivery("111");
        deliveryDtos.stream().forEach(s->s.getDeliveryId());
    }
}