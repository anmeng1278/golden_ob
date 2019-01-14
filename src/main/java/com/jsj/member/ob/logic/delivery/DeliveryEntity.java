package com.jsj.member.ob.logic.delivery;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.dto.api.delivery.*;
import com.jsj.member.ob.dto.api.stock.StockDto;
import com.jsj.member.ob.entity.Delivery;
import com.jsj.member.ob.entity.DeliveryStock;
import com.jsj.member.ob.entity.Stock;
import com.jsj.member.ob.entity.Wechat;
import com.jsj.member.ob.enums.DeliveryStatus;
import com.jsj.member.ob.enums.DeliveryType;
import com.jsj.member.ob.enums.PropertyType;
import com.jsj.member.ob.enums.StockStatus;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.DeliveryLogic;
import com.jsj.member.ob.logic.WechatLogic;
import com.jsj.member.ob.rabbitmq.wx.TemplateDto;
import com.jsj.member.ob.rabbitmq.wx.WxSender;
import com.jsj.member.ob.service.DeliveryService;
import com.jsj.member.ob.service.DeliveryStockService;
import com.jsj.member.ob.service.StockService;
import com.jsj.member.ob.tuple.TwoTuple;
import com.jsj.member.ob.utils.DateUtils;
import com.jsj.member.ob.utils.TupleUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component
public class DeliveryEntity extends DeliveryBase {

    public DeliveryEntity() {
        super(PropertyType.ENTITY);
    }

    @Autowired
    public void initService(DeliveryService deliveryService,
                            StockService stockService,
                            DeliveryStockService deliveryStockService,
                            WxSender wxSender) {
        super.deliveryService = deliveryService;
        super.stockService = stockService;
        super.deliveryStockService = deliveryStockService;
        super.wxSender = wxSender;
    }


    //region (public) 使用库存 CreateDelivery

    /**
     * 使用库存
     *
     * @param requ
     * @return
     */
    @Override
    @Transactional(Constant.DBTRANSACTIONAL)
    public CreateDeliveryResp CreateDelivery(CreateDeliveryRequ requ) {

        if (requ.getStockDtos().isEmpty()) {
            throw new TipException("使用库存不能为空");
        }
        if (StringUtils.isEmpty(requ.getContactName())) {
            throw new TipException("联系人不能为空");
        }
        if (!com.jsj.member.ob.utils.StringUtils.isMobile(requ.getMobile())) {
            throw new TipException("联系手机格式错误");
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
            if (StringUtils.isEmpty(requ.getAirportCode())) {
                throw new TipException("请选择自提网点");
            }
        }

        //获取库存
        List<StockDto> stockDtos = requ.getStockDtos();

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

        //使用库存
        this.UseStocks(stockDtos, delivery);

        CreateDeliveryResp resp = new CreateDeliveryResp();
        resp.setDeliveryId(delivery.getDeliveryId());

        // WX发送实物使用成功模板
        Map map = TemplateDto.GetProduct(stockDtos);
        TemplateDto temp1 = TemplateDto.EntityUseSuccessed(delivery, map, stockDtos);
        wxSender.sendNormal(temp1);

        //给微信接收者发送待处理发货模板
        if (delivery.getTypeId() == DeliveryType.DISTRIBUTE.getValue()) {
            map.put("title","客户已申请配送,请尽快安排发货!\n");
        }
        if (delivery.getTypeId() == DeliveryType.PICKUP.getValue()) {
            map.put("title","客户已申请自提，请核对并确认客户自提网点及时间，尽快安排相关对接工作!\n");
        }
        List<Wechat> wechats = WechatLogic.GetNotifyWechat();
        for (Wechat wechat : wechats) {
            TemplateDto temp2 = TemplateDto.HandleDelivery(wechat, map, stockDtos);
            wxSender.sendNormal(temp2);
        }

        return resp;
    }
    //endregion

    //region (public) 获取使用链接 GetUsedNavigate

    /**
     * 获取使用链接
     *
     * @param openId
     * @return
     */
    @Override
    public TwoTuple<String, String> GetUsedNavigate(String openId) {
        return TupleUtils.tuple("", "/stock/use1");
    }

    //endregion

    //region (public) 实物发货 OpreationDelivery

    /**
     * 实物发货
     *
     * @param requ
     * @return
     */
    @Override
    public OpreationDeliveryResp OpreationDelivery(OpreationDeliveryRequ requ) {

        DeliveryDto dto = DeliveryLogic.GetDelivery(requ.getDeliveryId());
        if (!dto.getDeliveryStatus().equals(DeliveryStatus.UNDELIVERY)) {
            throw new TipException(String.format("配送状态为：%s，不允许操作", dto.getDeliveryStatus().getMessage()));
        }

        if (dto.getDeliveryType().equals(DeliveryType.DISTRIBUTE)) {
            if (StringUtils.isEmpty(requ.getExpressNumber())) {
                throw new TipException("快递号不能为空");
            }
        }

        Delivery delivery = deliveryService.selectById(dto.getDeliveryId());
        List<DeliveryStock> deliveryStocks = deliveryStockService.selectList(new EntityWrapper<DeliveryStock>()
                .where("delivery_id = {0}", delivery.getDeliveryId()));

        delivery.setExpressNumber(requ.getExpressNumber());

        //配送改成已发货
        //自提改成已签收
        if (dto.getDeliveryType().equals(DeliveryType.DISTRIBUTE)) {
            delivery.setStatus(DeliveryStatus.DELIVERED.getValue());
        } else {
            delivery.setStatus(DeliveryStatus.SIGNED.getValue());
        }
        delivery.setUpdateTime(DateUtils.getCurrentUnixTime());
        deliveryService.updateById(delivery);

        //更新发货状态
        deliveryStocks.forEach(ds -> {
            Stock stock = stockService.selectById(ds.getStockId());
            stock.setStatus(StockStatus.SENT.getValue());
            stockService.updateById(stock);
        });

        OpreationDeliveryResp resp = new OpreationDeliveryResp();
        resp.setSuccess(true);

        return resp;
    }
}
