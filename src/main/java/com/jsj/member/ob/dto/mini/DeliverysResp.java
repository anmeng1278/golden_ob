package com.jsj.member.ob.dto.mini;

import com.jsj.member.ob.dto.api.delivery.DeliveryDto;

import java.util.List;

public class DeliverysResp {

    List<DeliveryDto> deliveryDtos;

    public List<DeliveryDto> getDeliveryDtos() {
        return deliveryDtos;
    }

    public void setDeliveryDtos(List<DeliveryDto> deliveryDtos) {
        this.deliveryDtos = deliveryDtos;
    }
}
