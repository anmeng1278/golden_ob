package com.jsj.member.ob.dto.mini;

import com.jsj.member.ob.dto.api.delivery.DeliveryDto;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class DeliveryDetailResp {

    @ApiModelProperty(value = "配送信息", required = true)
    private DeliveryDto deliveryDto;

    @ApiModelProperty(value = "配送记录", required = true)
    private List data;

    public DeliveryDto getDeliveryDto() {
        return deliveryDto;
    }

    public void setDeliveryDto(DeliveryDto deliveryDto) {
        this.deliveryDto = deliveryDto;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }
}
