package com.jsj.member.ob.logic.delivery;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.dto.api.delivery.*;
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
import com.jsj.member.ob.logic.ThirdPartyLogic;
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

@Component
public class DeliveryActivity extends DeliveryBase {

    public DeliveryActivity() {
        super(PropertyType.ACTIVITYCODE);
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
        if (requ.getStockDtos().size() > 1) {
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
        String unionId = requ.getBaseRequ().getUnionId();

        //判断是否存在未使用的活动码
        DeliveryDto unDeliveryDto = this.GetUnDeliveryDto(unionId);
        if (unDeliveryDto != null) {
            throw new TipException("您还有未使用的次卡");
        }

        //获取库存
        List<StockDto> stockDtos = requ.getStockDtos();

        Delivery delivery = new Delivery();

        delivery.setStatus(DeliveryStatus.UNDELIVERY.getValue());
        delivery.setUpdateTime(DateUtils.getCurrentUnixTime());
        delivery.setContactName(requ.getContactName());

        delivery.setMobile(Long.parseLong(requ.getMobile()));
        delivery.setCreateTime(DateUtils.getCurrentUnixTime());
        delivery.setOpenId(openId);
        delivery.setUnionId(unionId);
        delivery.setFlightNumber(requ.getFlightNumber());

        delivery.setPropertyTypeId(this.getPropertyType().getValue());
        delivery.setRemarks(requ.getRemarks());
        delivery.setTypeId(DeliveryType.PICKUP.getValue());
        delivery.setAirportCode(requ.getAirportCode());
        delivery.setAirportName(requ.getAirportName());

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

        // WX发送活动码使用成功模板
        TemplateDto temp = TemplateDto.QrcodeUseSuccessed(delivery, stockDtos);
        wxSender.sendNormal(temp);

        return resp;
    }
    //endregion

    //region (public) 获取使用链接 GetUsedNavigate

    /**
     * 获取使用链接
     *
     * @param unionId
     * @return
     */
    @Override
    public TwoTuple<String, String> GetUsedNavigate(String unionId) {

        //判断是否存在未使用的活动码
        DeliveryDto unDeliveryDto = this.GetUnDeliveryDto(unionId);
        if (unDeliveryDto != null) {
            List<DeliveryStock> deliveryStocks = DeliveryLogic.GetDeliveryStocks(unDeliveryDto.getDeliveryId());
            if (!deliveryStocks.isEmpty()) {
                String url = String.format("/stock/qrcode/%d/%d", deliveryStocks.get(0).getDeliveryId(), deliveryStocks.get(0).getStockId());
                return TupleUtils.tuple("您还有未使用的次卡", url);
            }
        }

        return TupleUtils.tuple("", "/stock/use2");
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
            throw new TipException(String.format("活动码状态为：%s，不允许操作",
                    dto.getDeliveryStatus().getMessage(this.getPropertyType())));
        }

        Delivery delivery = deliveryService.selectById(dto.getDeliveryId());
        List<DeliveryStock> deliveryStocks = deliveryStockService.selectList(new EntityWrapper<DeliveryStock>()
                .where("delivery_id = {0}", delivery.getDeliveryId()));

        String remark = delivery.getRemarks();


        //更新发货状态
        for (DeliveryStock ds : deliveryStocks) {

            //获取验证码
            GetActivityCodesResp resp = ThirdPartyLogic.GetActivityCodes(null);
            if (resp.getBaseResponse().isSuccess()) {
                ds.setActivityCode(resp.getActivityCodes().get(0));
                //获取成功
                if (remark != null) {
                    remark += String.format("获取成功：%s", this.getPropertyType().getMessage());
                } else {
                    remark = String.format("获取成功：%s", this.getPropertyType().getMessage());
                }
            } else {
                ds.setActivityCode("JSYX");
                //获取失败
                if (remark != null) {
                    remark += String.format("获取失败：%s", resp.getBaseResponse().getErrorMessage());
                } else {
                    remark = String.format("获取失败：%s", resp.getBaseResponse().getErrorMessage());
                }
            }
            this.deliveryStockService.updateById(ds);

            Stock stock = stockService.selectById(ds.getStockId());
            stock.setStatus(StockStatus.SENT.getValue());
            stockService.updateById(stock);
        }

        //更新状态为已获取
        delivery.setRemarks(remark);
        delivery.setStatus(DeliveryStatus.DELIVERED.getValue());
        this.deliveryService.updateById(delivery);

        OpreationDeliveryResp resp = new OpreationDeliveryResp();
        resp.setSuccess(true);

        return resp;

    }

    //endregion

    //region (public) 验证使用参数 validateUsed

    /**
     * 验证使用参数
     *
     * @param stockDtos
     */
    @Override
    public void validateUsed(List<StockDto> stockDtos) {
        super.validateUsed(stockDtos);
        if (stockDtos.size() > 1) {
            throw new TipException("只能使用一个活动码");
        }
        if (!stockDtos.get(0).getProductDto().getPropertyType().equals(this.getPropertyType())) {
            throw new TipException("参数错误");
        }
    }
    //endregion

}
