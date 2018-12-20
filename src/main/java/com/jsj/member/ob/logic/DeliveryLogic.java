package com.jsj.member.ob.logic;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jsj.member.ob.dto.api.delivery.CreateDeliveryRequ;
import com.jsj.member.ob.dto.api.delivery.CreateDeliveryResp;
import com.jsj.member.ob.dto.api.delivery.DeliveryDto;
import com.jsj.member.ob.dto.api.delivery.DeliveryStockDto;
import com.jsj.member.ob.dto.api.product.ProductDto;
import com.jsj.member.ob.dto.api.stock.StockDto;
import com.jsj.member.ob.entity.Delivery;
import com.jsj.member.ob.entity.DeliveryStock;
import com.jsj.member.ob.enums.DeliveryStatus;
import com.jsj.member.ob.enums.DeliveryType;
import com.jsj.member.ob.enums.PropertyType;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.delivery.DeliveryBase;
import com.jsj.member.ob.logic.delivery.DeliveryFactory;
import com.jsj.member.ob.service.DeliveryService;
import com.jsj.member.ob.service.DeliveryStockService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

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
        entityWrapper.where("delete_time is null");
        entityWrapper.where("delivery_id={0}", deliveryId);

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
     *
     * @param openId
     * @return
     */
    public static List<DeliveryDto> GetDelivery(String openId) {

        if (StringUtils.isBlank(openId)) {
            throw new TipException("参数不合法，用户openId为空");
        }

        EntityWrapper<Delivery> wrapper = new EntityWrapper<>();
        wrapper.where("delete_time is null and open_Id={0}", openId);
        List<Delivery> deliveries = deliveryLogic.deliveryService.selectList(wrapper);

        List<DeliveryDto> deliveryDtos = new ArrayList<>();
        for (Delivery delivery : deliveries) {

            DeliveryDto dto = DeliveryLogic.ToDto(delivery);

            deliveryDtos.add(dto);

        }
        return deliveryDtos;

    }

    /**
     * 获取配送信息
     *
     * @param deliveryId
     * @return
     */
    public static DeliveryDto GetDelivery(int deliveryId) {

        Delivery delivery = deliveryLogic.deliveryService.selectById(deliveryId);
        DeliveryDto dto = DeliveryLogic.ToDto(delivery);

        return dto;

    }

    /**
     * 获得DeliveryStock
     *
     * @param deliverId
     * @return
     */
    public static List<DeliveryStock> GetDeliveryStocks(int deliverId) {

        EntityWrapper<DeliveryStock> wrapper = new EntityWrapper<>();
        wrapper.where("delivery_id={0}", deliverId);

        List<DeliveryStock> deliveryStocks = deliveryLogic.deliveryStockService.selectList(wrapper);

        return deliveryStocks;
    }

    /**
     * 实体转换
     *
     * @param delivery
     * @return
     */
    public static DeliveryDto ToDto(Delivery delivery) {

        if (delivery == null) {
            return null;
        }

        DeliveryDto dto = new DeliveryDto();

        List<ProductDto> productDtos = new ArrayList<>();
        List<StockDto> stockDtos = DeliveryLogic.GetDeliveryStock(delivery.getDeliveryId());
        for (StockDto stockDto : stockDtos) {
            ProductDto productDto = ProductLogic.GetProduct(stockDto.getProductId());
            productDtos.add(productDto);
        }
        dto.setStockDtos(stockDtos);
        dto.setProductDtos(productDtos);

        dto.setAddress(delivery.getAddress());
        dto.setCityId(delivery.getCityId());
        dto.setProvinceId(delivery.getProvinceId());
        dto.setContactName(delivery.getContactName());
        dto.setDistrictId(delivery.getDistrictId());
        dto.setEffectiveDate(delivery.getEffectiveDate());

        dto.setOpenId(delivery.getOpenId());
        dto.setExpressNumber(delivery.getExpressNumber());
        dto.setMobile(delivery.getMobile());
        dto.setDeliveryStatus(DeliveryStatus.valueOf(delivery.getStatus()));
        dto.setDeliveryType(DeliveryType.valueOf(delivery.getTypeId()));
        dto.setRemarks(delivery.getRemarks());

        dto.setDeliveryId(delivery.getDeliveryId());
        dto.setCreateTime(delivery.getCreateTime());
        dto.setPropertyType(PropertyType.valueOf(delivery.getPropertyTypeId()));
        dto.setFlightNumber(delivery.getFlightNumber());
        dto.setAirportCode(delivery.getAirportCode());
        dto.setIdNumber(delivery.getIdNumber());
        dto.setAirportName(delivery.getAirportName());

        return dto;
    }


    /**
     * 创建配送
     *
     * @param requ
     * @return
     */
    public static CreateDeliveryResp CreateDelivery(CreateDeliveryRequ requ) {
        DeliveryBase deliveryBase = DeliveryFactory.GetInstance(requ.getPropertyType());
        return deliveryBase.CreateDelivery(requ);
    }

    /**
     * 获得配送集合
     * @param deliveryStatus
     * @param deliveryType
     * @return
     */
    public static List<Delivery> GetDelivery(DeliveryStatus deliveryStatus, DeliveryType deliveryType, PropertyType propertyType){

        EntityWrapper<Delivery> wrapper = new EntityWrapper<>();
        wrapper.where("delete_time is null");

        if(deliveryStatus != null){
            wrapper.where("status = {0} ",deliveryStatus.getValue());
        }
        if(deliveryType != null){
            wrapper.where("type_id = {0}",deliveryType);
        }
        if(propertyType != null){
            wrapper.where("property_type_id = {0}",propertyType);
        }

        List<Delivery> deliveries = deliveryLogic.deliveryService.selectList(wrapper);

        return deliveries;

    }

    /**
     * 获取配送库存
     *
     * @param openId
     * @param deliveryId
     * @return
     */
    public static List<DeliveryStockDto> GetDeliveryStocks(int deliveryId, String openId) {

        Wrapper<DeliveryStock> wrapper = new EntityWrapper<>();
        wrapper.where("delete_time is null");
        wrapper.where("delivery_id = {0}", deliveryId);

        List<DeliveryStock> deliveryStocks = deliveryLogic.deliveryStockService.selectList(wrapper);
        List<DeliveryStockDto> deliveryStockDtos = new ArrayList<>();

        deliveryStocks.forEach(d -> {
            deliveryStockDtos.add(ToDeliveryStockDto(d));
        });
        return deliveryStockDtos;
    }

    /**
     * 配送库存实体转换
     *
     * @param entity
     * @return
     */
    public static DeliveryStockDto ToDeliveryStockDto(DeliveryStock entity) {

        DeliveryStockDto dto = new DeliveryStockDto();

        dto.setActivityCode(entity.getActivityCode());
        dto.setDeliveryId(entity.getDeliveryId());
        dto.setDeliveryStockId(entity.getDeliveryStockId());
        dto.setStockId(entity.getStockId());

        return dto;

    }

    /**
     * 验证次卡配送状态
     *
     * @param openId
     */
    public static List<DeliveryStockDto> getUnUsedActivityCodes(String openId) {

        List<Integer> statuses = new ArrayList<>();
        statuses.add(DeliveryStatus.UNDELIVERY.getValue());
        statuses.add(DeliveryStatus.DELIVERED.getValue());

        Wrapper<Delivery> wrapper = new EntityWrapper<>();
        wrapper.where("delete_time is null");
        wrapper.where("open_id = {0}", openId);
        wrapper.where("property_type_id = {0}", PropertyType.ACTIVITYCODE.getValue());
        wrapper.in("status ", statuses);

        List<DeliveryStockDto> deliveryStockDtos = new ArrayList<>();

        Delivery delivery = deliveryLogic.deliveryService.selectOne(wrapper);
        if (delivery == null) {
            return deliveryStockDtos;
        }

        deliveryStockDtos = GetDeliveryStocks(delivery.getDeliveryId(), openId);
        return deliveryStockDtos;
    }

    /**
     * 获取未开卡信息
     *
     * @param openId
     * @return
     */
    public static DeliveryDto getUnCreateGoldenCard(String openId) {

        Wrapper<Delivery> wrapper = new EntityWrapper<>();

        wrapper.where("open_id = {0}", openId);
        wrapper.where("delete_time is null");
        wrapper.where("status = {0}", DeliveryStatus.UNDELIVERY.getValue());
        wrapper.where("property_type_id = {0}", PropertyType.GOLDENCARD.getValue());

        Delivery delivery = deliveryLogic.deliveryService.selectOne(wrapper);
        return ToDto(delivery);

    }
}
