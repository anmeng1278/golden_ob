package com.jsj.member.ob.logic;

import com.jsj.member.ob.dto.api.coupon.UseCouponRequ;
import com.jsj.member.ob.dto.api.coupon.UseCouponResp;
import com.jsj.member.ob.dto.api.order.CreateOrderRequ;
import com.jsj.member.ob.dto.api.order.CreateOrderResp;
import com.jsj.member.ob.dto.api.order.OrderProduct;
import com.jsj.member.ob.entity.Order;
import com.jsj.member.ob.enums.OrderStatus;
import com.jsj.member.ob.service.OrderProductService;
import com.jsj.member.ob.service.OrderService;
import com.jsj.member.ob.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderLogic {

    public static OrderLogic orderLogic;

    // 初始化的时候，将本类中的sysConfigManager赋值给静态的本类变量
    @PostConstruct
    public void init() {
        orderLogic = this;
        orderLogic.orderService = this.orderService;
    }

    @Autowired
    OrderService orderService;

    @Autowired
    OrderProductService orderProductService;

    /**
     * 创建订单，普通订单、团单、秒杀单、
     *
     * @param requ
     * @return
     */
    public static CreateOrderResp CreateOrder(CreateOrderRequ requ) {

        //TODO 参数校验

        Order order = new Order();

        order.setOpenId(requ.getBaseRequ().getOpenId());
        order.setSeckillId(requ.getSecKillId());
        order.setTeamOrderId(requ.getTeamOrderId());
        order.setTypeId(requ.getOrderType().getValue());
        order.setRemarks(requ.getRemarks());

        order.setStatus(OrderStatus.UNPAY.getValue());
        order.setTypeId(requ.getOrderType().getValue());
        order.setCreateTime(DateUtils.getCurrentUnixTime());
        order.setUpdateTime(DateUtils.getCurrentUnixTime());
        order.setOrderNumber("");

        List<com.jsj.member.ob.entity.OrderProduct> orderProducts = new ArrayList<>();

        //订单应支付金额
        double payAmount = 0d;

        for (OrderProduct op : requ.getOrderProducts()) {

            //TODO 通过商品编号获取商品金额，这里有秒杀商品
            payAmount += 100;

            com.jsj.member.ob.entity.OrderProduct orderProduct = new com.jsj.member.ob.entity.OrderProduct();

            orderProduct.setNumber(op.getNumber());
            orderProduct.setOrderProductId(op.getProductId());
            orderProduct.setProductSizeId(op.getProductSizeId());
            orderProduct.setCreateTime(DateUtils.getCurrentUnixTime());
            orderProduct.setUpdateTime(DateUtils.getCurrentUnixTime());

            orderProduct.setProductId(op.getProductId());
            orderProducts.add(orderProduct);
        }

        //使用优惠券
        if (requ.getWechatCouponId() != null) {

            UseCouponRequ useCouponRequ = new UseCouponRequ();
            useCouponRequ.setUseAmount(payAmount);
            useCouponRequ.setUse(true);
            useCouponRequ.setWechatCouponId(requ.getWechatCouponId().intValue());

            UseCouponResp useCouponResp = CouponLogic.UseCoupon(useCouponRequ);
            if (useCouponResp.getDiscountAmount() > 0) {
                order.setWechatCouponId(useCouponResp.getWechatCouponId());
                order.setCouponPrice(useCouponResp.getDiscountAmount());

                payAmount = useCouponResp.getPayAmount();
            }
        }

        order.setAmount(payAmount);
        order.setPayAmount(payAmount);
        orderLogic.orderService.insert(order);

        order.setOrderNumber((10000 + order.getOrderId()) + "");
        orderLogic.orderService.updateById(order);

        orderProducts.stream().forEach(op -> {
            op.setOrderId(order.getOrderId());
            orderLogic.orderProductService.insert(op);
        });

        if (payAmount == 0) {
            //TODO 调用支付完成方法

        }

        CreateOrderResp resp = new CreateOrderResp();

        resp.setAmount(order.getPayAmount());
        resp.setOrderNumber(order.getOrderNumber());
        resp.setExpiredTime(DateUtils.getCurrentUnixTime() + 60 * 30);

        return resp;

    }

}
