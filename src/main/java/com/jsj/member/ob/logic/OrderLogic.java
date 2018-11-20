package com.jsj.member.ob.logic;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jsj.member.ob.dto.api.order.CreateOrderRequ;
import com.jsj.member.ob.dto.api.order.CreateOrderResp;
import com.jsj.member.ob.dto.api.order.OrderProductDto;
import com.jsj.member.ob.entity.Order;
import com.jsj.member.ob.entity.OrderProduct;
import com.jsj.member.ob.enums.OrderStatus;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.order.OrderBase;
import com.jsj.member.ob.logic.order.OrderFactory;
import com.jsj.member.ob.service.OrderProductService;
import com.jsj.member.ob.service.OrderService;
import com.jsj.member.ob.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderLogic extends BaseLogic {

    public static OrderLogic orderLogic;

    // 初始化的时候，将本类中的sysConfigManager赋值给静态的本类变量
    @PostConstruct
    public void init() {
        orderLogic = this;
    }

    @Autowired
    OrderService orderService;

    @Autowired
    OrderProductService orderProductService;

    /**
     * 创建订单，普通订单、团单、秒杀单、
     *
     * @param requ
     * @return
     */
    public static CreateOrderResp CreateOrder(CreateOrderRequ requ) {

        //参数校验
        if (requ.getActivityType() == null) {
            throw new TipException("活动类型不能为空");
        }

        OrderBase orderBase = OrderFactory.GetInstance(requ.getActivityType());
        return orderBase.CreateOrder(requ);

    }


    /**
     * 获取订单商品列表
     *
     * @param orderId
     * @return
     */
    public static List<OrderProductDto> GetOrderProducts(int orderId) {

        List<OrderProductDto> orderProductDtos = new ArrayList<>();
        List<OrderProduct> orderProducts = orderLogic.orderProductService.selectList(new EntityWrapper<OrderProduct>().where("order_id={0}", orderId));

        orderProducts.forEach(op -> {

            OrderProductDto dto = new OrderProductDto();
            dto.setNumber(op.getNumber());
            dto.setProductSpecId(op.getProductSpecId());
            dto.setProductId(op.getProductId());

            orderProductDtos.add(dto);
        });

        return orderProductDtos;
    }

    /**
     * 获取订单数
     *
     * @param opendId
     * @return
     */
    public static Integer GetOrderCount(String opendId) {

        if (StringUtils.isBlank(opendId)) {
            return 0;
        }

        EntityWrapper<Order> wrapper = new EntityWrapper<>();
        wrapper.where("open_id={0}", opendId);

        return orderLogic.orderService.selectCount(wrapper);

    }

    /**
     * 取消订单
     *
     * @return
     */
    public static List<Integer> CancelOrder() {

        EntityWrapper<Order> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("status={0}", OrderStatus.UNPAY.getValue());
        entityWrapper.where("expired_time < UNIX_TIMESTAMP()");

        List<Order> orders = orderLogic.orderService.selectList(entityWrapper);

        for (Order o : orders) {
            //修改状态
            o.setStatus(OrderStatus.CANCEL.getValue());

            //恢复库存
            List<OrderProductDto> orderProductDtos = OrderLogic.GetOrderProducts(o.getOrderId());
            ProductLogic.RestoreProductSpecStock(orderProductDtos);

            //取消订单
            orderLogic.orderService.updateById(o);
        }

        //返回已取消的订单列表
        return orders.stream().map(o -> o.getOrderId()).collect(Collectors.toList());

    }

    /**
     * 取消订单
     *
     * @param orderId
     */
    public static void CancelOrder(int orderId) {

        Order order = orderLogic.orderService.selectById(orderId);
        if (OrderStatus.valueOf(order.getStatus()) != OrderStatus.UNPAY) {
            throw new TipException("当前订单不允许取消");
        }

        //修改状态
        order.setStatus(OrderStatus.CANCEL.getValue());

        //恢复库存
        List<OrderProductDto> orderProductDtos = OrderLogic.GetOrderProducts(order.getOrderId());
        ProductLogic.RestoreProductSpecStock(orderProductDtos);

        //取消订单
        orderLogic.orderService.updateById(order);

    }

    /**
     * 删除订单
     * 订单状态为未支付时，恢复库存
     *
     * @param orderId
     */
    public static void DeleteOrder(int orderId) {

        Order order = orderLogic.orderService.selectById(orderId);
        OrderStatus orderStatus = OrderStatus.valueOf(order.getStatus());

        if (orderStatus == OrderStatus.UNPAY) {
            //修改状态
            order.setStatus(OrderStatus.CANCEL.getValue());
            //恢复库存
            List<OrderProductDto> orderProductDtos = OrderLogic.GetOrderProducts(order.getOrderId());
            ProductLogic.RestoreProductSpecStock(orderProductDtos);
        }
        order.setDeleteTime(DateUtils.getCurrentUnixTime());
        //取消订单
        orderLogic.orderService.updateById(order);

    }

}
