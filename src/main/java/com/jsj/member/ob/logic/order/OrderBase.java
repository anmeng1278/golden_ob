package com.jsj.member.ob.logic.order;

import com.jsj.member.ob.dto.api.coupon.UseCouponRequ;
import com.jsj.member.ob.dto.api.coupon.UseCouponResp;
import com.jsj.member.ob.dto.api.order.CreateOrderRequ;
import com.jsj.member.ob.dto.api.order.CreateOrderResp;
import com.jsj.member.ob.entity.Order;
import com.jsj.member.ob.enums.ActivityType;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.CouponLogic;
import com.jsj.member.ob.service.ActivityOrderService;
import com.jsj.member.ob.service.ActivityService;
import com.jsj.member.ob.service.OrderProductService;
import com.jsj.member.ob.service.OrderService;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public abstract class OrderBase {

    OrderService orderService;
    OrderProductService orderProductService;
    ActivityService activityService;
    ActivityOrderService activityOrderService;

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
     * 订单支付成功
     */
    public void OrderPaySuccess(int orderId) {
        throw new NotImplementedException();
    }


    public boolean isCanUseCoupon() {
        return canUseCoupon;
    }

    public void setCanUseCoupon(boolean canUseCoupon) {
        this.canUseCoupon = canUseCoupon;
    }

    /**
     * 通用验证参数
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

}
