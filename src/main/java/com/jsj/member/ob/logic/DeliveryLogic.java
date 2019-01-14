package com.jsj.member.ob.logic;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jsj.member.ob.dto.api.delivery.*;
import com.jsj.member.ob.dto.api.product.ProductDto;
import com.jsj.member.ob.dto.api.stock.StockDto;
import com.jsj.member.ob.entity.Delivery;
import com.jsj.member.ob.entity.DeliveryStock;
import com.jsj.member.ob.entity.Stock;
import com.jsj.member.ob.enums.DeliveryStatus;
import com.jsj.member.ob.enums.DeliveryType;
import com.jsj.member.ob.enums.PropertyType;
import com.jsj.member.ob.enums.StockStatus;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.delivery.DeliveryBase;
import com.jsj.member.ob.logic.delivery.DeliveryFactory;
import com.jsj.member.ob.service.DeliveryService;
import com.jsj.member.ob.service.DeliveryStockService;
import com.jsj.member.ob.service.StockService;
import com.jsj.member.ob.utils.DateUtils;
import com.jsj.member.ob.utils.Md5Utils;
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

    @Autowired
    StockService stockService;

    //region (public) 获取未发货数 GetUnDeliveryCount

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
    //endregion

    //region (public) 获得配送的库存详情 GetDeliveryStock

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
    //endregion

    //region (public) 获取我的配送记录 GetDelivery

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
        wrapper.orderBy("create_time desc");
        List<Delivery> deliveries = deliveryLogic.deliveryService.selectList(wrapper);

        List<DeliveryDto> deliveryDtos = new ArrayList<>();
        for (Delivery delivery : deliveries) {

            DeliveryDto dto = DeliveryLogic.ToDto(delivery);

            deliveryDtos.add(dto);

        }
        return deliveryDtos;

    }
    //endregion

    //region (public) 获取配送信息 GetDelivery

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
    //endregion

    //region (public) 修改物流状态 UpdateDeliveryStatus

    /**
     * 修改物流状态
     *
     * @param deliveryId
     * @return
     */
    public static void UpdateDeliveryStatus(int deliveryId, String method) {

        Delivery delivery = deliveryLogic.deliveryService.selectById(deliveryId);

        if (method.equals("delete")) {
            delivery.setDeleteTime(DateUtils.getCurrentUnixTime());
        }

        if (method.equals("confirm")) {
            delivery.setStatus(DeliveryStatus.SIGNED.getValue());
            delivery.setUpdateTime(DateUtils.getCurrentUnixTime());

        }
        deliveryLogic.deliveryService.updateById(delivery);

    }
    //endregion

    //region (public) 获得DeliveryStock GetDeliveryStocks

    /**
     * 获得DeliveryStock
     *
     * @param deliverId
     * @return
     */
    public static List<DeliveryStock> GetDeliveryStocks(int deliverId) {

        EntityWrapper<DeliveryStock> wrapper = new EntityWrapper<>();
        wrapper.where("delete_time is null");
        wrapper.where("delivery_id={0}", deliverId);
        wrapper.orderBy("create_time desc");

        List<DeliveryStock> deliveryStocks = deliveryLogic.deliveryStockService.selectList(wrapper);

        return deliveryStocks;
    }
    //endregion

    //region (public) 实体转换 ToDto

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
        dto.setUpdateTime(delivery.getUpdateTime());

        dto.setDeliveryId(delivery.getDeliveryId());
        dto.setCreateTime(delivery.getCreateTime());
        dto.setPropertyType(PropertyType.valueOf(delivery.getPropertyTypeId()));
        dto.setFlightNumber(delivery.getFlightNumber());
        dto.setAirportCode(delivery.getAirportCode());
        dto.setIdNumber(delivery.getIdNumber());
        dto.setAirportName(delivery.getAirportName());

        dto.setIdTypeId(delivery.getIdTypeId());
        dto.setDeleteTime(delivery.getDeleteTime());

        return dto;
    }
    //endregion

    //region (public) 创建配送 CreateDelivery

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
    //endregion

    //region (public) 获得配送集合 CreateDelivery

    /**
     * 获得配送集合
     *
     * @param deliveryStatus
     * @param deliveryType
     * @return
     */
    public static List<Delivery> GetDelivery(DeliveryStatus deliveryStatus, DeliveryType deliveryType, PropertyType propertyType) {

        EntityWrapper<Delivery> wrapper = new EntityWrapper<>();
        wrapper.where("delete_time is null");

        if (deliveryStatus != null) {
            wrapper.where("status = {0} ", deliveryStatus.getValue());
        }
        if (deliveryType != null) {
            wrapper.where("type_id = {0}", deliveryType);
        }
        if (propertyType != null) {
            wrapper.where("property_type_id = {0}", propertyType);
        }

        wrapper.orderBy("create_time desc");

        List<Delivery> deliveries = deliveryLogic.deliveryService.selectList(wrapper);

        return deliveries;

    }
    //endregion

    //region (public) 获取配送库存 GetDeliveryStocks

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
        wrapper.orderBy("create_time desc");

        List<DeliveryStock> deliveryStocks = deliveryLogic.deliveryStockService.selectList(wrapper);
        List<DeliveryStockDto> deliveryStockDtos = new ArrayList<>();

        deliveryStocks.forEach(d -> {
            deliveryStockDtos.add(ToDeliveryStockDto(d));
        });
        return deliveryStockDtos;
    }
    //endregion

    //region (public) 配送库存实体转换 ToDeliveryStockDto

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
    //endregion


    //region (public) 获取未开卡信息 GetUnDeliveryDto

    /**
     * 获取未发货信息
     *
     * @param openId
     * @return
     */
    public static DeliveryDto GetUnDeliveryDto(String openId, PropertyType propertyType) {

        Wrapper<Delivery> wrapper = new EntityWrapper<>();

        wrapper.where("open_id = {0}", openId);
        wrapper.where("delete_time is null");

        //活动码 0/10 不允许
        switch (propertyType) {
            case ACTIVITYCODE: {
                List<Integer> status = new ArrayList<>();
                status.add(DeliveryStatus.UNDELIVERY.getValue());
                status.add(DeliveryStatus.DELIVERED.getValue());
                wrapper.in("status", status);
            }
            break;
            case ENTITY:
                wrapper.where("status = {0}", DeliveryStatus.UNDELIVERY.getValue());
                break;
            case GOLDEN:
            case MONTH:
            case NATION: {
                List<Integer> status = new ArrayList<>();
                status.add(DeliveryStatus.UNDELIVERY.getValue());
                status.add(DeliveryStatus.DELIVERED.getValue());
                wrapper.in("status", status);
            }
            break;
        }
        wrapper.where("property_type_id = {0}", propertyType.getValue());

        Delivery delivery = deliveryLogic.deliveryService.selectOne(wrapper);
        return ToDto(delivery);

    }

    //endregion

    //region (public) 使用活动码 ActivityCodeVerify

    /**
     * 使用活动码
     *
     * @param requ
     * @return
     */
    public static VerifyActivityCodeResp VerifyActivityCode(VerifyActivityCodeRequ requ) {

        VerifyActivityCodeResp resp = new VerifyActivityCodeResp();
        if (StringUtils.isEmpty(requ.getActivityCode())) {
            resp.setRespCd("9999");
            resp.setRespMsg("活动码不能为空");
            return resp;
        }

        int timeStamp = requ.getTimeStamp();
        String activityCode = requ.getActivityCode();

        if (Math.abs(DateUtils.getCurrentUnixTime() - timeStamp) >= 60 * 10) {
            resp.setRespCd("9999");
            resp.setRespMsg("请求已过期");
            return resp;
        }

        String sign = String.format("%s%dJSYX", activityCode, timeStamp);
        sign = Md5Utils.MD5(sign).toLowerCase();

        if (!requ.getSign().equals(sign)) {
            resp.setRespCd("9999");
            resp.setRespMsg("非法请求");
            return resp;
        }

        Wrapper<DeliveryStock> wrapper = new EntityWrapper<>();
        wrapper.where("activity_code = {0}", activityCode);

        DeliveryStock deliveryStock = deliveryLogic.deliveryStockService.selectOne(wrapper);
        if (deliveryStock == null) {
            resp.setRespCd("9999");
            resp.setRespMsg(String.format("没有找到活动码：%s", activityCode));
            return resp;
        }

        Delivery delivery = deliveryLogic.deliveryService.selectById(deliveryStock.getDeliveryId());
        if (!delivery.getStatus().equals(DeliveryStatus.SIGNED.getValue())) {

            delivery.setStatus(DeliveryStatus.SIGNED.getValue());
            String remark = delivery.getRemarks();
            if (remark != null) {
                remark += String.format("使用活动码：%s %s %s", activityCode, requ.getAirportName(), requ.getVipHallName());
            } else {
                remark = String.format("使用活动码：%s %s %s", activityCode, requ.getAirportName(), requ.getVipHallName());
            }
            delivery.setRemarks(remark);
            deliveryLogic.deliveryService.updateById(delivery);

            //更新活动码核销状态
            Stock stock = deliveryLogic.stockService.selectById(deliveryStock.getStockId());
            stock.setStatus(StockStatus.SIGNED.getValue());
            deliveryLogic.stockService.updateById(stock);

            //TODO 发送活动码核销成功
            //用户持活动码进厅，员工验证活动码下单后，调用起方法
            //机场名称 requ.getAirportName() 贵宾厅名称 requ.getVipHallName()
            //示例：感谢您使用金色世纪贵宾厅服务，祝你旅途愉快
            //delivery.getOpenId();
        }


        resp.setRespCd("0000");
        resp.setRespMsg("核销成功");
        return resp;

    }
    //endregion

    //region (public) 获取待开卡记录 GetUnOpenGoldenCard

    /**
     * 获取待开卡记录
     *
     * @return
     */
    public static List<DeliveryDto> GetUnCreateGoldenCard() {

        Wrapper<Delivery> wrapper = new EntityWrapper<>();

        wrapper.where("delete_time is null");
        //扫描任务
        wrapper.where("property_type_id = {0}", PropertyType.MONTH.getValue());
        wrapper.where("status = {0}", DeliveryStatus.UNDELIVERY.getValue());
        wrapper.where("( effective_date > 0 and effective_date <= UNIX_TIMESTAMP())");


        List<Delivery> deliveries = deliveryLogic.deliveryService.selectList(wrapper);
        List<DeliveryDto> deliveryDtos = new ArrayList<>();

        deliveries.forEach(entity -> {
            deliveryDtos.add(ToDto(entity));
        });

        return deliveryDtos;
    }
    //endregion
}
