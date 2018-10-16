package com.jsj.member.ob.logic.order;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.dto.api.order.CreateOrderRequ;
import com.jsj.member.ob.dto.api.order.CreateOrderResp;
import com.jsj.member.ob.dto.api.product.Product;
import com.jsj.member.ob.entity.Order;
import com.jsj.member.ob.entity.OrderProduct;
import com.jsj.member.ob.entity.Seckill;
import com.jsj.member.ob.enums.OrderStatus;
import com.jsj.member.ob.enums.OrderType;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.ProductLogic;
import com.jsj.member.ob.service.OrderProductService;
import com.jsj.member.ob.service.OrderService;
import com.jsj.member.ob.service.SeckillService;
import com.jsj.member.ob.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 秒杀单
 */
@Component
public class OrderSeckill extends OrderBase {

    public OrderSeckill() {
        super(OrderType.SECKILL);
    }

    @Autowired
    SeckillService seckillService;

    @Autowired
    public void initService(OrderService orderService, OrderProductService orderProductService) {
        super.orderService = orderService;
        super.orderProductService = orderProductService;
    }

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

        if (requ.getOrderProducts().size() > 1) {
            throw new TipException("秒杀订单暂不支持多种商品");
        }

        com.jsj.member.ob.dto.api.order.OrderProduct opt = requ.getOrderProducts().get(0);

        if (opt.getNumber() != 1) {
            throw new TipException("秒杀商品只能购买一个");
        }

        Product product = ProductLogic.GetProduct(opt.getProductId());
        if (product.getStockCount() <= 0) {
            throw new TipException("商品库存不足");
        }

        //产品类型判断
        if (product.getSeckillId() == 0) {
            throw new TipException("非秒杀商品不允许下单");
        }

        Seckill seckill = seckillService.selectById(product.getSeckillId());
        if (seckill.getBeginTime() > DateUtils.getCurrentUnixTime()) {
            throw new TipException("秒杀活动未开始");
        }
        if (seckill.getEndTime() < DateUtils.getCurrentUnixTime()) {
            throw new TipException("秒杀活动已结束");
        }
        if (!seckill.getIfpass()) {
            throw new TipException("秒杀活动已结束");
        }
        if (requ.getWechatCouponId() > 0) {
            throw new TipException("秒杀订单暂不支持优惠券");
        }
        if (seckill.getStockCount() <= 0) {
            throw new TipException("库存不足，秒杀失败");
        }

        //重复购买判断
        EntityWrapper<Order> orderWrapper = new EntityWrapper<>();
        orderWrapper.where("open_id = {0}", requ.getBaseRequ().getOpenId());
        orderWrapper.where("type_id = {0}", OrderType.SECKILL.getValue());
        orderWrapper.where("exists( select * from _order_product as op where op.product_id = {0} and op.order_id = _order.order_id )", product.getProductId());

        if (orderService.selectCount(orderWrapper) > 0) {
            throw new TipException("秒杀商品只能购买一次");
        }

        //组织订单实体
        Order order = new Order();

        order.setOpenId(requ.getBaseRequ().getOpenId());
        order.setTypeId(this.getOrderType().getValue());
        order.setRemarks(requ.getRemarks());
        order.setSeckillId(requ.getSecKillId());

        order.setStatus(OrderStatus.UNPAY.getValue());
        order.setCreateTime(DateUtils.getCurrentUnixTime());
        order.setUpdateTime(DateUtils.getCurrentUnixTime());
        order.setExpiredTime(DateUtils.getCurrentUnixTime() + Constant.ORDER_EXPIRED_TIME);
        order.setOrderNumber("");

        List<OrderProduct> orderProducts = new ArrayList<>();

        //订单应支付金额
        double orderAmount = product.getSecPrice();

        com.jsj.member.ob.entity.OrderProduct orderProduct = new com.jsj.member.ob.entity.OrderProduct();

        orderProduct.setNumber(opt.getNumber());
        orderProduct.setOrderProductId(opt.getProductId());
        orderProduct.setProductSizeId(opt.getProductSizeId());
        orderProduct.setCreateTime(DateUtils.getCurrentUnixTime());
        orderProduct.setUpdateTime(DateUtils.getCurrentUnixTime());

        orderProduct.setProductId(opt.getProductId());
        orderProducts.add(orderProduct);

        //订单金额
        order.setAmount(orderAmount);
        order.setPayAmount(orderAmount);
        orderService.insert(order);

        order.setOrderNumber((10000 + order.getOrderId()) + "");
        orderService.updateById(order);

        //更新订单商品中的订单编号
        orderProducts.stream().forEach(op -> {
            op.setOrderId(order.getOrderId());
            orderProductService.insert(op);
        });

        //消减秒杀库存
        seckill.setStockCount(seckill.getStockCount() - 1);
        seckillService.updateById(seckill);

        //削减库存
        ProductLogic.ReductionProductStock(requ.getOrderProducts());

        CreateOrderResp resp = new CreateOrderResp();

        resp.setAmount(order.getPayAmount());
        resp.setOrderNumber(order.getOrderNumber());
        resp.setExpiredTime(order.getExpiredTime());
        resp.setSuccess(true);

        return resp;

    }
}
