package com.jsj.member.ob.logic;

import com.alibaba.fastjson.JSON;
import com.jsj.member.ob.App;
import com.jsj.member.ob.dto.BaseRequ;
import com.jsj.member.ob.dto.api.order.CreateOrderRequ;
import com.jsj.member.ob.dto.api.order.CreateOrderResp;
import com.jsj.member.ob.dto.api.order.OrderProduct;
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

        List<OrderProduct> orderProducts = new ArrayList<>();

        OrderProduct orderProduct1 = new OrderProduct();
        orderProduct1.setNumber(1);
        orderProduct1.setProductId(1);
        orderProduct1.setProductSizeId(1);
        //100

        OrderProduct orderProduct2 = new OrderProduct();
        orderProduct2.setNumber(5);
        orderProduct2.setProductId(2);
        orderProduct2.setProductSizeId(2);
        //30

        orderProducts.add(orderProduct1);
        orderProducts.add(orderProduct2);

        requ.setOrderProducts(orderProducts);
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

        List<OrderProduct> orderProducts = new ArrayList<>();

        OrderProduct orderProduct1 = new OrderProduct();
        orderProduct1.setNumber(1);
        orderProduct1.setProductId(1);
        orderProduct1.setProductSizeId(1);
        //100

        orderProducts.add(orderProduct1);

        requ.setOrderProducts(orderProducts);

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