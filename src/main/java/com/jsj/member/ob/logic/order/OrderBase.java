package com.jsj.member.ob.logic.order;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jsj.member.ob.dto.api.coupon.UseCouponRequ;
import com.jsj.member.ob.dto.api.coupon.UseCouponResp;
import com.jsj.member.ob.dto.api.order.CreateOrderRequ;
import com.jsj.member.ob.dto.api.order.CreateOrderResp;
import com.jsj.member.ob.dto.api.stock.StockDto;
import com.jsj.member.ob.dto.proto.*;
import com.jsj.member.ob.entity.Order;
import com.jsj.member.ob.entity.OrderProduct;
import com.jsj.member.ob.entity.Product;
import com.jsj.member.ob.entity.Stock;
import com.jsj.member.ob.enums.ActivityType;
import com.jsj.member.ob.enums.OrderStatus;
import com.jsj.member.ob.enums.PropertyType;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.CouponLogic;
import com.jsj.member.ob.logic.MemberLogic;
import com.jsj.member.ob.logic.RedpacketLogic;
import com.jsj.member.ob.logic.StockLogic;
import com.jsj.member.ob.rabbitmq.wx.TemplateDto;
import com.jsj.member.ob.rabbitmq.wx.WxSender;
import com.jsj.member.ob.service.*;
import com.jsj.member.ob.tuple.TwoTuple;
import com.jsj.member.ob.utils.DateUtils;
import com.jsj.member.ob.utils.TupleUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
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
    ProductService productService;

    @Autowired
    WxSender wxSender;

    /**
     * 是否允许使用代金券
     */
    private boolean canUseCoupon;

    /**
     * 是否允许创建plus订单
     */
    private boolean canCreatePlus;

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

                st.setUnionId(order.getUnionId());
                st.setOpenId(order.getOpenId());
                st.setProductId(op.getProductId());
                st.setProductSpecId(op.getProductSpecId());
                st.setOrderId(op.getOrderId());
                st.setOrderProductId(op.getOrderProductId());

                stocks.add(st);
            }
        }
        if (stocks.size() == 0 || stocks == null) {
            throw new TipException("参数有误！");
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

        //验证购买条件
        requ.getOrderProductDtos().forEach(o -> {
            Product product = productService.selectById(o.getProductId());
            if (product.getPropertyTypeId().equals(PropertyType.PLUS.getValue())) {
                if (!this.isCanCreatePlus()) {
                    throw new TipException("暂不允许购买Plus券");
                }
                if (requ.getBaseRequ().getJsjId() <= 0) {
                    throw new TipException("不符合购买Plus券条件");
                }
            }
        });

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
     * @return
     */
    public TwoTuple<Double, Double> Exchange(int jsjId, double orderAmount) {

        if (jsjId <= 0) {
            throw new TipException("没有绑定会员，不允许兑换商品。");
        }

        double balance = MemberLogic.StrictChoiceSearch(jsjId);
        if (orderAmount > balance) {

            double payAmount = orderAmount - balance;
            payAmount = new BigDecimal(payAmount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

            double giftAmount = balance;
            return TupleUtils.tuple(payAmount, giftAmount);
        }
        return TupleUtils.tuple(0d, orderAmount);
    }

    /**
     * 商品兑换
     *
     * @param jsjId
     * @param orderAmount
     * @param order
     * @param productName
     * @return
     */
    public double Exchange(int jsjId, double orderAmount, Order order, String productName) {

        if (jsjId <= 0) {
            throw new TipException("没有绑定会员，不允许兑换商品。");
        }

        JSONObject js = new JSONObject();
        js.put("JSJID", jsjId);

        //是否允许购买plus
        boolean isAllowBuy = true;
        try {
            JSONObject jsonObject = MemberLogic.GetCustAsset(js);
            isAllowBuy = jsonObject.getBoolean("IsBuyPlus");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!isAllowBuy) {
            throw new TipException("不允许重复开通plus权益");
        }

        //扣减兑换金额
        StrictChoiceConsumeRequestOuterClass.StrictChoiceConsumeRequest.Builder requ = StrictChoiceConsumeRequestOuterClass.StrictChoiceConsumeRequest.newBuilder();

        BaseRequestOuterClass.BaseRequest.Builder baseRequ = BaseRequestOuterClass.BaseRequest.newBuilder();
        baseRequ.setSourceWay(SourceWayOuterClass.SourceWay.KTGJ);

        requ.setBaseRequest(baseRequ.build());
        requ.setJSJID(jsjId);
        requ.setMoney(orderAmount);
        requ.setOrderType(StrictChoiceOrderTypeOuterClass.StrictChoiceOrderType.StrictChoice);
        requ.setOrderID(order.getOrderId());
        requ.setRemark("严选商品兑换");
        requ.setProjectID(13006);
        requ.setTradeName(productName);

        StrictChoiceConsumeResponseOuterClass.StrictChoiceConsumeResponse resp = MemberLogic.StrictChoiceConsume(requ.build());

        if (!resp.getBaseResponse().getIsSuccess()) {
            throw new TipException(resp.getBaseResponse().getErrorMessage());
        }

        return 0;

    }


    public boolean isCanCreatePlus() {
        return canCreatePlus;
    }

    public void setCanCreatePlus(boolean canCreatePlus) {
        this.canCreatePlus = canCreatePlus;
    }


}
