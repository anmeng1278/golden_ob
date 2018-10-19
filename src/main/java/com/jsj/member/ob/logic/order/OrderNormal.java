package com.jsj.member.ob.logic.order;

import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.dto.api.order.CreateOrderRequ;
import com.jsj.member.ob.dto.api.order.CreateOrderResp;
import com.jsj.member.ob.dto.api.order.OrderProductDto;
import com.jsj.member.ob.dto.api.product.ProductSpecDto;
import com.jsj.member.ob.entity.Order;
import com.jsj.member.ob.entity.OrderProduct;
import com.jsj.member.ob.enums.ActivityType;
import com.jsj.member.ob.enums.OrderStatus;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.ProductLogic;
import com.jsj.member.ob.service.ActivityOrderService;
import com.jsj.member.ob.service.ActivityService;
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
        super(ActivityType.NORMAL);
        super.setCanUseCoupon(true);
    }

    @Autowired
    public void initService(OrderService orderService,
                            OrderProductService orderProductService,
                            ActivityService activityService,
                            ActivityOrderService activityOrderService
    ) {
        super.orderService = orderService;
        super.orderProductService = orderProductService;
        super.activityService = activityService;
        super.activityOrderService = activityOrderService;
    }

    /**
     * 创建订单
     * 订单金额 = sum(订单规格售价) - 优惠券金额
     * 允许使用优惠券
     * 削减规格库存
     * 允许购买多种商品
     *
     * @param requ
     * @return
     */
    @Override
    @Transactional(Constant.DBTRANSACTIONAL)
    public CreateOrderResp CreateOrder(CreateOrderRequ requ) {

        //通用验证
        super.validateCreateRequ(requ);

        //参数校验
        if (org.apache.commons.lang3.StringUtils.isBlank(requ.getBaseRequ().getOpenId())) {
            throw new TipException("用户编号不能为空");
        }
        if (requ.getOrderProductDtos() == null || requ.getOrderProductDtos().size() == 0) {
            throw new TipException("购买商品不能为空");
        }

        //组织订单实体
        Order order = new Order();

        order.setOpenId(requ.getBaseRequ().getOpenId());
        order.setTypeId(this.getActivityType().getValue());
        order.setRemarks(requ.getRemarks());

        order.setStatus(OrderStatus.UNPAY.getValue());
        order.setCreateTime(DateUtils.getCurrentUnixTime());
        order.setUpdateTime(DateUtils.getCurrentUnixTime());
        order.setExpiredTime(DateUtils.getCurrentUnixTime() + Constant.ORDER_EXPIRED_TIME);

        List<com.jsj.member.ob.entity.OrderProduct> orderProducts = new ArrayList<>();

        //订单应支付金额
        double orderAmount = 0d;

        for (OrderProductDto op : requ.getOrderProductDtos()) {

            ProductSpecDto productSpecDto = ProductLogic.GetProductSpec(op.getProductSpecId());
            OrderProduct orderProduct = new OrderProduct();

            orderProduct.setNumber(op.getNumber());
            orderProduct.setOrderProductId(op.getProductId());
            orderProduct.setProductSpecId(op.getProductSpecId());
            orderProduct.setCreateTime(DateUtils.getCurrentUnixTime());
            orderProduct.setUpdateTime(DateUtils.getCurrentUnixTime());

            orderProduct.setProductId(op.getProductId());
            orderProducts.add(orderProduct);

            //获取规格金额
            orderAmount += productSpecDto.getSalePrice();

        }
        //削减规格库存
        ProductLogic.ReductionProductSpecStock(requ.getOrderProductDtos(), this.getActivityType(), null);

        //使用优惠券后的支付金额
        double payAmount = super.UseCoupon(requ.getWechatCouponId(), orderAmount, order);
        //支付金额
        order.setPayAmount(payAmount);
        //订单金额
        order.setAmount(orderAmount);
        orderService.insert(order);

        //更新订单商品中的订单编号
        orderProducts.stream().forEach(op -> {
            op.setOrderId(order.getOrderId());
            orderProductService.insert(op);
        });

        if (payAmount == 0) {
            this.OrderPaySuccess(order.getOrderId());
        }

        CreateOrderResp resp = new CreateOrderResp();

        resp.setAmount(order.getPayAmount());
        resp.setOrderId(order.getOrderId());
        resp.setExpiredTime(order.getExpiredTime());
        resp.setSuccess(true);

        return resp;

    }

    /**
     * 订单支付成功
     *
     * @param orderId
     */
    @Override
    public void OrderPaySuccess(int orderId) {

        //修改订单状态
        //添加库存
        //发送短信等

    }

}
