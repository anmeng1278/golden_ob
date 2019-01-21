package com.jsj.member.ob.logic.delivery;

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
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class DeliveryBase {


    /**
     * 配送
     */
    DeliveryService deliveryService;

    /**
     * 库存
     */
    StockService stockService;

    /**
     * 配送库存
     */
    DeliveryStockService deliveryStockService;

    /**
     * 微信消息发送
     */
    WxSender wxSender;

    public DeliveryBase(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    /**
     * 订单类型
     */
    private PropertyType propertyType;

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    //region (public) 使用库存 CreateDelivery

    /**
     * 使用库存
     *
     * @param requ
     * @return
     */
    public abstract CreateDeliveryResp CreateDelivery(CreateDeliveryRequ requ);
    //endregion

    //region (protected) 使用库存 CreateDeliveryCard

    /**
     * 使用库存卡
     *
     * @param requ
     * @return
     */
    protected CreateDeliveryResp CreateDeliveryCard(CreateDeliveryRequ requ) {

        if (requ.getStockDtos().isEmpty()) {
            throw new TipException("使用库存不能为空");
        }
        if (requ.getStockDtos().size() > 1) {
            throw new TipException("只能开通一张会员卡");
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
        if (requ.getEffectiveDate() < DateUtils.getTodayTime()) {
            throw new TipException("生效时间必须大于或等于今天");
        }

        String openId = requ.getBaseRequ().getOpenId();

        //验证是否有未完成的开卡信息
        DeliveryDto unDeliveryDto = this.GetUnDeliveryDto(openId);
        if (unDeliveryDto != null) {
            throw new TipException("您的开卡正在确认中，不能重复开卡……");
        }

        //获取库存
        List<StockDto> stockDtos = requ.getStockDtos();

        Delivery delivery = new Delivery();

        delivery.setStatus(DeliveryStatus.UNDELIVERY.getValue());
        delivery.setUpdateTime(DateUtils.getCurrentUnixTime());
        delivery.setContactName(requ.getContactName());
        delivery.setIdNumber(requ.getIdNumber());
        delivery.setIdTypeId(requ.getIdTypeId());
        delivery.setEffectiveDate(requ.getEffectiveDate());

        delivery.setMobile(Long.parseLong(requ.getMobile()));
        delivery.setCreateTime(DateUtils.getCurrentUnixTime());
        delivery.setOpenId(requ.getBaseRequ().getOpenId());

        delivery.setPropertyTypeId(this.getPropertyType().getValue());
        delivery.setRemarks(requ.getRemarks());
        delivery.setTypeId(DeliveryType.PICKUP.getValue());

        this.deliveryService.insert(delivery);

        //使用库存
        this.UseStocks(stockDtos, delivery);

        CreateDeliveryResp resp = new CreateDeliveryResp();
        resp.setDeliveryId(delivery.getDeliveryId());

        // WX发送开卡确认中模板
        Map card = new HashMap();
        card.put("title","正在为您开卡，请您耐心等待!\n");
        card.put("effectiveDate",DateUtils.formatDateByUnixTime(Long.parseLong(requ.getEffectiveDate() + ""), "yyyy-MM-dd"));
        DeliveryDto deliveryDto = DeliveryLogic.GetDelivery(delivery.getDeliveryId());
        TemplateDto temp = TemplateDto.OpenCardConfirm(deliveryDto,card);
        wxSender.sendNormal(temp);

        // 给微信接收者发送待处理开卡模板
        //生效时间
        String effectiveDate = DateUtils.formatDateByUnixTime(Long.parseLong(requ.getEffectiveDate() + ""), "yyyy-MM-dd");
        Map map = new HashMap();
        map.put("title", String.format("客户已申请开卡,请核对开卡信息!\n\n客户姓名：%s\n身份证号：%s\n手机号码：%s\n生效时间：%s", requ.getContactName(), requ.getIdNumber(), requ.getMobile(), effectiveDate));
        map.put("productName", deliveryDto.getProductDtos().get(0).getProductName());
        List<Wechat> wechats = WechatLogic.GetNotifyWechat();
        for (Wechat wechat : wechats) {
            TemplateDto temp2 = TemplateDto.HandleDelivery(wechat, map, stockDtos);
            wxSender.sendNormal(temp2);
        }

        return resp;
    }
    //endregion

    //region (public) 验证是否有未完成的开卡信息 GetUnDeliveryDto

    /**
     * 验证是否有未完成的开卡信息
     *
     * @param openId
     * @return
     */
    public DeliveryDto GetUnDeliveryDto(String openId) {

        //验证是否有未完成的开卡信息
        DeliveryDto dto = DeliveryLogic.GetUnDeliveryDto(openId, this.propertyType);
        return dto;

    }
    //endregion

    //region (public) 获取使用链接 GetUsedNavigate

    /**
     * 获取使用链接
     *
     * @param openId
     * @return
     */
    public abstract TwoTuple<String, String> GetUsedNavigate(String openId);
    //#endregion

    //region (protected) 使用库存 UseStocks

    /**
     * 使用库存
     *
     * @param stockDtos
     * @param delivery
     */
    protected void UseStocks(List<StockDto> stockDtos, Delivery delivery) {

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
    }
    //endregion

    //region (public) 发货、开卡、创建活动码 OpreationDelivery

    /**
     * 发货、开卡、创建活动码
     *
     * @param requ
     * @return
     */
    public abstract OpreationDeliveryResp OpreationDelivery(OpreationDeliveryRequ requ);
    //endregion

    //region (public) 验证使用库存 validateUsed

    /**
     * 验证使用库存
     *
     * @param stockDtos
     */
    public void validateUsed(List<StockDto> stockDtos) {

        if (stockDtos == null || stockDtos.isEmpty()) {
            throw new TipException("使用库存不能为空");
        }

    }
    //endregion

}
