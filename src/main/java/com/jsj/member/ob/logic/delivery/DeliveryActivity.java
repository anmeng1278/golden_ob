package com.jsj.member.ob.logic.delivery;

import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.dto.api.delivery.CreateDeliveryRequ;
import com.jsj.member.ob.dto.api.delivery.CreateDeliveryResp;
import com.jsj.member.ob.dto.api.delivery.DeliveryStockDto;
import com.jsj.member.ob.dto.api.stock.StockDto;
import com.jsj.member.ob.dto.thirdParty.GetActivityCodesResp;
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
import com.jsj.member.ob.logic.ThirdPartyLogic;
import com.jsj.member.ob.rabbitmq.wx.TemplateDto;
import com.jsj.member.ob.rabbitmq.wx.WxSender;
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
public class DeliveryActivity extends DeliveryBase {

    public DeliveryActivity() {
        super(PropertyType.ACTIVITYCODE);
    }

    WxSender wxSender;

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
            throw new TipException("次卡只能使用一个");
        }
        if (StringUtils.isEmpty(requ.getContactName())) {
            throw new TipException("真实姓名不能为空");
        }
        if (!com.jsj.member.ob.utils.StringUtils.isMobile(requ.getMobile())) {
            throw new TipException("手机号码格式错误");
        }
        if (StringUtils.isEmpty(requ.getAirportCode())) {
            throw new TipException("请选择使用机场");
        }

        String openId = requ.getBaseRequ().getOpenId();

        //判断是否存在未使用的活动码
        List<DeliveryStockDto> deliveryStockDtos = DeliveryLogic.getUnUsedActivityCodes(openId);
        if (!deliveryStockDtos.isEmpty()) {
            throw new TipException("您还有未使用的次卡");
        }

        //获取库存
        List<StockDto> stockDtos = StockLogic.GetStocks(openId, requ.getUseProductDtos(), false);

        Delivery delivery = new Delivery();

        delivery.setStatus(DeliveryStatus.UNDELIVERY.getValue());
        delivery.setUpdateTime(DateUtils.getCurrentUnixTime());
        delivery.setContactName(requ.getContactName());

        delivery.setMobile(Long.parseLong(requ.getMobile()));
        delivery.setCreateTime(DateUtils.getCurrentUnixTime());
        delivery.setOpenId(requ.getBaseRequ().getOpenId());
        delivery.setFlightNumber(requ.getFlightNumber());

        delivery.setPropertyTypeId(this.getPropertyType().getValue());
        delivery.setRemarks(requ.getRemarks());
        delivery.setTypeId(DeliveryType.PICKUP.getValue());
        delivery.setAirportCode(requ.getAirportCode());
        delivery.setAirportName(requ.getAirportName());

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

            GetActivityCodesResp resp = ThirdPartyLogic.GetActivityCodes(null);
            if (resp.getBaseResponse().isSuccess()) {
                deliveryStock.setActivityCode(resp.getActivityCodes().get(0));
            } else {
                deliveryStock.setActivityCode("KTYX");
            }

            this.deliveryStockService.insert(deliveryStock);

            //修改库存状态
            stock.setStatus(StockStatus.USED.getValue());
            stock.setUpdateTime(DateUtils.getCurrentUnixTime());

            this.stockService.updateById(stock);

        }

        //更新状态为已获取活动码
        delivery.setStatus(DeliveryStatus.DELIVERED.getValue());
        this.deliveryService.updateById(delivery);

        CreateDeliveryResp resp = new CreateDeliveryResp();
        resp.setDeliveryId(delivery.getDeliveryId());
        resp.setStockId(stockDtos.get(0).getStockId());

        //TODO WX发送活动码使用成功模板
        //url：/stock/qrcode/{deliveryId}/{stockId}
        //感谢您在xxxx机场使用金色逸站通用券，点击模板可直接出未用券二维码
        //空铁管家祝您旅途愉快
        //顾客：张宁
        //消费时间：2018.12.24
        TemplateDto temp = TemplateDto.QrcodeUseSuccessed(delivery,stockDtos);
        wxSender.sendNormal(temp);

        return resp;
    }
}
