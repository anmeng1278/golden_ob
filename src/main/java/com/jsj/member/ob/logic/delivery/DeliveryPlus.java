package com.jsj.member.ob.logic.delivery;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.dto.api.delivery.*;
import com.jsj.member.ob.dto.api.stock.StockDto;
import com.jsj.member.ob.dto.api.wechat.WechatDto;
import com.jsj.member.ob.dto.proto.AddServiceRequestOuterClass;
import com.jsj.member.ob.dto.proto.AddServiceResponseOuterClass;
import com.jsj.member.ob.dto.proto.UpgradeServiceOuterClass;
import com.jsj.member.ob.entity.Delivery;
import com.jsj.member.ob.entity.DeliveryStock;
import com.jsj.member.ob.entity.Stock;
import com.jsj.member.ob.enums.DeliveryStatus;
import com.jsj.member.ob.enums.DeliveryType;
import com.jsj.member.ob.enums.PropertyType;
import com.jsj.member.ob.enums.StockStatus;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.DeliveryLogic;
import com.jsj.member.ob.logic.MemberLogic;
import com.jsj.member.ob.logic.WechatLogic;
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

@Component
public class DeliveryPlus extends DeliveryBase {
    public DeliveryPlus() {
        super(PropertyType.PLUS);
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
        if (requ.getStockDtos().size() > 1) {
            throw new TipException("Plus只能使用一个");
        }
        if (requ.getBaseRequ().getJsjId() <= 0) {
            throw new TipException("必须是会员才能开通");
        }

        String openId = requ.getBaseRequ().getOpenId();

        //判断是否存在未使用的活动码
        DeliveryDto unDeliveryDto = this.GetUnDeliveryDto(openId);
        if (unDeliveryDto != null) {
            throw new TipException("您还有未使用的Plus权益");
        }

        //获取库存
        List<StockDto> stockDtos = requ.getStockDtos();

        Delivery delivery = new Delivery();

        delivery.setContactName(requ.getContactName());
        delivery.setMobile(Long.parseLong(requ.getMobile()));
        delivery.setStatus(DeliveryStatus.UNDELIVERY.getValue());
        delivery.setUpdateTime(DateUtils.getCurrentUnixTime());

        delivery.setCreateTime(DateUtils.getCurrentUnixTime());
        delivery.setOpenId(requ.getBaseRequ().getOpenId());
        delivery.setPropertyTypeId(this.getPropertyType().getValue());
        delivery.setTypeId(DeliveryType.PICKUP.getValue());

        this.deliveryService.insert(delivery);

        //使用库存
        this.UseStocks(stockDtos, delivery);

        //生成活动码
        OpreationDeliveryRequ opreationDeliveryRequ = new OpreationDeliveryRequ();
        opreationDeliveryRequ.setDeliveryId(delivery.getDeliveryId());

        this.OpreationDelivery(opreationDeliveryRequ);

        CreateDeliveryResp resp = new CreateDeliveryResp();
        resp.setDeliveryId(delivery.getDeliveryId());
        resp.setStockId(stockDtos.get(0).getStockId());

        //TODO WX发送权益开通成功模板
        //        //您的plus权益已开通成功，即刻生效。

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

        //判断是否存在未开通的权益
        DeliveryDto unDeliveryDto = this.GetUnDeliveryDto(openId);
        if (unDeliveryDto != null) {
            return TupleUtils.tuple("您的权益正在确认中，不能重复开通……", "");
        }

        return TupleUtils.tuple("", "/stock/use4");
    }

    //endregion

    //region (public) 发货、开卡、创建活动码 OpreationDelivery

    /**
     * 发货、开卡、创建活动码
     *
     * @param requ
     * @return
     */
    @Override
    public OpreationDeliveryResp OpreationDelivery(OpreationDeliveryRequ requ) {

        DeliveryDto dto = DeliveryLogic.GetDelivery(requ.getDeliveryId());
        WechatDto wechatDto = WechatLogic.GetWechat(dto.getOpenId());

        if (!dto.getDeliveryStatus().equals(DeliveryStatus.UNDELIVERY)) {
            throw new TipException(String.format("权益状态为：%s，不允许操作",
                    dto.getDeliveryStatus().getMessage(this.getPropertyType())));
        }

        Delivery delivery = deliveryService.selectById(dto.getDeliveryId());
        List<DeliveryStock> deliveryStocks = deliveryStockService.selectList(new EntityWrapper<DeliveryStock>()
                .where("delivery_id = {0}", delivery.getDeliveryId()));

        String remark = delivery.getRemarks();

        //更新发货状态
        for (DeliveryStock ds : deliveryStocks) {

            AddServiceRequestOuterClass.AddServiceRequest.Builder createRequ = AddServiceRequestOuterClass.AddServiceRequest.newBuilder();

            createRequ.setCustomerId(wechatDto.getJsjid());
            createRequ.setOperapersonId(1);
            createRequ.setOperaTime(DateUtils.getCurrentUnixTime());
            createRequ.setServiceId(UpgradeServiceOuterClass.UpgradeService.Plus500);

            AddServiceResponseOuterClass.AddServiceResponse resp = MemberLogic.CreatePlus(createRequ.build());

            if (resp.getBaseResponse().getIsSuccess()) {

                if (!resp.getResultFlag()) {
                    throw new TipException(resp.getMessage());
                }
                //获取成功
                if (remark != null) {
                    remark += String.format("开通成功：%s", this.getPropertyType().getMessage());
                } else {
                    remark = String.format("开通成功：%s", this.getPropertyType().getMessage());
                }
            } else {
                throw new TipException(resp.getBaseResponse().getErrorMessage());
            }
            this.deliveryStockService.updateById(ds);

            Stock stock = stockService.selectById(ds.getStockId());
            stock.setStatus(StockStatus.SIGNED.getValue());
            stockService.updateById(stock);
        }

        //更新状态为开通成功
        delivery.setRemarks(remark);
        delivery.setStatus(DeliveryStatus.SIGNED.getValue());
        this.deliveryService.updateById(delivery);

        OpreationDeliveryResp resp = new OpreationDeliveryResp();
        resp.setSuccess(true);

        return resp;

    }

    //endregion


    //region (public) 验证使用参数 validateUsed

    /**
     * 验证使用参数
     * @param stockDtos
     */
    @Override
    public void validateUsed(List<StockDto> stockDtos) {
        super.validateUsed(stockDtos);
        if (stockDtos.size() > 1) {
            throw new TipException("只能开通一个Plus权益");
        }
        if (!stockDtos.get(0).getProductDto().getPropertyType().equals(this.getPropertyType())) {
            throw new TipException("参数错误");
        }
    }
    //endregion
}
