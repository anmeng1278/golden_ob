package com.jsj.member.ob.logic.order;

import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.dto.api.coupon.UseCouponRequ;
import com.jsj.member.ob.dto.api.coupon.UseCouponResp;
import com.jsj.member.ob.dto.api.order.CreateOrderRequ;
import com.jsj.member.ob.dto.api.order.CreateOrderResp;
import com.jsj.member.ob.dto.api.order.OrderProduct;
import com.jsj.member.ob.dto.api.product.Product;
import com.jsj.member.ob.entity.Order;
import com.jsj.member.ob.enums.OrderStatus;
import com.jsj.member.ob.enums.OrderType;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.CouponLogic;
import com.jsj.member.ob.logic.ProductLogic;
import com.jsj.member.ob.service.OrderProductService;
import com.jsj.member.ob.service.OrderService;
import com.jsj.member.ob.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 普通订单
 */
@Component
public class OrderNormal extends OrderBase {

    public OrderNormal() {
        super(OrderType.NORMAL);
    }

    @Autowired
    public void initService(OrderService orderService, OrderProductService orderProductService) {
        super.orderService = orderService;
        super.orderProductService = orderProductService;
    }

    /**
     * 创建订单
     *
     * @param requ
     * @return
     */
    @Override
    @Transactional(Constant.DBTRANSACTIONAL)
    public CreateOrderResp CreateOrder(CreateOrderRequ requ) {

        //参数校验
        if (org.apache.commons.lang3.StringUtils.isBlank(requ.getBaseRequ().getOpenId())) {
            throw new TipException("用户编号不能为空");
        }
        if (requ.getOrderProducts() == null || requ.getOrderProducts().size() == 0) {
            throw new TipException("购买商品不能为空");
        }

        //组织订单实体
        Order order = new Order();

        order.setOpenId(requ.getBaseRequ().getOpenId());
        order.setTypeId(this.getOrderType().getValue());
        order.setRemarks(requ.getRemarks());

        order.setStatus(OrderStatus.UNPAY.getValue());
        order.setCreateTime(DateUtils.getCurrentUnixTime());
        order.setUpdateTime(DateUtils.getCurrentUnixTime());
        order.setExpiredTime(DateUtils.getCurrentUnixTime() + Constant.ORDER_EXPIRED_TIME);
        order.setOrderNumber("");

        List<com.jsj.member.ob.entity.OrderProduct> orderProducts = new ArrayList<>();

        //订单应支付金额
        double orderAmount = 0d;

        for (OrderProduct op : requ.getOrderProducts()) {

            Product product = ProductLogic.GetProduct(op.getProductId());
            if (op.getNumber() > product.getStockCount()) {
                throw new TipException(String.format("库存不足，下单失败，商品名称：%s", product.getProductName()));
            }

            orderAmount += product.getSalePrice();

            com.jsj.member.ob.entity.OrderProduct orderProduct = new com.jsj.member.ob.entity.OrderProduct();

            orderProduct.setNumber(op.getNumber());
            orderProduct.setOrderProductId(op.getProductId());
            orderProduct.setProductSizeId(op.getProductSizeId());
            orderProduct.setCreateTime(DateUtils.getCurrentUnixTime());
            orderProduct.setUpdateTime(DateUtils.getCurrentUnixTime());

            orderProduct.setProductId(op.getProductId());
            orderProducts.add(orderProduct);
        }

        //订单金额
        order.setAmount(orderAmount);
        double payAmount = orderAmount;

        //使用优惠券
        if (requ.getWechatCouponId() > 0) {

            UseCouponRequ useCouponRequ = new UseCouponRequ();
            useCouponRequ.setUseAmount(orderAmount);
            useCouponRequ.setUse(true);
            useCouponRequ.setWechatCouponId(requ.getWechatCouponId());

            UseCouponResp useCouponResp = CouponLogic.UseCoupon(useCouponRequ);
            if (useCouponResp.getDiscountAmount() > 0) {
                order.setWechatCouponId(useCouponResp.getWechatCouponId());
                order.setCouponPrice(useCouponResp.getDiscountAmount());
                payAmount = useCouponResp.getPayAmount();
            }
        }

        //支付金额
        order.setPayAmount(payAmount);
        orderService.insert(order);

        order.setOrderNumber((10000 + order.getOrderId()) + "");
        orderService.updateById(order);

        //更新订单商品中的订单编号
        orderProducts.stream().forEach(op -> {
            op.setOrderId(order.getOrderId());
            orderProductService.insert(op);
        });


        //削减库存
        ProductLogic.ReductionProductStock(requ.getOrderProducts());

        if (payAmount == 0) {
            this.OrderPaySuccess(order.getOrderNumber());
        }

        CreateOrderResp resp = new CreateOrderResp();

        resp.setAmount(order.getPayAmount());
        resp.setOrderNumber(order.getOrderNumber());
        resp.setExpiredTime(order.getExpiredTime());
        resp.setSuccess(true);

        return resp;

    }

    /**
     * 订单支付成功
     *
     * @param orderNumber
     */
    @Override
    public void OrderPaySuccess(String orderNumber) {

        //修改订单状态
        //添加库存
        //发送短信等

    }

}
