package com.jsj.member.ob.rabbitmq.wx;

import com.jsj.member.ob.App;
import com.jsj.member.ob.dto.api.delivery.DeliveryDto;
import com.jsj.member.ob.dto.api.stock.StockDto;
import com.jsj.member.ob.entity.Delivery;
import com.jsj.member.ob.entity.Order;
import com.jsj.member.ob.logic.DeliveryLogic;
import com.jsj.member.ob.logic.StockLogic;
import com.jsj.member.ob.service.DeliveryService;
import com.jsj.member.ob.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
public class TemplateDtoTest {

    @Autowired
    OrderService orderService;
    @Autowired
    WxSender wxSender;

    @Autowired
    DeliveryService deliveryService;

    @Test
    public void newOrderPaySuccessed() {

        Order order = orderService.selectById(10036);
        Map map = new HashMap();
        map.put("productName","超级大螃蟹");
        TemplateDto dto = TemplateDto.NewOrderPaySuccessed(order, map);
        wxSender.sendNormal(dto);

    }

    @Test
    public void cancelUnPayOrder() {

        Order order = orderService.selectById(10356);
        Map map = new HashMap();
        map.put("productName","超级大螃蟹");
        TemplateDto dto = TemplateDto.CancelUnPayOrder(order, map);
        wxSender.sendNormal(dto);
    }

    @Test
    public void qrcodeUseSuccessed() {
        Delivery delivery = deliveryService.selectById(60);
        List<StockDto> stockDtos = StockLogic.GetStocks(delivery.getOpenId());
        TemplateDto dto = TemplateDto.QrcodeUseSuccessed(delivery,stockDtos);
        wxSender.sendNormal(dto);
    }

    @Test
    public void entityUseSuccessed() {
        Delivery delivery = deliveryService.selectById(60);
        List<StockDto> stockDtos = StockLogic.GetStocks(delivery.getOpenId());
        Map map = new HashMap();
        map.put("productName","超级大螃蟹");
        TemplateDto dto = TemplateDto.EntityUseSuccessed(delivery,map,stockDtos);
        wxSender.sendNormal(dto);
    }

    @Test
    public void openCardConfirm() {
        DeliveryDto deliveryDto = DeliveryLogic.GetDelivery(60);
        TemplateDto dto = TemplateDto.OpenCardConfirm(deliveryDto);
        wxSender.sendNormal(dto);
    }

    @Test
    public void openCardSuccess() {
        DeliveryDto deliveryDto = DeliveryLogic.GetDelivery(60);
        TemplateDto dto = TemplateDto.OpenCardSuccess(deliveryDto);
        wxSender.sendNormal(dto);
    }
}