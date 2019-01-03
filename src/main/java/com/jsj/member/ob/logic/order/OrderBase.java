package com.jsj.member.ob.logic.order;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jsj.member.ob.dto.api.coupon.UseCouponRequ;
import com.jsj.member.ob.dto.api.coupon.UseCouponResp;
import com.jsj.member.ob.dto.api.order.CreateOrderRequ;
import com.jsj.member.ob.dto.api.order.CreateOrderResp;
import com.jsj.member.ob.dto.api.stock.StockDto;
import com.jsj.member.ob.dto.proto.NotifyModelOuterClass;
import com.jsj.member.ob.entity.Order;
import com.jsj.member.ob.entity.OrderProduct;
import com.jsj.member.ob.entity.Stock;
import com.jsj.member.ob.enums.ActivityType;
import com.jsj.member.ob.enums.OrderStatus;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.CouponLogic;
import com.jsj.member.ob.logic.RedpacketLogic;
import com.jsj.member.ob.logic.StockLogic;
import com.jsj.member.ob.rabbitmq.wx.TemplateDto;
import com.jsj.member.ob.rabbitmq.wx.WxSender;
import com.jsj.member.ob.service.ActivityOrderService;
import com.jsj.member.ob.service.ActivityService;
import com.jsj.member.ob.service.OrderProductService;
import com.jsj.member.ob.service.OrderService;
import com.jsj.member.ob.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * 削减库存规则
 * 1.普通商品 削减商品规格库存
 * 2.组合商品 削减活动库存+削减活动商品库存+商品规格库存
 * 3.团购     削减活动库存+削减活动商品库存+商品规格库存
 * 4.秒杀     削减活动商品库存+商品规格库存
 * 5.兑换     削减活动商品库存+商品规格库存
 */

public abstract class OrderBase {

    OrderService orderService;
    OrderProductService orderProductService;
    ActivityService activityService;
    ActivityOrderService activityOrderService;

    @Autowired
    WxSender wxSender;

    /**
     * 是否允许使用代金券
     */
    private boolean canUseCoupon;

    public OrderBase(ActivityType ot) {
        this.activityType = ot;
    }

    /**
     * 订单类型
     */
    private ActivityType activityType;

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    /**
     * 创建订单
     *
     * @param requ
     * @return
     */
    public abstract CreateOrderResp CreateOrder(CreateOrderRequ requ);

    /**
     * 计算商品应付金额
     *
     * @param requ
     * @return
     */
    public abstract CreateOrderResp CalculateOrder(CreateOrderRequ requ);

    /**
     * 订单支付成功
     */
    public void PaySuccessed(int orderId, NotifyModelOuterClass.NotifyModel notifyModel) {

        //订单信息
        //已取消状态下，可能会完成支付
        Order order = orderService.selectById(orderId);
        if (order.getStatus() != OrderStatus.UNPAY.getValue() && order.getStatus() != OrderStatus.CANCEL.getValue()) {
            return;
        }

        //订单商品
        List<OrderProduct> orderProducts = orderProductService.selectList(new EntityWrapper<OrderProduct>().where("order_id={0}", orderId));
        if (orderProducts.isEmpty()) {
            return;
        }

        //添加库存
        List<Stock> stocks = new ArrayList<>();
        for (OrderProduct op : orderProducts) {
            for (int i = 0; i < op.getNumber(); i++) {

                Stock st = new Stock();

                st.setOpenId(order.getOpenId());
                st.setProductId(op.getProductId());
                st.setProductSpecId(op.getProductSpecId());
                st.setOrderId(op.getOrderId());
                st.setOrderProductId(op.getOrderProductId());

                stocks.add(st);
            }
        }
        StockLogic.AddOrderStock(stocks);

        //红包
        RedpacketLogic.CreateOrderRedpacket(orderId);

        //修改订单状态
        order.setStatus(OrderStatus.PAYSUCCESS.getValue());
        order.setPayTime(DateUtils.getCurrentUnixTime());
        if (notifyModel != null) {
            order.setTransactionId(notifyModel.getTradeOrderID());
        }

        orderService.updateById(order);

        //支付成功发送微信推送
        List<StockDto> stockDtos = new ArrayList<>();
        for (Stock stock : stocks) {
            StockDto stockDto = StockLogic.ToDto(stock);
            stockDtos.add(stockDto);
        }
        Map map = TemplateDto.GetProduct(stockDtos);
        TemplateDto temp = TemplateDto.NewOrderPaySuccessed(order, map);
        wxSender.sendNormal(temp);

    }


    public boolean isCanUseCoupon() {
        return canUseCoupon;
    }

    public void setCanUseCoupon(boolean canUseCoupon) {
        this.canUseCoupon = canUseCoupon;
    }

    /**
     * 通用验证参数
     *
     * @param requ
     */
    public void validateCreateRequ(CreateOrderRequ requ) {

        if (!this.isCanUseCoupon() && requ.getWechatCouponId() > 0) {
            throw new TipException(String.format("当前订单类型不允许使用优惠券，订单类型：%s", this.activityType.getMessage()));
        }


    }

    /**
     * 使用优惠券
     *
     * @param wechatCouponId
     * @param orderAmount
     * @param order
     * @return
     */
    public double UseCoupon(Integer wechatCouponId, double orderAmount, Order order) {

        if (wechatCouponId == null || wechatCouponId == 0) {
            return orderAmount;
        }

        //使用优惠券
        UseCouponRequ useCouponRequ = new UseCouponRequ();
        useCouponRequ.setUseAmount(orderAmount);
        useCouponRequ.setUse(true);
        useCouponRequ.setWechatCouponId(wechatCouponId);

        UseCouponResp useCouponResp = CouponLogic.UseCoupon(useCouponRequ);
        if (useCouponResp.getDiscountAmount() > 0) {
            order.setWechatCouponId(useCouponResp.getWechatCouponId());
            order.setCouponPrice(useCouponResp.getDiscountAmount());
            orderAmount = useCouponResp.getPayAmount();
        }

        return orderAmount;

    }

    /**
     * 商品兑换
     *
     * @param jsjId
     * @param orderAmount
     * @param order
     * @return
     */
    public double Exchange(int jsjId, double orderAmount, Order order) {

        if (jsjId <= 0) {
            throw new TipException("没有绑定会员，不允许兑换商品。");
        }

        //TODO 扣减金额
        return 0;

    }
}
