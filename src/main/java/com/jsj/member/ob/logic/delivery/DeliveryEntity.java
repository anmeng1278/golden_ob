package com.jsj.member.ob.logic.delivery;

import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.dto.api.delivery.CreateDeliveryRequ;
import com.jsj.member.ob.dto.api.delivery.CreateDeliveryResp;
import com.jsj.member.ob.dto.api.stock.StockDto;
import com.jsj.member.ob.entity.Delivery;
import com.jsj.member.ob.entity.DeliveryStock;
import com.jsj.member.ob.entity.Stock;
import com.jsj.member.ob.enums.DeliveryStatus;
import com.jsj.member.ob.enums.DeliveryType;
import com.jsj.member.ob.enums.PropertyType;
import com.jsj.member.ob.enums.StockStatus;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.StockLogic;
import com.jsj.member.ob.service.DeliveryService;
import com.jsj.member.ob.service.DeliveryStockService;
import com.jsj.member.ob.service.StockService;
import com.jsj.member.ob.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class DeliveryEntity extends DeliveryBase {

    public DeliveryEntity() {
        super(PropertyType.ENTITY);
    }

    @Autowired
    public void initService(DeliveryService deliveryService, StockService stockService, DeliveryStockService deliveryStockService) {
        super.deliveryService = deliveryService;
        super.stockService = stockService;
        super.deliveryStockService = deliveryStockService;
    }

    @Override
    @Transactional(Constant.DBTRANSACTIONAL)
    public CreateDeliveryResp CreateDelivery(CreateDeliveryRequ requ) {

        if (requ.getUseProductDtos().isEmpty()) {
            throw new TipException("使用库存不能为空");
        }
        if (StringUtils.isEmpty(requ.getContactName())) {
            throw new TipException("联系人不能为空");
        }
        if (!com.jsj.member.ob.utils.StringUtils.isMobile(requ.getMobile())) {
            throw new TipException("手机号格式错误");
        }

        //配送
        if (requ.getDeliveryType().equals(DeliveryType.DISTRIBUTE)) {
            if (requ.getProvinceId() <= 0) {
                throw new TipException("请选择省份");
            }
            if (requ.getCityId() <= 0) {
                throw new TipException("请选择地区");
            }
            if (StringUtils.isEmpty(requ.getAddress())) {
                throw new TipException("请输入详细地址");
            }
        }

        if (requ.getDeliveryType().equals(DeliveryType.PICKUP)) {
            if (StringUtils.isEmpty(requ.getEffectiveDate())) {
                throw new TipException("请选择自提时间");
            }
            if (StringUtils.isEmpty(requ.getAirportCode())) {
                throw new TipException("请选择自提网点");
            }
        }

        String openId = requ.getBaseRequ().getOpenId();

        //获取库存
        List<StockDto> stockDtos = StockLogic.GetStocks(openId, requ.getUseProductDtos(), false);

        Delivery delivery = new Delivery();

        delivery.setStatus(DeliveryStatus.UNDELIVERY.getValue());
        delivery.setUpdateTime(DateUtils.getCurrentUnixTime());
        delivery.setAddress(requ.getAddress());
        delivery.setCityId(requ.getCityId());
        delivery.setContactName(requ.getContactName());

        delivery.setDistrictId(requ.getDistrictId());
        delivery.setMobile(Long.parseLong(requ.getMobile()));
        delivery.setProvinceId(requ.getProvinceId());
        delivery.setCreateTime(DateUtils.getCurrentUnixTime());
        delivery.setOpenId(requ.getBaseRequ().getOpenId());

        delivery.setPropertyTypeId(this.getPropertyType().getValue());
        delivery.setRemarks(requ.getRemarks());
        delivery.setTypeId(requ.getDeliveryType().getValue());
        delivery.setAirportCode(requ.getAirportCode());
        delivery.setAirportName(requ.getAirportName());

        //自提时间、生效日期
        delivery.setEffectiveDate(requ.getEffectiveDate());
        this.deliveryService.insert(delivery);

        for (StockDto stockDto : stockDtos) {

            Stock stock = this.stockService.selectById(stockDto.getStockId());
            if (stock.getStatus() != StockStatus.UNUSE.getValue()) {
                throw new TipException("当前库存状态不允许使用");
            }

            //添加提供库存
            DeliveryStock deliveryStock = new DeliveryStock();

            deliveryStock.setCreateTime(DateUtils.getCurrentUnixTime());
            deliveryStock.setDeliveryId(delivery.getDeliveryId());
            deliveryStock.setStockId(stock.getStockId());
            deliveryStock.setUpdateTime(DateUtils.getCurrentUnixTime());

            this.deliveryStockService.insert(deliveryStock);

            //修改库存状态
            stock.setStatus(StockStatus.USED.getValue());
            stock.setUpdateTime(DateUtils.getCurrentUnixTime());

            this.stockService.updateById(stock);
        }

        CreateDeliveryResp resp = new CreateDeliveryResp();
        resp.setDeliveryId(delivery.getDeliveryId());

        return resp;
    }
}
