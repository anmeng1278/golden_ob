package com.jsj.member.ob.logic.delivery;

import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.dto.api.delivery.CreateDeliveryRequ;
import com.jsj.member.ob.dto.api.delivery.CreateDeliveryResp;
import com.jsj.member.ob.dto.api.delivery.DeliveryDto;
import com.jsj.member.ob.dto.api.stock.StockDto;
import com.jsj.member.ob.entity.Delivery;
import com.jsj.member.ob.entity.DeliveryStock;
import com.jsj.member.ob.entity.Stock;
import com.jsj.member.ob.enums.DeliveryStatus;
import com.jsj.member.ob.enums.DeliveryType;
import com.jsj.member.ob.enums.PropertyType;
import com.jsj.member.ob.enums.StockStatus;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.DeliveryLogic;
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
public class DeliveryGoldenCard extends DeliveryBase {

    public DeliveryGoldenCard() {
        super(PropertyType.GOLDENCARD);
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
        if (requ.getUseProductDtos().size() > 1) {
            throw new TipException("开卡只能使用一个");
        }
        if (StringUtils.isEmpty(requ.getContactName())) {
            throw new TipException("真实姓名不能为空");
        }
        if (!com.jsj.member.ob.utils.StringUtils.isMobile(requ.getMobile())) {
            throw new TipException("手机号码格式错误");
        }

        if (!com.jsj.member.ob.utils.StringUtils.isIdNumber(requ.getIdNumber())) {
            throw new TipException("证件号格式错误");
        }
        if (!com.jsj.member.ob.utils.StringUtils.isStrDate(requ.getEffectiveDate())) {
            throw new TipException("生效日期格式错误");
        }


        String openId = requ.getBaseRequ().getOpenId();

        //判断是否存在未开卡申请
        DeliveryDto unCreateGoldenCard = DeliveryLogic.getUnCreateGoldenCard(openId);
        if (unCreateGoldenCard != null) {
            throw new TipException("您的开卡正在确认中，不能重复开卡……");
        }

        //获取库存
        List<StockDto> stockDtos = StockLogic.GetStocks(openId, requ.getUseProductDtos(), false);

        Delivery delivery = new Delivery();

        delivery.setStatus(DeliveryStatus.UNDELIVERY.getValue());
        delivery.setUpdateTime(DateUtils.getCurrentUnixTime());
        delivery.setContactName(requ.getContactName());
        delivery.setIdNumber(requ.getIdNumber());
        delivery.setEffectiveDate(requ.getEffectiveDate());

        delivery.setMobile(Long.parseLong(requ.getMobile()));
        delivery.setCreateTime(DateUtils.getCurrentUnixTime());
        delivery.setOpenId(requ.getBaseRequ().getOpenId());

        delivery.setPropertyTypeId(this.getPropertyType().getValue());
        delivery.setRemarks(requ.getRemarks());
        delivery.setTypeId(DeliveryType.PICKUP.getValue());

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
