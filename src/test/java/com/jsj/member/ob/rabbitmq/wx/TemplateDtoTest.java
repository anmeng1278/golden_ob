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
import com.jsj.member.ob.logic.DictLogic;
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
        Map map = new HashMap();
        map.put("title","您的plus权益已开通成功，即刻生效!\n");
        map.put("effectiveDate",DateUtils.formatDateByUnixTime(Long.parseLong(deliveryDto.getCreateTime() + ""), "yyyy-MM-dd"));
/*
        map.put("title","正在为您开卡，请您耐心等待!\n");
        map.put("effectiveDate",DateUtils.formatDateByUnixTime(Long.parseLong(deliveryDto.getEffectiveDate() + ""), "yyyy-MM-dd"));*/
        TemplateDto dto = TemplateDto.OpenCardConfirm(deliveryDto,map);
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
        TemplateDto dto = TemplateDto.VerifySuccessed(delivery, requ);
        wxSender.sendNormal(dto);
    }

    @Test
    public void handleDelivery() {

        Delivery delivery = deliveryService.selectById(48);
        List<StockDto> stockDtos = StockLogic.GetStocks(delivery.getOpenId());

        Map map = new HashMap();

        //给微信接收者发送待处理发货模板
        if (delivery.getTypeId() == DeliveryType.DISTRIBUTE.getValue()) {
            String address = DictLogic.GetDict(delivery.getProvinceId()).getDictName()+DictLogic.GetDict(delivery.getCityId()).getDictName()+DictLogic.GetDict(delivery.getDistrictId()).getDictName()+delivery.getAddress();
            map.put("title", String.format("客户已申请配送,请尽快安排发货!\n\n客户姓名：%s\n手机号码：%s\n详细地址：%s", delivery.getContactName(), delivery.getMobile(),address));
            map.put("productName", "超级大螃蟹");
        }
      /*  if (delivery.getTypeId() == DeliveryType.PICKUP.getValue()) {
            //自提时间
            String pickUpDate = DateUtils.formatDateByUnixTime(Long.parseLong(delivery.getCreateTime() + ""), "yyyy-MM-dd hh:mm:ss");
            map.put("title", String.format("客户已申请自提，请核对并确认客户自提网点及时间，尽快安排相关对接工作!\n\n客户姓名：%s\n手机号码：%s\n自提时间：%s\n自提网点：%s", delivery.getContactName(), delivery.getMobile(), pickUpDate, delivery.getAirportName()));
            map.put("productName", "超级大螃蟹");
        }*/
        if(delivery.getPropertyTypeId() == PropertyType.NATION.getValue()){

            //生效时间
            String effectiveDate = DateUtils.formatDateByUnixTime(Long.parseLong(delivery.getEffectiveDate() + ""), "yyyy-MM-dd");
            map.put("title",String.format("客户已申请开卡,请核对开卡信息!\n\n客户姓名：%s\n身份证号：%s\n手机号码：%s\n生效时间：%s",delivery.getContactName(),delivery.getIdNumber(),delivery.getMobile(),effectiveDate));
            DeliveryDto deliveryDto = DeliveryLogic.GetDelivery(delivery.getDeliveryId());
            map.put("productName",deliveryDto.getProductDtos().get(0).getProductName());

        }

        List<Wechat> wechats = WechatLogic.GetNotifyWechat();
        for (Wechat wechat : wechats) {
            TemplateDto temp2 = TemplateDto.HandleDelivery(wechat, map, stockDtos);
            wxSender.sendNormal(temp2);
            System.out.println(temp2.getToUser());
        }


    }

    @Test
    public void deliverySuccessed() {

        DeliveryDto deliveryDto = DeliveryLogic.GetDelivery(60);
        TemplateDto dto = TemplateDto.OpenCardSuccess(deliveryDto);
        wxSender.sendNormal(dto);

        Delivery delivery = deliveryService.selectById(72);
        List<StockDto> stockDtos = DeliveryLogic.GetDeliveryStock(73);
        Map map = TemplateDto.GetProduct(stockDtos);
        TemplateDto temp = TemplateDto.DeliverySuccessed(delivery, map, stockDtos);
        wxSender.sendNormal(temp);


    }


}