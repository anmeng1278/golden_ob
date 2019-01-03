package com.jsj.member.ob.logic.order;

import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.dto.api.activity.ActivityDto;
import com.jsj.member.ob.dto.api.activity.ActivityProductDto;
import com.jsj.member.ob.dto.api.order.CreateOrderRequ;
import com.jsj.member.ob.dto.api.order.CreateOrderResp;
import com.jsj.member.ob.dto.api.order.OrderProductDto;
import com.jsj.member.ob.entity.Order;
import com.jsj.member.ob.entity.OrderProduct;
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
 * 秒杀单
 */
@Component
public class OrderExchange extends OrderBase {

    public OrderExchange() {
        super(ActivityType.EXCHANGE);
        super.setCanUseCoupon(false);
    }


    @Autowired
    public void initService(OrderService orderService,
                            OrderProductService orderProductService,
                            ActivityService activityService,
                            ActivityOrderService activityOrderService,
                            WxSender wxSender
    ) {
        super.orderService = orderService;
        super.orderProductService = orderProductService;
        super.activityService = activityService;
        super.activityOrderService = activityOrderService;
        super.wxSender = wxSender;
    }

    @Autowired
    ActivityProductService activityProductService;

    /**
     * 创建秒杀订单
     * 订单金额 = 秒杀金额
     * 不允许使用优惠券
     * 削减秒杀库存
     * 削减规格库存
     * 只允许购买一种商品并且只能购买一个,并且只能购买一次
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

        if (requ.getBaseRequ().getJsjId() <= 0) {
            throw new TipException("会员编号不能为空");
        }

        if (requ.getActivityId() == 0) {
            throw new TipException("活动编号不能为空");
        }

        if (requ.getOrderProductDtos() == null || requ.getOrderProductDtos().isEmpty()) {
            throw new TipException("兑换商品不能为空");
        }
        if (requ.getOrderProductDtos().size() != 1) {
            throw new TipException("只能兑换一个商品");
        }
        if (requ.getNumber() <= 0) {
            requ.setNumber(1);
        }

        //所选商品
        OrderProductDto chooseOrderProduct = requ.getOrderProductDtos().get(0);

        //活动
        ActivityDto activityDto = ActivityLogic.GetActivity(requ.getActivityId());

        //活动商品
        List<ActivityProductDto> activityProductDtos = ActivityLogic.GetActivityProductDtos(requ.getActivityId(), chooseOrderProduct.getProductSpecId());

        if (activityProductDtos.isEmpty()) {
            throw new TipException(String.format("没有发现活动商品，请稍后重试。活动编号：%d", activityDto.getActivityId()));
        }

        if (activityDto.getDeleteTime() != null) {
            throw new TipException("活动结束啦");
        }
        if (activityDto.getBeginTime() > DateUtils.getCurrentUnixTime()) {
            throw new TipException("活动未开始");
        }
        if (activityDto.getEndTime() < DateUtils.getCurrentUnixTime()) {
            throw new TipException("活动结束啦");
        }

        if (activityDto.getActivityType() != this.getActivityType()) {
            throw new TipException("当前活动非兑换活动");
        }


        //组织订单实体
        Order order = new Order();

        order.setOpenId(requ.getBaseRequ().getOpenId());
        order.setTypeId(this.getActivityType().getValue());
        order.setRemarks(requ.getRemarks());
        order.setActivityId(activityDto.getActivityId());

        order.setStatus(OrderStatus.UNPAY.getValue());
        order.setCreateTime(DateUtils.getCurrentUnixTime());
        order.setUpdateTime(DateUtils.getCurrentUnixTime());
        order.setExpiredTime(DateUtils.getCurrentUnixTime() + Constant.ORDER_EXPIRED_TIME);
        order.setOrderUniqueCode(StringUtils.UUID32());

        //用于创建商品订单
        List<OrderProduct> orderProducts = new ArrayList<>();

        //用于削减库存
        List<OrderProductDto> orderProductDtos = new ArrayList<>();

        //购买份数
        int number = requ.getNumber();

        //订单金额
        double orderAmount = 0d;

        for (ActivityProductDto apd : activityProductDtos) {

            if (apd.getStockCount() < number) {
                throw new TipException("商品售罄啦");
            }

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

            orderAmount += apd.getSalePrice() * number;

            //削减活动商品库存
            ActivityLogic.ReductionActivityProductStock(apd.getActivityId(), apd.getProductId(), apd.getProductSpecId(), number);
        }

        //削减规格库存
        ProductLogic.ReductionProductSpecStock(orderProductDtos, this.getActivityType(), null);

        //TODO 商品兑换
        double payAmount = super.Exchange(requ.getBaseRequ().getJsjId(), orderAmount, order);

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
            this.PaySuccessed(order.getOrderId(), null);
        }

        CreateOrderResp resp = new CreateOrderResp();

        resp.setAmount(order.getPayAmount());
        resp.setOrderId(order.getOrderId());
        resp.setOrderUniqueCode(order.getOrderUniqueCode());
        resp.setExpiredTime(order.getExpiredTime());
        resp.setSuccess(true);

        return resp;

    }

    @Override
    public CreateOrderResp CalculateOrder(CreateOrderRequ requ) {
        throw new TipException("方法暂未实现");
    }
}
