package com.jsj.member.ob.logic.order;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.dto.api.activity.ActivityDto;
import com.jsj.member.ob.dto.api.activity.ActivityProductDto;
import com.jsj.member.ob.dto.api.order.CreateOrderRequ;
import com.jsj.member.ob.dto.api.order.CreateOrderResp;
import com.jsj.member.ob.dto.api.order.OrderProductDto;
import com.jsj.member.ob.entity.ActivityOrder;
import com.jsj.member.ob.entity.Order;
import com.jsj.member.ob.entity.OrderProduct;
import com.jsj.member.ob.enums.ActivityOrderStatus;
import com.jsj.member.ob.enums.ActivityType;
import com.jsj.member.ob.enums.OrderStatus;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.ActivityLogic;
import com.jsj.member.ob.logic.ProductLogic;
import com.jsj.member.ob.rabbitmq.wx.WxSender;
import com.jsj.member.ob.service.*;
import com.jsj.member.ob.utils.DateUtils;
import com.jsj.member.ob.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 团购单
 */
@Component
public class OrderGroupon extends OrderBase {
    public OrderGroupon() {
        super(ActivityType.GROUPON);
        super.setCanUseCoupon(false);
    }


    @Autowired
    public void initService(OrderService orderService,
                            OrderProductService orderProductService,
                            ActivityService activityService,
                            ActivityOrderService activityOrderService,
                            ProductService productService,
                            WxSender wxSender
    ) {
        super.orderService = orderService;
        super.orderProductService = orderProductService;
        super.activityService = activityService;
        super.activityOrderService = activityOrderService;
        super.productService = productService;
        super.wxSender = wxSender;
    }
    @Autowired
    ActivityProductService activityProductService;

    /**
     * 创建团单,应该在组团完成后自动创建
     * 订单金额 = 组团金额
     * 不允许使用优惠券
     * 削减规格库存
     * 组团成功后调用
     *
     * @param requ
     * @return
     */
    @Override
    @Transactional(Constant.DBTRANSACTIONAL)
    public CreateOrderResp CreateOrder(CreateOrderRequ requ) {

        //通用验证
        super.validateCreateRequ(requ);

        if (requ.getActivityId() == 0) {
            throw new TipException("活动编号不能为空");
        }

        if (requ.getActivityOrderId() == 0) {
            throw new TipException("活动单号不能为空");
        }

        CreateOrderResp resp = new CreateOrderResp();

        //获取团单
        ActivityOrder activityOrder = activityOrderService.selectById(requ.getActivityOrderId());
        if (activityOrder.getStatus() != ActivityOrderStatus.SUCCESS.getValue()) {
            throw new TipException(String.format("团单状态不允许创建订单，团单编号：%d，团单状态：%s", activityOrder.getActivityOrderId(), ActivityOrderStatus.valueOf(activityOrder.getStatus()).getMessage()));
        }

        //活动
        ActivityDto activityDto = ActivityLogic.GetActivity(requ.getActivityId());

        //活动商品
        List<ActivityProductDto> activityProductDtos = ActivityLogic.GetActivityProductDtos(requ.getActivityId());

        if (activityDto.getDeleteTime() != null) {
            throw new TipException("活动结束啦");
        }
        if (activityDto.getBeginTime() > DateUtils.getCurrentUnixTime()) {
            throw new TipException("活动未开始");
        }
        if (activityDto.getEndTime() < DateUtils.getCurrentUnixTime()) {
            throw new TipException("活动结束啦");
        }
        if (activityProductDtos.isEmpty()) {
            throw new TipException(String.format("没有发现活动商品，请稍后重试。活动编号：%d", activityDto.getActivityId()));
        }
        if (activityDto.getActivityType() != this.getActivityType()) {
            throw new TipException("当前活动非团购活动");
        }

        List<ActivityOrder> activityOrders = activityOrderService.selectList(new EntityWrapper<ActivityOrder>().where("activity_order_id={0} or parent_activity_order_id={0}", requ.getActivityOrderId()));
        for (ActivityOrder to : activityOrders) {

            //组织订单实体
            Order order = new Order();

            order.setOpenId(to.getOpenId());
            order.setUnionId(to.getUnionId());
            order.setTypeId(this.getActivityType().getValue());
            order.setRemarks(requ.getRemarks());
            order.setActivityId(activityDto.getActivityId());
            order.setActivityOrderId(activityOrder.getActivityOrderId());

            order.setStatus(OrderStatus.UNPAY.getValue());
            order.setCreateTime(DateUtils.getCurrentUnixTime());
            order.setUpdateTime(DateUtils.getCurrentUnixTime());
            order.setExpiredTime(DateUtils.getCurrentUnixTime() + Constant.ORDER_EXPIRED_TIME);
            order.setOrderUniqueCode(StringUtils.UUID32());

            order.setOrderSourceId(requ.getSourceType().getValue());

            //用于创建商品订单
            List<OrderProduct> orderProducts = new ArrayList<>();

            //用于削减库存
            List<OrderProductDto> orderProductDtos = new ArrayList<>();

            //购买份数
            int number = 1;

            for (ActivityProductDto apd : activityProductDtos) {

                //用于创建商品订单
                OrderProduct orderProduct = new OrderProduct();

                orderProduct.setNumber(number);
                orderProduct.setProductSpecId(apd.getProductSpecId());
                orderProduct.setCreateTime(DateUtils.getCurrentUnixTime());
                orderProduct.setUpdateTime(DateUtils.getCurrentUnixTime());

                orderProduct.setProductId(apd.getProductId());
                orderProducts.add(orderProduct);

                //用于削减库存
                OrderProductDto orderProductDto = new OrderProductDto();
                orderProductDto.setProductId(apd.getProductId());
                orderProductDto.setProductSpecId(apd.getProductSpecId());
                orderProductDto.setNumber(number);

                orderProductDtos.add(orderProductDto);

                //削减活动商品库存
                ActivityLogic.ReductionActivityProductStock(apd.getActivityId(), apd.getProductId(), apd.getProductSpecId(), number);
            }

            //消减活动库存
            ActivityLogic.ReductionActivityStock(activityDto.getActivityId(), number, requ.getActivityOrderId());

            //削减规格库存
            ProductLogic.ReductionProductSpecStock(orderProductDtos, this.getActivityType(), null);

            //订单金额
            double orderAmount = activityDto.getSalePrice() * number;
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

        }

        //更新团单状态
        activityOrders.stream().forEach(to -> {
            to.setStatus(ActivityOrderStatus.ORDERSUCCESS.getValue());
            to.setUpdateTime(DateUtils.getCurrentUnixTime());
            activityOrderService.updateById(to);
        });

        resp.setSuccess(true);
        resp.setMessage("订单创建成功");

        return resp;

    }

    @Override
    public CreateOrderResp CalculateOrder(CreateOrderRequ requ) {
        throw new TipException("方法暂未实现");
    }
}
