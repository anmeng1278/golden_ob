package com.jsj.member.ob.logic.order;

import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.dto.BaseRequ;
import com.jsj.member.ob.dto.api.activity.ActivityDto;
import com.jsj.member.ob.dto.api.activity.ActivityProductDto;
import com.jsj.member.ob.dto.api.delivery.CreateDeliveryRequ;
import com.jsj.member.ob.dto.api.order.CreateOrderRequ;
import com.jsj.member.ob.dto.api.order.CreateOrderResp;
import com.jsj.member.ob.dto.api.order.OrderProductDto;
import com.jsj.member.ob.dto.api.product.ProductSpecDto;
import com.jsj.member.ob.dto.api.stock.StockDto;
import com.jsj.member.ob.dto.proto.CustomerInfoNewOuterClass;
import com.jsj.member.ob.dto.proto.CustomerInformationRequestOuterClass;
import com.jsj.member.ob.dto.proto.CustomerInformationResponseOuterClass;
import com.jsj.member.ob.dto.proto.NotifyModelOuterClass;
import com.jsj.member.ob.entity.Order;
import com.jsj.member.ob.entity.OrderProduct;
import com.jsj.member.ob.enums.ActivityType;
import com.jsj.member.ob.enums.DeliveryType;
import com.jsj.member.ob.enums.OrderStatus;
import com.jsj.member.ob.enums.PropertyType;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.*;
import com.jsj.member.ob.rabbitmq.wx.WxSender;
import com.jsj.member.ob.service.*;
import com.jsj.member.ob.tuple.TwoTuple;
import com.jsj.member.ob.utils.DateUtils;
import com.jsj.member.ob.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 秒杀单
 */
@Component
public class OrderExchange extends OrderBase {

    public OrderExchange() {
        super(ActivityType.EXCHANGE);
        super.setCanUseCoupon(false);
        super.setCanCreatePlus(true);
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
        if (requ.getBaseRequ().getJsjId() <= 0) {
            throw new TipException("参数不合法，用户jsjId为空");
        }
        if (org.apache.commons.lang3.StringUtils.isBlank(requ.getBaseRequ().getOpenId())) {
            throw new TipException("参数不合法，用户openId为空");
        }
        if (org.apache.commons.lang3.StringUtils.isBlank(requ.getBaseRequ().getUnionId())) {
            throw new TipException("参数不合法，用户unionId为空");
        }

        if (requ.getActivityId() == 0) {
            throw new TipException("活动编号不能为空");
        }

        if (requ.getOrderProductDtos() == null || requ.getOrderProductDtos().isEmpty()) {
            throw new TipException("兑换商品不能为空");
        }

        //活动
        ActivityDto activityDto = ActivityLogic.GetActivity(requ.getActivityId());

        //活动商品
        List<ActivityProductDto> activityProductDtos = ActivityLogic.GetActivityProductDtos(requ.getActivityId());

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
        order.setUnionId(requ.getBaseRequ().getUnionId());
        order.setTypeId(this.getActivityType().getValue());
        order.setRemarks(requ.getRemarks());
        order.setActivityId(activityDto.getActivityId());

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

        //订单金额
        double orderAmount = 0d;
        String productName = "";

        int plusNumber = 0;
        for (OrderProductDto op : requ.getOrderProductDtos()) {

            Optional<ActivityProductDto> first = activityProductDtos.stream().filter(ap -> ap.getProductId().equals(op.getProductId()) &&
                    ap.getProductSpecId().equals(op.getProductSpecId())).findFirst();

            if (!first.isPresent()) {
                throw new TipException(String.format("商品不在兑换活动中，商品编号：%d，活动编号：%d", op.getProductId(), requ.getActivityId()));
            }

            if (first.get().getStockCount() < op.getNumber()) {
                throw new TipException(String.format("商品\"%s\"库存不足，当前库存：%d", first.get().getProductDto().getProductName(), first.get().getStockCount()));
            }

            if (first.get().getProductDto().getPropertyType().equals(PropertyType.PLUS)) {
                plusNumber += op.getNumber();
            }

            //判断是否多个plus权益
            if (plusNumber > 1) {
                throw new TipException("Plus权益只能兑换一个");
            }

            //用于创建商品订单
            OrderProduct orderProduct = new OrderProduct();

            orderProduct.setNumber(op.getNumber());
            orderProduct.setProductSpecId(op.getProductSpecId());
            orderProduct.setCreateTime(DateUtils.getCurrentUnixTime());
            orderProduct.setUpdateTime(DateUtils.getCurrentUnixTime());

            orderProduct.setProductId(op.getProductId());
            orderProducts.add(orderProduct);

            //用于削减库存
            OrderProductDto orderProductDto = new OrderProductDto();
            orderProductDto.setProductId(op.getProductId());
            orderProductDto.setProductSpecId(op.getProductSpecId());
            orderProductDto.setNumber(op.getNumber());

            orderProductDtos.add(orderProductDto);

            //获取商品规格
            ProductSpecDto productSpecDto = ProductLogic.GetProductSpec(op.getProductSpecId());

            orderAmount += productSpecDto.getSalePrice() * op.getNumber();

            //削减活动商品库存
            ActivityLogic.ReductionActivityProductStock(requ.getActivityId(), op.getProductId(), op.getProductSpecId(), op.getNumber());

            productName += productSpecDto.getProductDto().getProductName() + " ";
        }

        //削减规格库存
        ProductLogic.ReductionProductSpecStock(orderProductDtos, this.getActivityType(), null);

        double payAmount = 0d;
        //支付金额
        order.setPayAmount(payAmount);

        //订单金额
        order.setAmount(orderAmount);
        order.setEquityPrice(orderAmount);
        order.setRemarks("商品兑换");

        orderService.insert(order);

        //商品兑换
        super.Exchange(requ.getBaseRequ().getJsjId(), orderAmount, order, productName);

        //更新订单商品中的订单编号
        orderProducts.stream().forEach(op -> {
            op.setOrderId(order.getOrderId());
            orderProductService.insert(op);
        });

        if (payAmount == 0) {
            this.PaySuccessed(order.getOrderId(),
                    null,
                    requ.getBaseRequ().getJsjId(),
                    requ.getBaseRequ().getUnionId(),
                    requ.getBaseRequ().getOpenId());
        }


        CreateOrderResp resp = new CreateOrderResp();

        resp.setAmount(order.getPayAmount());
        resp.setOrderId(order.getOrderId());
        resp.setOrderUniqueCode(order.getOrderUniqueCode());
        resp.setExpiredTime(order.getExpiredTime());
        resp.setSuccess(true);
        resp.setMessage("兑换成功");

        return resp;

    }

    /**
     * 支付成功，兑换plus
     *
     * @param orderId
     * @param notifyModel
     * @param jsjId
     * @param unionId
     * @param openId
     */
    public void PaySuccessed(int orderId, NotifyModelOuterClass.NotifyModel notifyModel, int jsjId, String unionId, String openId) {

        super.PaySuccessed(orderId, notifyModel);

        List<StockDto> stockDtos = StockLogic.GetStocksByOrderId(orderId);

        Optional<StockDto> first = stockDtos.stream().filter(x -> x.getProductDto().getPropertyType().equals(PropertyType.PLUS)).findFirst();
        if (!first.isPresent()) {
            return;
        }

        //查询会员信息
        CustomerInformationRequestOuterClass.CustomerInformationRequest.Builder builder = CustomerInformationRequestOuterClass.CustomerInformationRequest.newBuilder();
        builder.setJSJID(jsjId + "");

        CustomerInformationResponseOuterClass.CustomerInformationResponse customerInformation = MemberLogic.CustomerInformation(builder.build());

        if (customerInformation.getBaseResponse().getIsSuccess() && customerInformation.getListCount() > 0) {

            CustomerInfoNewOuterClass.CustomerInfoNew userInfo = customerInformation.getList(0);

            //String cardId = userInfo.getCardID();
            //String cardTypeIdName = userInfo.getCardTypeName();
            String mobile = userInfo.getContactmeans();
            //String cardInvalidDate = userInfo.getCardInvalidDate();
            String customerName = userInfo.getCustomerName();

            if (org.apache.commons.lang3.StringUtils.isEmpty(mobile)) {
                mobile = "15210000000";
            }
            if (org.apache.commons.lang3.StringUtils.isEmpty(customerName)) {
                customerName = "未填写";
            }

            CreateDeliveryRequ requ = new CreateDeliveryRequ();
            requ.setMobile(mobile);
            requ.setContactName(customerName);
            requ.setDeliveryType(DeliveryType.PICKUP);
            requ.setPropertyType(PropertyType.PLUS);

            requ.getStockDtos().add(first.get());

            BaseRequ baseRequ = new BaseRequ();
            baseRequ.setUnionId(unionId);
            baseRequ.setOpenId(openId);
            baseRequ.setJsjId(jsjId);

            requ.setBaseRequ(baseRequ);

            DeliveryLogic.CreateDelivery(requ);

        }
    }

    @Override
    public CreateOrderResp CalculateOrder(CreateOrderRequ requ) {

        //通用验证
        super.validateCreateRequ(requ);

        //会员编号
        int jsjId = requ.getBaseRequ().getJsjId();

        //参数校验
        if (jsjId <= 0) {
            throw new TipException("参数不合法，用户jsjId为空");
        }
        if (org.apache.commons.lang3.StringUtils.isBlank(requ.getBaseRequ().getOpenId())) {
            throw new TipException("参数不合法，用户openId为空");
        }
        if (org.apache.commons.lang3.StringUtils.isBlank(requ.getBaseRequ().getUnionId())) {
            throw new TipException("参数不合法，用户unionId为空");
        }
        if (requ.getOrderProductDtos() == null || requ.getOrderProductDtos().size() == 0) {
            throw new TipException("购买商品不能为空");
        }
        //活动
        ActivityDto activityDto = ActivityLogic.GetActivity(requ.getActivityId());

        //活动商品
        List<ActivityProductDto> activityProductDtos = ActivityLogic.GetActivityProductDtos(requ.getActivityId());

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

        //订单应支付金额
        double orderAmount = 0d;

        for (OrderProductDto op : requ.getOrderProductDtos()) {

            Optional<ActivityProductDto> first = activityProductDtos.stream().filter(ap -> ap.getProductId().equals(op.getProductId()) &&
                    ap.getProductSpecId().equals(op.getProductSpecId())).findFirst();

            if (!first.isPresent()) {
                throw new TipException(String.format("商品不在兑换活动中，商品编号：%d，活动编号：%d", op.getProductId(), requ.getActivityId()));
            }

            if (first.get().getStockCount() < op.getNumber()) {
                throw new TipException(String.format("商品\"%s\"库存不足，当前库存：%d", first.get().getProductDto().getProductName(), first.get().getStockCount()));
            }

            //获取商品规格
            ProductSpecDto productSpecDto = ProductLogic.GetProductSpec(op.getProductSpecId());
            if (productSpecDto.getDeleteTime() != null) {
                throw new TipException("不允许兑换，所选商品规格已被删除");
            }

            //获取规格金额
            orderAmount += productSpecDto.getSalePrice() * op.getNumber();
        }

        //原价
        orderAmount = new BigDecimal(orderAmount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        CreateOrderResp resp = new CreateOrderResp();
        TwoTuple<Double, Double> twoTuple = super.Exchange(jsjId, orderAmount);

        double payAmount = twoTuple.first;
        double giftAmount = twoTuple.second;

        resp.setAmount(payAmount);
        resp.setCouponAmount(0d);
        resp.setGiftAmount(giftAmount);
        resp.setOriginalAmount(orderAmount);
        resp.setSuccess(true);

        return resp;

    }
}
