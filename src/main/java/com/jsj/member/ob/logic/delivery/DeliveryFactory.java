package com.jsj.member.ob.logic.delivery;

import com.jsj.member.ob.enums.PropertyType;
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
public class DeliveryFactory {

    @Autowired
    private List<DeliveryBase> deliveryBases;

    public static DeliveryFactory orderFactory;

    private static final Map<PropertyType, DeliveryBase> myServiceCache = new HashMap<>();

    @PostConstruct
    public void initMyServiceCache() {
        for (DeliveryBase ob : deliveryBases) {
            myServiceCache.put(ob.getPropertyType(), ob);
        }
        orderFactory = this;
    }


    /**
     * 获取操作实体类
     *
     * @param propertyType
     * @return
     */
    public static DeliveryBase GetInstance(PropertyType propertyType) {

        DeliveryBase deliveryBase = myServiceCache.get(propertyType);
        if (deliveryBase == null) throw new RuntimeException("Unknown orderType: " + propertyType);
        return deliveryBase;

    }

}
