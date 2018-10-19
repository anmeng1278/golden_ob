package com.jsj.member.ob.logic.order;

import com.jsj.member.ob.enums.ActivityType;
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

    private static final Map<ActivityType, OrderBase> myServiceCache = new HashMap<>();

    @PostConstruct
    public void initMyServiceCache() {
        for (OrderBase ob : orderBases) {
            myServiceCache.put(ob.getActivityType(), ob);
        }
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
}
