package com.jsj.member.ob.logic;

import com.jsj.member.ob.App;
import com.jsj.member.ob.dto.api.delivery.DeliveryDto;
import com.jsj.member.ob.dto.api.stock.StockDto;
import com.jsj.member.ob.rabbitmq.wx.TemplateDto;
import com.jsj.member.ob.rabbitmq.wx.WxSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
public class DeliveryLogicTest {
    @Autowired
    WxSender wxSender;

    @Test
    public void getDeliveryOrder() {
        DeliveryLogic.GetDeliveryStock(2);
    }

    @Test
    public void getMyDelivery(){
        List<DeliveryDto> deliveryDtos = DeliveryLogic.GetDelivery("111");
        deliveryDtos.stream().forEach(s->s.getDeliveryId());
    }

    @Test
    public void test(){
        List<StockDto> stockDtos = DeliveryLogic.GetDeliveryStock(1);

        Map map = TemplateDto.GetProduct(stockDtos);
        System.out.println(map.get("productPrice"));
    }
}