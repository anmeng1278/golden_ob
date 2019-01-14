package com.jsj.member.ob.rabbitmq.wx;

import com.jsj.member.ob.App;
import com.jsj.member.ob.dto.api.delivery.DeliveryDto;
import com.jsj.member.ob.dto.api.delivery.VerifyActivityCodeRequ;
import com.jsj.member.ob.dto.api.stock.StockDto;
import com.jsj.member.ob.entity.Delivery;
import com.jsj.member.ob.entity.Order;
import com.jsj.member.ob.entity.Wechat;
import com.jsj.member.ob.enums.DeliveryType;
import com.jsj.member.ob.enums.PropertyType;
import com.jsj.member.ob.logic.DeliveryLogic;
import com.jsj.member.ob.logic.StockLogic;
import com.jsj.member.ob.logic.WechatLogic;
import com.jsj.member.ob.service.DeliveryService;
import com.jsj.member.ob.service.OrderService;
import com.jsj.member.ob.utils.DateUtils;
import com.jsj.member.ob.utils.Md5Utils;
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

        Order order = orderService.selectById(10279);
        Map map = new HashMap();
        map.put("productName", "超级大螃蟹");
        TemplateDto dto = TemplateDto.NewOrderPaySuccessed(order, map);
        wxSender.sendNormal(dto);

    }

    @Test
    public void cancelUnPayOrder() {

        Order order = orderService.selectById(10356);
        Map map = new HashMap();
        map.put("productName", "超级大螃蟹");
        TemplateDto dto = TemplateDto.CancelUnPayOrder(order, map);
        wxSender.sendNormal(dto);
    }

    @Test
    public void qrcodeUseSuccessed() {
        Delivery delivery = deliveryService.selectById(60);
        List<StockDto> stockDtos = StockLogic.GetStocks(delivery.getOpenId());
        TemplateDto dto = TemplateDto.QrcodeUseSuccessed(delivery, stockDtos);
        wxSender.sendNormal(dto);
    }

    @Test
    public void entityUseSuccessed() {
        Delivery delivery = deliveryService.selectById(60);
        List<StockDto> stockDtos = StockLogic.GetStocks(delivery.getOpenId());
        Map map = new HashMap();
        map.put("productName", "超级大螃蟹");
        TemplateDto dto = TemplateDto.EntityUseSuccessed(delivery, map, stockDtos);
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

    @Test
    public void verifySuccessed() {
        Delivery delivery = deliveryService.selectById(72);
        VerifyActivityCodeRequ requ = new VerifyActivityCodeRequ();
        String activityCode = "JSYXe47494507426";
        int timeStamp = DateUtils.getCurrentUnixTime();
        String sign = String.format("%s%dJSYX", activityCode, timeStamp);
        sign = Md5Utils.MD5(sign).toLowerCase();
        requ.setSign(sign);
        requ.setVipHallName("RLK");
        requ.setAirportName("巴彦淖尔机场");
        TemplateDto dto = TemplateDto.VerifySuccessed(delivery,requ );
        wxSender.sendNormal(dto);
    }

    @Test
    public void handleDelivery() {

        Delivery delivery = deliveryService.selectById(49);
        List<StockDto> stockDtos = StockLogic.GetStocks(delivery.getOpenId());

        Map map = new HashMap();
        if (delivery.getTypeId() == DeliveryType.DISTRIBUTE.getValue()) {
            map.put("title","客户已申请配送,请尽快安排发货!\n");
            map.put("productName", "超级大螃蟹");
        }
        if (delivery.getTypeId() == DeliveryType.PICKUP.getValue()) {
            map.put("title","客户已申请自提,请核对自提信息!\n");
            map.put("productName", "超级大螃蟹");
        }
        if(delivery.getPropertyTypeId() == PropertyType.NATION.getValue()){
            map.put("title","客户已申请开卡,请核对开卡信息!\n");
            DeliveryDto deliveryDto = DeliveryLogic.GetDelivery(delivery.getDeliveryId());
            map.put("productName",deliveryDto.getProductDtos().get(0).getProductName());
        }

        List<Wechat> wechats = WechatLogic.GetNotifyWechat();
        for (Wechat wechat : wechats) {
            TemplateDto temp2 = TemplateDto.HandleDelivery(wechat, map, stockDtos);
            wxSender.sendNormal(temp2);
        }
    }

    @Test
    public void deliverySuccessed() {
        Delivery delivery = deliveryService.selectById(72);
        List<StockDto> stockDtos = DeliveryLogic.GetDeliveryStock(73);
        Map map = TemplateDto.GetProduct(stockDtos);
        TemplateDto temp = TemplateDto.DeliverySuccessed(delivery,map, stockDtos);
        wxSender.sendNormal(temp);
    }


}