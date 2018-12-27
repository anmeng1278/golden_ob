package com.jsj.member.ob.logic;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jsj.member.ob.dto.api.order.CreateOrderRequ;
import com.jsj.member.ob.dto.api.order.CreateOrderResp;
import com.jsj.member.ob.dto.api.order.OrderDto;
import com.jsj.member.ob.dto.api.order.OrderProductDto;
import com.jsj.member.ob.dto.api.product.ProductDto;
import com.jsj.member.ob.entity.Order;
import com.jsj.member.ob.entity.OrderProduct;
import com.jsj.member.ob.enums.ActivityType;
import com.jsj.member.ob.enums.OrderFlag;
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

    //region (public) 创建订单，普通订单、团单、秒杀单、 CreateOrder

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
    //endregion

    //region (public) 获取订单商品列表 GetOrderProducts

    /**
     * 获取订单商品列表
     *
     * @param orderId
     * @return
     */
    public static List<OrderProductDto> GetOrderProducts(int orderId) {

        List<OrderProductDto> orderProductDtos = new ArrayList<>();

        EntityWrapper<OrderProduct> wrapper = new EntityWrapper<OrderProduct>();
        wrapper.where("delete_time is null");
        wrapper.where("order_id={0}", orderId);
        wrapper.orderBy("create_time desc");

        List<OrderProduct> orderProducts = orderLogic.orderProductService.selectList(wrapper);

        orderProducts.forEach(op -> {

            OrderProductDto dto = new OrderProductDto();

            ProductDto productDto = ProductLogic.GetProduct(op.getProductId());

            dto.setProductDto(productDto);
            dto.setNumber(op.getNumber());
            dto.setProductSpecId(op.getProductSpecId());
            dto.setProductId(op.getProductId());

            orderProductDtos.add(dto);
        });

        return orderProductDtos;
    }
    //endregion

    //region (public) 获取订单数 GetOrderCount

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
    //endregion

    //region (public) 取消订单 CancelOrder

    /**
     * 取消订单
     *
     * @return
     */
    public static List<Integer> CancelOrder() {

        EntityWrapper<Order> entityWrapper = new EntityWrapper<>();

        List list = new ArrayList();
        list.add(OrderStatus.UNPAY.getValue());
        list.add(OrderStatus.PAYFAIL.getValue());

        entityWrapper.in("status", list);
        entityWrapper.where("delete_time is null");
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

            //TODO 未支付订单取消客服消息
            //o.getOpenId()
        }

        //返回已取消的订单列表
        return orders.stream().map(o -> o.getOrderId()).collect(Collectors.toList());

    }
    //endregion

    //region (public) 取消订单 CancelOrder

    /**
     * 取消订单
     *
     * @param orderId
     */
    public static void CancelOrder(int orderId) {

        Order order = orderLogic.orderService.selectById(orderId);

        if (OrderStatus.valueOf(order.getStatus()) != OrderStatus.UNPAY &&
                OrderStatus.valueOf(order.getStatus()) != OrderStatus.PAYFAIL) {
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
    //endregion

    //region (public) 删除订单 DeleteOrder

    /**
     * 删除订单
     * 订单状态为未支付时，恢复库存
     *
     * @param orderId
     */
    public static void DeleteOrder(int orderId) {

        Order order = orderLogic.orderService.selectById(orderId);
        OrderStatus orderStatus = OrderStatus.valueOf(order.getStatus());

        if (orderStatus == OrderStatus.UNPAY && orderStatus != OrderStatus.PAYFAIL) {
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
    //endregion

    //region (public) 获得用户的所有订单 GetOrders

    /**
     * 获得用户的所有订单
     *
     * @param openId
     * @return
     */
    public static List<OrderDto> GetOrders(String openId) {
        return OrderLogic.GetOrders(openId, OrderFlag.ALLORDERS);
    }
    //endregion

    //region (public) 获得用户所有订单或所有待支付订单 GetOrders

    /**
     * 获得用户所有订单或所有待支付订单
     *
     * @param openId
     * @return
     */
    public static List<OrderDto> GetOrders(String openId, OrderFlag orderFlag) {

        EntityWrapper<Order> wrapper = new EntityWrapper<>();
        wrapper.where("delete_time is null and open_id={0}", openId);

        if (orderFlag == OrderFlag.UNPAIDORDERS) {

            List<Integer> orderStatuses = new ArrayList<>();
            orderStatuses.add(OrderStatus.UNPAY.getValue());
            orderStatuses.add(OrderStatus.PAYFAIL.getValue());

            wrapper.in("status", orderStatuses);
        }

        wrapper.orderBy(" status asc ,create_time desc");

        List<Order> orders = orderLogic.orderService.selectList(wrapper);
        List<OrderDto> orderDtos = new ArrayList<>();

        for (Order entity : orders) {

            OrderDto dto = OrderLogic.ToDto(entity);

            List<OrderProductDto> orderProductDtos = OrderLogic.GetOrderProducts(entity.getOrderId());
            dto.setOrderProductDtos(orderProductDtos);

            orderDtos.add(dto);
        }
        return orderDtos;
    }
    //endregion

    //region (public) 实体转换 ToDto

    /**
     * 实体转换
     * @param entity
     * @return
     */
    public static OrderDto ToDto(Order entity) {

        OrderDto dto = new OrderDto();

        dto.setOpenId(entity.getOpenId());
        dto.setOrderId(entity.getOrderId());
        dto.setRemarks(entity.getRemarks());
        dto.setAmount(entity.getAmount());
        dto.setPayAmount(entity.getPayAmount());

        dto.setStatus(OrderStatus.valueOf(entity.getStatus()));
        dto.setTransactionId(entity.getTransactionId());
        dto.setWechatCouponId(entity.getWechatCouponId());
        dto.setCouponPrice(entity.getCouponPrice());
        dto.setActivityId(entity.getActivityId());

        dto.setActivityOrderId(entity.getActivityOrderId());
        dto.setTypeId(entity.getTypeId());
        dto.setPayTime(entity.getPayTime());
        dto.setExpiredTime(entity.getExpiredTime());
        dto.setUpdateTime(entity.getUpdateTime());

        dto.setOrderUniqueCode(entity.getOrderUniqueCode());

        return dto;

    }
    //endregion

    //region (public) 获取订单 GetOrder

    /**
     * 获取订单
     * @param orderId
     * @return
     */
    public static OrderDto GetOrder(int orderId) {

        Order entity = orderLogic.orderService.selectById(orderId);
        OrderDto dto = OrderLogic.ToDto(entity);

        return dto;
    }
    //endregion

    //region (public) 获取订单 GetOrder

    /**
     * 获取订单
     * @param orderUniqueCode
     * @return
     */
    public static OrderDto GetOrder(String orderUniqueCode) {

        Wrapper<Order> wrapper = new EntityWrapper<>();
        wrapper.where("order_unique_code = {0}", orderUniqueCode);

        Order entity = orderLogic.orderService.selectOne(wrapper);
        OrderDto dto = OrderLogic.ToDto(entity);

        return dto;
    }
    //endregion

    //region (public) 获取秒杀活动成功后的秒杀订单 GetOrder

    /**
     * 获取秒杀活动成功后的秒杀订单
     *
     * @param activityId
     * @param openId
     * @param activityType
     * @param createTime
     * @return
     */
    public static OrderDto GetOrder(int activityId, String openId, ActivityType activityType, int createTime) {

        Wrapper<Order> wrapper = new EntityWrapper<>();

        wrapper.where("activity_id = {0}", activityId);
        wrapper.where("type_id = {0}", activityType.getValue());
        wrapper.where("create_time >= {0}", createTime);
        wrapper.where("open_id = {0}", openId);

        wrapper.orderBy("create_time desc");

        Order order = orderLogic.orderService.selectOne(wrapper);
        if (order != null) {
            return ToDto(order);
        }

        return null;

    }
    //endregion
}
