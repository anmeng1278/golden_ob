package com.jsj.member.ob.dto.mini;

import io.swagger.annotations.ApiModelProperty;

public class DeliveryDetailRequ {

    @ApiModelProperty(value = "配送编号", required = true)
    private int deliveryId;

    @ApiModelProperty(value = "库存编号", required = true)
    private int stockId;

    public int getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(int deliveryId) {
        this.deliveryId = deliveryId;
    }

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }
}
