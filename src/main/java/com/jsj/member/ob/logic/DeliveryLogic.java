package com.jsj.member.ob.logic;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jsj.member.ob.dto.api.stock.StockDto;
import com.jsj.member.ob.entity.Delivery;
import com.jsj.member.ob.entity.DeliveryStock;
import com.jsj.member.ob.enums.DeliveryStatus;
import com.jsj.member.ob.service.DeliveryService;
import com.jsj.member.ob.service.DeliveryStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class DeliveryLogic {

    public static DeliveryLogic deliveryLogic;


    @PostConstruct
    public void init() {
        deliveryLogic = this;
        deliveryLogic.deliveryService = this.deliveryService;
        deliveryLogic.deliveryStockService = this.deliveryStockService;
    }

    @Autowired
    DeliveryService deliveryService;

    @Autowired
    DeliveryStockService deliveryStockService;

    /**
     * 获取未发货数
     *
     * @return
     */
    public static Integer GetUnDeliveryCount() {

        EntityWrapper<Delivery> wrapper = new EntityWrapper<>();

        wrapper.where("delete_time is null");
        wrapper.where("status={0}", DeliveryStatus.UNDELIVERY.getValue());

        return deliveryLogic.deliveryService.selectCount(wrapper);

    }

    /**
     * 获得配送的库存详情
     * @param deliveryId
     * @return
     */
    public static List<StockDto> GetDeliveryStock(int deliveryId){

        EntityWrapper<DeliveryStock> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("delivery_id={0}",deliveryId);
        entityWrapper.where("delete_time is null");

        List<DeliveryStock> deliveryStocks = deliveryLogic.deliveryStockService.selectList(entityWrapper);
        List<StockDto> stockDtos = new ArrayList<>();
        for (DeliveryStock deliveryStock : deliveryStocks) {
            StockDto stockDto = StockLogic.GetStock(deliveryStock.getStockId());
            stockDtos.add(stockDto);
        }
        return stockDtos;
    }

}
