package com.jsj.member.ob.logic;

import com.alibaba.fastjson.JSON;
import com.jsj.member.ob.App;
import com.jsj.member.ob.dto.BaseRequ;
import com.jsj.member.ob.dto.api.order.CreateOrderRequ;
import com.jsj.member.ob.dto.api.order.CreateOrderResp;
import com.jsj.member.ob.dto.api.order.OrderProductDto;
import com.jsj.member.ob.dto.proto.NotifyModelOuterClass;
import com.jsj.member.ob.enums.ActivityType;
import com.jsj.member.ob.exception.ProductStockException;
import com.jsj.member.ob.logic.order.OrderBase;
import com.jsj.member.ob.logic.order.OrderFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
public class OrderLogicTest {

    /**
     * 创建普通订单
     */
    @Test
    public void createNormalOrder() {

        try {

            CreateOrderRequ requ = new CreateOrderRequ();

            BaseRequ baseRequ = new BaseRequ();
            baseRequ.setOpenId("111111");

            requ.setBaseRequ(baseRequ);
            requ.setRemarks("备注信息");
            requ.setActivityType(ActivityType.NORMAL);

            List<OrderProductDto> orderProductDtos = new ArrayList<>();

            OrderProductDto orderProductDto1 = new OrderProductDto();
            orderProductDto1.setNumber(1);
            orderProductDto1.setProductId(1);
            orderProductDto1.setProductSpecId(1);
            //100

            OrderProductDto orderProductDto2 = new OrderProductDto();
            orderProductDto2.setNumber(5);
            orderProductDto2.setProductId(2);
            orderProductDto2.setProductSpecId(2);
            //30

            orderProductDtos.add(orderProductDto1);
            orderProductDtos.add(orderProductDto2);

            requ.setOrderProductDtos(orderProductDtos);
            requ.setWechatCouponId(2);

            CreateOrderResp createOrderResp = OrderLogic.CreateOrder(requ);
            System.out.println(JSON.toJSONString(createOrderResp));

        } catch (ProductStockException e) {
            e.printStackTrace();
            System.out.println("商品编号：" + e.getProductId());
            System.out.println("规格编号：" + e.getProductSpecId());
            System.out.println("订单编号：" + e.getOrderId());
            System.out.println("使用数量：" + e.getNumber());
            System.out.println("当前库存：" + e.getStock());
            System.out.println("订单类型：" + e.getActivityType().getMessage());
        }

    }


    /**
     * 创建秒杀订单
     */
    @Test
    public void createSeckillOrder() {

        try {

            CreateOrderRequ requ = new CreateOrderRequ();

            BaseRequ baseRequ = new BaseRequ();
            baseRequ.setOpenId("333333");

            requ.setBaseRequ(baseRequ);
            requ.setRemarks("备注信息");
            requ.setActivityType(ActivityType.SECKILL);
            requ.setActivityId(1);

            OrderProductDto opd = new OrderProductDto();
            opd.setProductId(3);
            opd.setProductSpecId(4);
            requ.getOrderProductDtos().add(opd);

            CreateOrderResp createOrderResp = OrderLogic.CreateOrder(requ);
            System.out.println(JSON.toJSONString(createOrderResp));

        } catch (ProductStockException e) {
            e.printStackTrace();
            System.out.println("商品编号：" + e.getProductId());
            System.out.println("规格编号：" + e.getProductSpecId());
            System.out.println("订单编号：" + e.getOrderId());
            System.out.println("使用数量：" + e.getNumber());
            System.out.println("当前库存：" + e.getStock());
            System.out.println("订单类型：" + e.getActivityType().getMessage());
        }


    }

    /**
     * 创建团 订单
     */
    @Test
    public void createGrouponOrder() {

        CreateOrderRequ requ = new CreateOrderRequ();

        requ.setRemarks("备注信息");
        requ.setActivityType(ActivityType.GROUPON);
        requ.setActivityId(2);
        requ.setActivityOrderId(1);

        CreateOrderResp createOrderResp = OrderLogic.CreateOrder(requ);
        System.out.println(JSON.toJSONString(createOrderResp));

        Assert.assertEquals(true, true);

    }


    /**
     * 创建组合订单
     */
    @Test
    public void createCombinationOrder() {


        try {

            CreateOrderRequ requ = new CreateOrderRequ();
            BaseRequ baseRequ = new BaseRequ();
            baseRequ.setOpenId("44444");

            requ.setBaseRequ(baseRequ);
            requ.setRemarks("备注信息");
            requ.setActivityType(ActivityType.COMBINATION);
            requ.setActivityId(3);


            CreateOrderResp createOrderResp = OrderLogic.CreateOrder(requ);
            System.out.println(JSON.toJSONString(createOrderResp));

            Assert.assertEquals(true, true);

        } catch (ProductStockException e) {
            e.printStackTrace();
            System.out.println("商品编号：" + e.getProductId());
            System.out.println("规格编号：" + e.getProductSpecId());
            System.out.println("订单编号：" + e.getOrderId());
            System.out.println("使用数量：" + e.getNumber());
            System.out.println("当前库存：" + e.getStock());
            System.out.println("订单类型：" + e.getActivityType().getMessage());
        }

    }


    /**
     * 支付订单
     */
    @Test
    public void payOrder() {

        NotifyModelOuterClass.NotifyModel notifyModel = NotifyModelOuterClass.NotifyModel.getDefaultInstance();

        int orderId = 10113;

        OrderBase orderBase = OrderFactory.GetInstance(orderId);
        orderBase.PaySuccessed(orderId, notifyModel);

    }

    @Test
    public void geyMyOrder() {
        List<OrderProductDto> orderProductDtos = OrderLogic.GetOrderProducts(10177);

        Map<String, Object> map = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        for (OrderProductDto orderProductDto : orderProductDtos) {
            sb.append(orderProductDto.getProductDto().getProductName() +"*"+ orderProductDto.getNumber()).append(",");

        }
        map.put("prudctName",sb);
        System.out.println(map.get("prudctName"));

    }

}