package com.jsj.member.ob.logic.order;

import com.jsj.member.ob.entity.Order;
import com.jsj.member.ob.enums.ActivityType;
import com.jsj.member.ob.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 创建订单操作工厂
 */
@Component
public class OrderFactory {

    @Autowired
    private List<OrderBase> orderBases;

    @Autowired
    private OrderService orderService;

    public static OrderFactory orderFactory;

    private static final Map<ActivityType, OrderBase> myServiceCache = new HashMap<>();

    @PostConstruct
    public void initMyServiceCache() {
        for (OrderBase ob : orderBases) {
            myServiceCache.put(ob.getActivityType(), ob);
        }
        orderFactory = this;
        orderFactory.orderService = this.orderService;
    }


    /**
     * 获取操作实体类
     *
     * @param activityType
     * @return
     */
    public static OrderBase GetInstance(ActivityType activityType) {

        OrderBase ob = myServiceCache.get(activityType);
        if (ob == null) throw new RuntimeException("Unknown orderType: " + activityType);
        return ob;

    }

    /**
     * 根据订单号获取操作实例
     *
     * @param orderId
     * @return
     */
    public static OrderBase GetInstance(int orderId) {

        Order order = orderFactory.orderService.selectById(orderId);
        ActivityType activityType = ActivityType.valueOf(order.getTypeId());

        return OrderFactory.GetInstance(activityType);
    }

}
