package com.jsj.member.ob.controller.api;

import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.dto.api.Request;
import com.jsj.member.ob.dto.api.Response;
import com.jsj.member.ob.dto.api.activity.ActivityDto;
import com.jsj.member.ob.dto.api.activity.ActivityProductDto;
import com.jsj.member.ob.dto.api.order.CreateOrderRequ;
import com.jsj.member.ob.dto.api.order.CreateOrderResp;
import com.jsj.member.ob.dto.api.order.OrderDto;
import com.jsj.member.ob.dto.api.order.OrderProductDto;
import com.jsj.member.ob.dto.api.product.ProductDto;
import com.jsj.member.ob.dto.api.product.ProductSpecDto;
import com.jsj.member.ob.dto.mini.ProductDetailRequ;
import com.jsj.member.ob.dto.mini.ProductDetailResp;
import com.jsj.member.ob.enums.ActivityType;
import com.jsj.member.ob.enums.SecKillStatus;
import com.jsj.member.ob.enums.SourceType;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.*;
import com.jsj.member.ob.logic.order.OrderBase;
import com.jsj.member.ob.logic.order.OrderFactory;
import com.jsj.member.ob.utils.DateUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("${webconfig.virtualPath}/mini")
public class ApiProductController {


    //region (public) 商品详情 productDetail

    /**
     * 商品详情
     *
     * @param requ
     * @return
     */
    @ApiOperation(value = "商品详情")
    @RequestMapping(value = "/productDetail", method = RequestMethod.POST)
    public Response<ProductDetailResp> productDetail(@ApiParam(value = "请求实体", required = true)
                                                     @RequestBody @Validated Request<ProductDetailRequ> requ) {

        ActivityType activityType = requ.getRequestBody().getActivityType();

        switch (activityType) {

            case NORMAL:
                return Response.ok(this.normalProduct(requ.getRequestBody()));

            case SECKILL:
                return Response.ok(this.secKillProduct(requ.getRequestBody()));

            case COMBINATION:
                return Response.ok(this.combProduct(requ.getRequestBody()));

            case EXCHANGE:
                return Response.ok(this.exchangeProduct(requ.getRequestBody()));

            default:
                throw new TipException("方法暂未实现");
        }

    }
    //endregion

    //region (private) 普通商品详情 normalProduct

    /**
     * 普通商品详情
     *
     * @param requ
     * @return
     */
    private ProductDetailResp normalProduct(ProductDetailRequ requ) {

        ProductDetailResp resp = new ProductDetailResp();

        int productId = requ.getProductId();
        if (productId <= 0) {
            throw new TipException("商品编号不能为空");
        }

        //商品详情
        ProductDto info = ProductLogic.GetProduct(productId);
        if (info == null) {
            throw new TipException("没有找到商品信息");
        }

        //库存
        int stockCount = 0;
        //售价
        double salePrice = info.getSalePrice();

        if (info.getProductSpecDtos() != null) {
            stockCount = info.getProductSpecDtos().stream().mapToInt(ProductSpecDto::getStockCount).sum();
        }

        List<Integer> productIds = new ArrayList<>();
        productIds.add(productId);

        if (info.getProductImgDtos() != null && info.getProductImgDtos().size() > 0) {
            String imgUrl = info.getProductImgDtos().get(0).getImgPath();
            resp.setImgUrl(imgUrl);
        }
        resp.setStockCount(stockCount);
        resp.setProductDto(info);
        resp.setSalePrice(salePrice);

        return resp;
    }
    //endregion

    //region (public) 秒杀商品详情 secKillProduct

    /**
     * 组合商品详情
     *
     * @param requ
     * @return
     */
    public ProductDetailResp secKillProduct(ProductDetailRequ requ) {

        ProductDetailResp resp = new ProductDetailResp();

        if (requ.getActivityId() <= 0) {
            throw new TipException("活动编号不能为空");
        }

        if (requ.getProductSpecId() <= 0) {
            throw new TipException("商品规格编号不能为空");
        }

        int activityId = requ.getActivityId();
        int productSpecId = requ.getProductSpecId();

        //秒杀活动信息
        ActivityDto info = ActivityLogic.GetActivity(activityId);
        if (!info.getIfpass()) {
            throw new TipException("没有找到活动信息");
        }

        //非组合商品，不允许进此链接
        if (!info.getActivityType().equals(ActivityType.SECKILL)) {
            throw new TipException("非秒杀活动");
        }

        //活动中的商品
        List<ActivityProductDto> productDtos = ActivityLogic.GetActivityProductDtos(activityId, productSpecId);
        if (productDtos.size() == 0) {
            throw new TipException("没有找到活动商品信息");
        }
        ProductDto productDto = productDtos.get(0).getProductDto();
        ProductSpecDto productSpecDto = ProductLogic.GetProductSpec(productSpecId);

        //库存
        int stockCount = Math.min(productDtos.get(0).getStockCount(), productSpecDto.getStockCount());

        //未开始
        int flag = 0;
        if (info.getBeginTime() > DateUtils.getCurrentUnixTime()) {
            //未开始
            flag = 0;
        } else if (DateUtils.getCurrentUnixTime() < info.getEndTime()) {
            //进行中
            if (stockCount <= 0) {
                //已售罄
                flag = 1;
            } else {
                flag = 2;
            }
        } else {
            flag = -1;
        }

        resp.setProductDto(productDto);
        resp.setSalePrice(productDtos.get(0).getSalePrice());
        resp.setStockCount(stockCount);

        resp.setFlag(flag);
        resp.setActivityId(activityId);
        resp.setProductId(productDto.getProductId());
        resp.setProductSpecId(productSpecId);

        return resp;

    }
    //endregion

    //region (public) 组合商品详情 combProduct

    /**
     * 组合商品详情
     *
     * @param requ
     * @return
     */
    public ProductDetailResp combProduct(ProductDetailRequ requ) {

        ProductDetailResp resp = new ProductDetailResp();

        if (requ.getActivityId() <= 0) {
            throw new TipException("活动编号不能为空");
        }

        int activityId = requ.getActivityId();

        ActivityDto info = ActivityLogic.GetActivity(activityId);

        //非组合商品，不允许进此链接
        if (!info.getActivityType().equals(ActivityType.COMBINATION)) {
            throw new TipException("非组合活动");
        }

        //活动中的商品
        List<ActivityProductDto> productDtos = ActivityLogic.GetActivityProductDtos(activityId);

        int stockCount = info.getStockCount();
        if (!productDtos.isEmpty()) {
            for (ActivityProductDto apd : productDtos) {
                stockCount = Math.min(stockCount, apd.getStockCount());
                ProductSpecDto productSpecDto = ProductLogic.GetProductSpec(apd.getProductSpecId());
                stockCount = Math.min(stockCount, productSpecDto.getStockCount());
            }
        }

        resp.setActivityDto(info);
        resp.setActivityProductDtos(productDtos);
        resp.setStockCount(stockCount);

        return resp;
    }
    //endregion

    //region (public) 兑换商品详情 exchangeProduct

    /**
     * 兑换商品活动详情
     *
     * @param requ
     * @return
     */
    public ProductDetailResp exchangeProduct(ProductDetailRequ requ) {

        ProductDetailResp resp = new ProductDetailResp();

        if (requ.getActivityId() <= 0) {
            throw new TipException("活动编号不能为空");
        }

        if (requ.getProductSpecId() <= 0) {
            throw new TipException("商品规格编号不能为空");
        }


        int activityId = requ.getActivityId();
        int productSpecId = requ.getProductSpecId();

        //兑换活动信息
        ActivityDto info = ActivityLogic.GetActivity(activityId);
        if (!info.getIfpass()) {
            throw new TipException("没有找到活动信息");
        }

        //非兑换商品，不允许进此链接
        if (!info.getActivityType().equals(ActivityType.EXCHANGE)) {
            throw new TipException("非兑换活动");
        }

        //活动中的商品
        List<ActivityProductDto> productDtos = ActivityLogic.GetActivityProductDtos(activityId, productSpecId);
        if (productDtos.size() == 0) {
            throw new TipException("没有找到活动商品信息");
        }

        ProductDto productDto = productDtos.get(0).getProductDto();
        ProductSpecDto productSpecDto = ProductLogic.GetProductSpec(productSpecId);

        //库存
        int stockCount = Math.min(productDtos.get(0).getStockCount(), productSpecDto.getStockCount());


        double balance = MemberLogic.StrictChoiceSearch(requ.getJsjId());

        if (productDto.getProductImgDtos() != null && productDto.getProductImgDtos().size() > 0) {
            String imgUrl = productDto.getProductImgDtos().get(0).getImgPath();
            resp.setImgUrl(imgUrl);
        }

        resp.setStockCount(stockCount);
        resp.setProductDto(productDto);
        resp.setActivityId(activityId);
        resp.setBalance(balance);

        return resp;
    }
    //endregion

    //region (public) 创建订单 createOrder

    /**
     * 创建订单
     *
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "创建订单")
    @RequestMapping(value = "/createOrder", method = RequestMethod.POST)
    @Transactional(Constant.DBTRANSACTIONAL)
    public Response<CreateOrderResp> createOrder(@ApiParam(value = "请求实体", required = true)
                                                 @RequestBody @Validated Request<CreateOrderRequ> requ) throws Exception {


        ActivityType activityType = requ.getRequestBody().getActivityType();

        //创建订单响应实体8
        CreateOrderResp resp;

        //创建秒杀订单
        if (activityType == ActivityType.SECKILL) {
            resp = this.createSecKillorder(requ.getRequestBody());
            if (!resp.isSuccess()) {
                throw new TipException(resp.getMessage());
            }
        } else {

            OrderBase orderBase = OrderFactory.GetInstance(requ.getRequestBody().getActivityType());
            resp = orderBase.CreateOrder(requ.getRequestBody());

            //来源购物车购买
            if ("cart".equals(requ.getRequestBody().getFrom())) {

                String openId = requ.getRequestBody().getBaseRequ().getOpenId();
                String unionId = requ.getRequestBody().getBaseRequ().getUnionId();

                for (OrderProductDto op : requ.getRequestBody().getOrderProductDtos()) {
                    //删除购物车购买商品
                    CartLogic.AddUpdateCartProduct(openId,
                            unionId,
                            op.getProductId(),
                            op.getProductSpecId(),
                            0,
                            "update",
                            0,
                            ActivityType.NORMAL
                    );
                }
            }
        }

        return Response.ok(resp);

    }
    //endregion

    //region (private) 创建秒杀订单 createSecKillorder

    /**
     * 创建秒杀订单
     *
     * @param requ
     * @return
     */
    private CreateOrderResp createSecKillorder(CreateOrderRequ requ) {

        CreateOrderResp resp = new CreateOrderResp();

        if (requ.getOrderProductDtos() == null || requ.getOrderProductDtos().isEmpty()) {
            throw new TipException("请选择秒杀商品");
        }

        if (requ.getOrderProductDtos().size() != 1) {
            throw new TipException("秒杀商品只能购买一个");
        }

        int activityId = requ.getActivityId();
        int productId = requ.getOrderProductDtos().get(0).getProductId();
        int productSpecId = requ.getOrderProductDtos().get(0).getProductSpecId();

        String openId = requ.getBaseRequ().getOpenId();
        String unionId = requ.getBaseRequ().getUnionId();

        int createTime = DateUtils.getCurrentUnixTime();
        SecKillStatus secKillStatus = ActivityLogic.RedisKill(activityId, productId, productSpecId, openId, unionId, SourceType.AWKMINI);

        if (secKillStatus.equals(SecKillStatus.SUCCESS)) {
            //秒杀成功
            resp.setSuccess(true);

            int times = 0;
            while (times < 5) {
                OrderDto orderDto = OrderLogic.GetOrder(activityId, unionId, ActivityType.SECKILL, createTime);
                if (orderDto != null) {
                    resp.setOrderUniqueCode(orderDto.getOrderUniqueCode());
                    resp.setCouponAmount(0);
                    resp.setAmount(orderDto.getPayAmount());
                    resp.setOriginalAmount(orderDto.getPayAmount());
                    resp.setOrderId(orderDto.getOrderId());
                    resp.setExpiredTime(orderDto.getExpiredTime());
                    break;
                }

                times++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return resp;

        } else {
            resp.setSuccess(false);
            resp.setMessage(secKillStatus.getMessage());
        }

        return resp;

    }
    //endregion

    //region (public) 订单订单价格 calculateOrder

    /**
     * 订单订单价格
     *
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "订单订单价格")
    @RequestMapping(value = "/calculateOrder", method = RequestMethod.POST)
    public Response<CreateOrderResp> calculateOrder(@ApiParam(value = "请求实体", required = true)
                                                    @RequestBody @Validated Request<CreateOrderRequ> requ) throws Exception {

        OrderBase orderBase = OrderFactory.GetInstance(requ.getRequestBody().getActivityType());
        CreateOrderResp resp = orderBase.CalculateOrder(requ.getRequestBody());

        return Response.ok(resp);

    }
    //endregion

}
