package com.jsj.member.ob.controller.api;

import com.jsj.member.ob.dto.api.Request;
import com.jsj.member.ob.dto.api.Response;
import com.jsj.member.ob.dto.api.activity.ActivityDto;
import com.jsj.member.ob.dto.api.activity.ActivityProductDto;
import com.jsj.member.ob.dto.api.coupon.WechatCouponDto;
import com.jsj.member.ob.dto.api.order.CreateOrderRequ;
import com.jsj.member.ob.dto.api.order.CreateOrderResp;
import com.jsj.member.ob.dto.api.product.ProductDto;
import com.jsj.member.ob.dto.api.product.ProductSpecDto;
import com.jsj.member.ob.dto.mini.ProductDetailRequ;
import com.jsj.member.ob.dto.mini.ProductDetailResp;
import com.jsj.member.ob.enums.ActivityType;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.ActivityLogic;
import com.jsj.member.ob.logic.CouponLogic;
import com.jsj.member.ob.logic.MemberLogic;
import com.jsj.member.ob.logic.ProductLogic;
import com.jsj.member.ob.logic.order.OrderBase;
import com.jsj.member.ob.logic.order.OrderFactory;
import com.jsj.member.ob.utils.DateUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

        ActivityType activityType = ActivityType.valueOf(requ.getRequestBody().getActivityType());

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

        String unionId = requ.getUnionId();

        //商品可用代金券
        List<WechatCouponDto> coupons = CouponLogic.GetWechatCoupons(productId, unionId);

        if (info.getProductImgDtos() != null && info.getProductImgDtos().size() > 0) {
            String imgUrl = info.getProductImgDtos().get(0).getImgPath();
            resp.setImgUrl(imgUrl);
        }
        resp.setStockCount(stockCount);
        resp.setProductDto(info);
        resp.setWechatCouponDtos(coupons);
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
    public Response<CreateOrderResp> createOrder(@ApiParam(value = "请求实体", required = true)
                                                 @RequestBody @Validated Request<CreateOrderRequ> requ) throws Exception {

        OrderBase orderBase = OrderFactory.GetInstance(requ.getRequestBody().getActivityType());
        CreateOrderResp resp = orderBase.CreateOrder(requ.getRequestBody());

        return Response.ok(resp);

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
