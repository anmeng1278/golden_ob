package com.jsj.member.ob.logic;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jsj.member.ob.dto.api.delivery.DeliveryDto;
import com.jsj.member.ob.dto.api.express.ExpressRequ;
import com.jsj.member.ob.dto.api.express.ExpressResp;
import com.jsj.member.ob.dto.api.product.ProductDto;
import com.jsj.member.ob.dto.api.stock.StockDto;
import com.jsj.member.ob.entity.Delivery;
import com.jsj.member.ob.entity.DeliveryStock;
import com.jsj.member.ob.enums.DeliveryStatus;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.service.DeliveryService;
import com.jsj.member.ob.service.DeliveryStockService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DeliveryLogic extends BaseLogic {

    public static DeliveryLogic deliveryLogic;


    @PostConstruct
    public void init() {
        deliveryLogic = this;
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
     *
     * @param deliveryId
     * @return
     */
    public static List<StockDto> GetDeliveryStock(int deliveryId) {

        EntityWrapper<DeliveryStock> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("delivery_id={0}", deliveryId);
        entityWrapper.where("delete_time is null");

        List<DeliveryStock> deliveryStocks = deliveryLogic.deliveryStockService.selectList(entityWrapper);

        List<StockDto> stockDtos = new ArrayList<>();
        for (DeliveryStock deliveryStock : deliveryStocks) {
            StockDto stockDto = StockLogic.GetStock(deliveryStock.getStockId());
            stockDtos.add(stockDto);
        }

        return stockDtos;
    }


    /**
     * 获取我的配送记录
     * @param openId
     * @return
     */
    public static List<DeliveryDto> GetMyDelivery(String openId) {

        if (StringUtils.isBlank(openId)) {
            throw new TipException("参数不合法，用户openId为空");
        }

        EntityWrapper<Delivery> wrapper = new EntityWrapper<>();
        wrapper.where("delete_time is null and open_Id={0}", openId);
        List<Delivery> deliveries = deliveryLogic.deliveryService.selectList(wrapper);

        List<DeliveryDto> deliveryDtos = new ArrayList<>();
        List<ProductDto> productDtos = new ArrayList<>();
        for (Delivery delivery : deliveries) {
            DeliveryDto deliveryDto = new DeliveryDto();

            List<StockDto> stockDtos = DeliveryLogic.GetDeliveryStock(delivery.getDeliveryId());
            for (StockDto stockDto : stockDtos) {
                ProductDto productDto = ProductLogic.GetProduct(stockDto.getProductId());
                productDtos.add(productDto);
            }
            deliveryDto.setStockDto(stockDtos);
            deliveryDto.setProductDtos(productDtos);

            deliveryDto.setAddress(delivery.getAddress());
            deliveryDto.setCityId(delivery.getCityId());
            deliveryDto.setProvinceId(delivery.getProvinceId());
            deliveryDto.setContactName(delivery.getContactName());
            deliveryDto.setDistrictId(delivery.getDistrictId());
            deliveryDto.setOpenId(delivery.getOpenId());
            deliveryDto.setExpressNumber(delivery.getExpressNumber());
            deliveryDto.setMobile(delivery.getMobile());
            deliveryDto.setStatus(delivery.getStatus());
            deliveryDto.setTypeId(delivery.getTypeId());
            deliveryDto.setRemarks(delivery.getRemarks());
            deliveryDto.setDeliveryId(delivery.getDeliveryId());
            deliveryDtos.add(deliveryDto);

        }
        return deliveryDtos;

    }


}
