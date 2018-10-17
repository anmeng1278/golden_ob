package com.jsj.member.ob.logic;

import com.alibaba.fastjson.JSON;
import com.jsj.member.ob.App;
import com.jsj.member.ob.dto.BaseRequ;
import com.jsj.member.ob.dto.api.order.CreateOrderRequ;
import com.jsj.member.ob.dto.api.order.CreateOrderResp;
import com.jsj.member.ob.dto.api.order.OrderProductDto;
import com.jsj.member.ob.enums.OrderType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
public class OrderLogicTest {

    /**
     * 创建普通订单
     */
    @Test
    public void createNormalOrder() {

        CreateOrderRequ requ = new CreateOrderRequ();

        BaseRequ baseRequ = new BaseRequ();
        baseRequ.setOpenId("111111");

        requ.setBaseRequ(baseRequ);
        requ.setRemarks("备注信息");
        requ.setOrderType(OrderType.NORMAL);

        List<OrderProductDto> orderProductDtos = new ArrayList<>();

        OrderProductDto orderProductDto1 = new OrderProductDto();
        orderProductDto1.setNumber(1);
        orderProductDto1.setProductId(1);
        orderProductDto1.setProductSizeId(1);
        //100

        OrderProductDto orderProductDto2 = new OrderProductDto();
        orderProductDto2.setNumber(5);
        orderProductDto2.setProductId(2);
        orderProductDto2.setProductSizeId(2);
        //30

        orderProductDtos.add(orderProductDto1);
        orderProductDtos.add(orderProductDto2);

        requ.setOrderProductDtos(orderProductDtos);
        requ.setWechatCouponId(2);

        CreateOrderResp createOrderResp = OrderLogic.CreateOrder(requ);
        System.out.println(JSON.toJSONString(createOrderResp));

        Assert.assertEquals(true, true);

    }


    /**
     * 创建秒杀订单
     */
    @Test
    public void createSeckillOrder() {

        CreateOrderRequ requ = new CreateOrderRequ();

        BaseRequ baseRequ = new BaseRequ();
        baseRequ.setOpenId("111111");

        requ.setBaseRequ(baseRequ);
        requ.setRemarks("备注信息");
        requ.setOrderType(OrderType.SECKILL);

        List<OrderProductDto> orderProductDtos = new ArrayList<>();

        OrderProductDto orderProductDto1 = new OrderProductDto();
        orderProductDto1.setNumber(1);
        orderProductDto1.setProductId(1);
        orderProductDto1.setProductSizeId(1);
        //100

        orderProductDtos.add(orderProductDto1);

        requ.setOrderProductDtos(orderProductDtos);

        CreateOrderResp createOrderResp = OrderLogic.CreateOrder(requ);
        System.out.println(JSON.toJSONString(createOrderResp));

        Assert.assertEquals(true, true);

    }

    /**
     * 创建团 订单
     */
    @Test
    public void createTeamOrder() {

        CreateOrderRequ requ = new CreateOrderRequ();

        requ.setRemarks("备注信息");
        requ.setOrderType(OrderType.TEAM);
        requ.setTeamOrderId(1);


        CreateOrderResp createOrderResp = OrderLogic.CreateOrder(requ);
        System.out.println(JSON.toJSONString(createOrderResp));

        Assert.assertEquals(true, true);

    }
}