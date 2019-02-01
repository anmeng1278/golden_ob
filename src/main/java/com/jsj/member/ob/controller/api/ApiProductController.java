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
import com.jsj.member.ob.logic.ProductLogic;
import com.jsj.member.ob.logic.order.OrderBase;
import com.jsj.member.ob.logic.order.OrderFactory;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${webconfig.virtualPath}/mini")
public class ApiProductController {


    @ApiOperation(value = "获得商品详情")
    @PostMapping(value = {"/getProductDetail"})
    public Response<ProductDetailResp> productDetail(@ApiParam(value = "请求实体", required = true)
                                                     @RequestBody @Validated Request<ProductDetailRequ> requ) throws Exception {

        ProductDetailResp resp = new ProductDetailResp();

        ActivityType activityType = ActivityType.valueOf(requ.getRequestBody().getActivityType());

        int stockCount = 0;
        switch (activityType) {
            case NORMAL:
                ProductDto product = ProductLogic.GetProduct(requ.getRequestBody().getProductId());
                stockCount = product.getProductSpecDtos().stream().mapToInt(ProductSpecDto::getStockCount).sum();
                //商品可用代金券
                List<WechatCouponDto> coupons = CouponLogic.GetWechatCoupons(requ.getRequestBody().getProductId(), requ.getRequestBody().getOpenId());
                resp.setProduct(product);
                resp.setCoupons(coupons);
                resp.setStockCount(stockCount);
                break;
            case SECKILL:
            case EXCHANGE:
                ActivityDto activity = ActivityLogic.GetActivity(requ.getRequestBody().getActivityId());
                List<ActivityProductDto> productDtos = ActivityLogic.GetActivityProductDtos(requ.getRequestBody().getActivityId(), requ.getRequestBody().getProductSpecId());
                ProductDto productDto = productDtos.get(0).getProductDto();
                ProductSpecDto productSpecDto = ProductLogic.GetProductSpec(requ.getRequestBody().getProductSpecId());
                //库存
                stockCount = Math.min(productDtos.get(0).getStockCount(), productSpecDto.getStockCount());
                resp.setProduct(productDto);
                resp.setActivity(activity);
                resp.setStockCount(stockCount);
                break;
            case COMBINATION:
                ActivityDto comActivity = ActivityLogic.GetActivity(requ.getRequestBody().getActivityId());
                List<ActivityProductDto> comActivityProducts = ActivityLogic.GetActivityProductDtos(requ.getRequestBody().getActivityId());
                if (!comActivityProducts.isEmpty()) {
                    for (ActivityProductDto apd : comActivityProducts) {
                        stockCount = Math.min(stockCount, apd.getStockCount());
                        ProductSpecDto specDto = ProductLogic.GetProductSpec(apd.getProductSpecId());
                        stockCount = Math.min(stockCount, specDto.getStockCount());
                    }
                }
                resp.setActivityProducts(comActivityProducts);
                resp.setActivity(comActivity);
                break;
        }
        return Response.ok(resp);
    }


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
        CreateOrderResp resp = orderBase.CalculateOrder(requ.getRequestBody());

        return Response.ok(resp);

    }
}
