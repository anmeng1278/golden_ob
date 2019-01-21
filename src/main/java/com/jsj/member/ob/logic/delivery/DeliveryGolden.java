package com.jsj.member.ob.logic.delivery;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.dto.api.delivery.*;
import com.jsj.member.ob.dto.api.stock.StockDto;
import com.jsj.member.ob.entity.Delivery;
import com.jsj.member.ob.entity.DeliveryStock;
import com.jsj.member.ob.entity.Stock;
import com.jsj.member.ob.enums.DeliveryStatus;
import com.jsj.member.ob.enums.PropertyType;
import com.jsj.member.ob.enums.StockStatus;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.DeliveryLogic;
import com.jsj.member.ob.rabbitmq.wx.WxSender;
import com.jsj.member.ob.service.DeliveryService;
import com.jsj.member.ob.service.DeliveryStockService;
import com.jsj.member.ob.service.StockService;
import com.jsj.member.ob.tuple.TwoTuple;
import com.jsj.member.ob.utils.TupleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class DeliveryGolden extends DeliveryBase {

    public DeliveryGolden() {
        super(PropertyType.GOLDEN);
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
        return super.CreateDeliveryCard(requ);
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

        DeliveryDto unDeliveryDto = this.GetUnDeliveryDto(openId);
        if (unDeliveryDto != null) {
            return TupleUtils.tuple("您的开卡正在确认中，不能重复开卡……", "");
        }

        return TupleUtils.tuple("", "/stock/use3");
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
        if (!dto.getDeliveryStatus().equals(DeliveryStatus.UNDELIVERY)) {
            throw new TipException(String.format("卡状态为：%s，不允许操作",
                    dto.getDeliveryStatus().getMessage(this.getPropertyType())));
        }

        Delivery delivery = deliveryService.selectById(dto.getDeliveryId());
        List<DeliveryStock> deliveryStocks = deliveryStockService.selectList(new EntityWrapper<DeliveryStock>()
                .where("delivery_id = {0}", delivery.getDeliveryId()));

        delivery.setStatus(DeliveryStatus.SIGNED.getValue());
        deliveryService.updateById(delivery);

        //更新发货状态
        deliveryStocks.forEach(ds -> {
            Stock stock = stockService.selectById(ds.getStockId());

            stock.setStatus(StockStatus.SIGNED.getValue());
            stockService.updateById(stock);
        });

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
            throw new TipException("只能开通一张会员卡");
        }
        if (!stockDtos.get(0).getProductDto().getPropertyType().equals(this.getPropertyType())) {
            throw new TipException("参数错误");
        }
    }
    //endregion
}
