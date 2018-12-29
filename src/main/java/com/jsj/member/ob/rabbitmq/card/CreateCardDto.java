package com.jsj.member.ob.rabbitmq.card;

import com.jsj.member.ob.enums.PropertyType;
import com.jsj.member.ob.rabbitmq.BaseDto;

public class CreateCardDto extends BaseDto {

    /**
     * 配送编号
     */
    private int deliveryId;

    /**
     * 商品类型
     */
    private PropertyType propertyType;

    public int getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(int deliveryId) {
        this.deliveryId = deliveryId;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }
}
