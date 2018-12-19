package com.jsj.member.ob.logic.delivery;

import com.jsj.member.ob.dto.api.delivery.CreateDeliveryRequ;
import com.jsj.member.ob.dto.api.delivery.CreateDeliveryResp;
import com.jsj.member.ob.enums.PropertyType;
import com.jsj.member.ob.service.DeliveryService;
import com.jsj.member.ob.service.DeliveryStockService;
import com.jsj.member.ob.service.StockService;

public abstract class DeliveryBase {


    DeliveryService deliveryService;
    StockService stockService;
    DeliveryStockService deliveryStockService;

    public DeliveryBase(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    /**
     * 订单类型
     */
    private PropertyType propertyType;

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public abstract CreateDeliveryResp CreateDelivery(CreateDeliveryRequ requ);

}
