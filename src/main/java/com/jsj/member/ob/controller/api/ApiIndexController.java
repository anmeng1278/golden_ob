package com.jsj.member.ob.controller.api;

import com.jsj.member.ob.dto.api.Request;
import com.jsj.member.ob.dto.api.Response;
import com.jsj.member.ob.dto.api.activity.ActivityDto;
import com.jsj.member.ob.dto.api.activity.ActivityProductDto;
import com.jsj.member.ob.dto.api.product.ProductDto;
import com.jsj.member.ob.dto.mini.ExchangeRequ;
import com.jsj.member.ob.dto.mini.ExchangeResp;
import com.jsj.member.ob.dto.mini.IndexRequ;
import com.jsj.member.ob.dto.mini.IndexResp;
import com.jsj.member.ob.entity.Banner;
import com.jsj.member.ob.enums.ActivityType;
import com.jsj.member.ob.enums.BannerType;
import com.jsj.member.ob.enums.OrderFlag;
import com.jsj.member.ob.enums.ProductType;
import com.jsj.member.ob.logic.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("${webconfig.virtualPath}/mini")
public class ApiIndexController {

    private final Logger logger = LoggerFactory.getLogger(ApiIndexController.class);


    //region (public) 首页 index

    /**
     * 首页
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "首页")
    @PostMapping(value = {"/index"})
    public Response<IndexResp> index(@ApiParam(value = "请求实体", required = true)
                                     @RequestBody
                                     @Validated Request<IndexRequ> request) {

        IndexResp resp = new IndexResp();

        //首页轮播图
        List<Banner> banners = BannerLogic.GetBanner(BannerType.COVER.getValue());
        resp.setBanners(banners);

        //品质出行
        List<ProductDto> qualityTravels = ProductLogic.GetProductDtos(ProductType.QUALITYTRAVELS.getValue(), 4);
        resp.setQualityTravels(qualityTravels);

        //爆款单品
        List<ProductDto> hotProducts = ProductLogic.GetProductDtos(ProductType.HOTPRODUCT.getValue(), 12);
        resp.setHotProducts(hotProducts);

        //限时秒杀
        ActivityDto secKills = ActivityLogic.GetActivity(ActivityType.SECKILL);
        List<ActivityProductDto> secKillProducts = new ArrayList<>();

        if (secKills != null) {
            secKillProducts = ActivityLogic.GetActivityProductDtos(secKills.getActivityId());
        }

        resp.setSecKillProducts(secKillProducts);
        resp.setSecKills(secKills);


        //组合优惠
        List<ActivityDto> setSales = ActivityLogic.GetActivityByType(ActivityType.COMBINATION);
        resp.setSetSales(setSales);

        //兑换专区商品
        ActivityDto exchange = ActivityLogic.GetActivity(ActivityType.EXCHANGE);
        List<ActivityProductDto> exchangeProducts = new ArrayList<>();
        if (exchange != null) {
            exchangeProducts = ActivityLogic.GetActivityProductDtos(exchange.getActivityId());
        }
        resp.setExchangeProducts(exchangeProducts);


        return Response.ok(resp);

    }
    //endregion

    //region (public) 兑换专区

    /**
     * 兑换专区
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "兑换专区")
    @PostMapping(value = {"/exchange"})
    public Response<ExchangeResp> exchange(@ApiParam(value = "请求实体", required = true)
                                           @RequestBody
                                           @Validated Request<ExchangeRequ> request) {

        ExchangeResp resp = new ExchangeResp();

        ActivityDto exchange = ActivityLogic.GetActivity(ActivityType.EXCHANGE);
        List<ActivityProductDto> exchangeProducts = new ArrayList<>();

        if (exchange != null) {
            exchangeProducts = ActivityLogic.GetActivityProductDtos(exchange.getActivityId());
        }

        resp.setExchange(exchange);
        resp.setExchangeProducts(exchangeProducts);

        String openId = request.getRequestBody().getOpenId();
        String unionId = request.getRequestBody().getUnionId();

        //购物车商品数
        int cartCount = CartLogic.GetCartProductCount(openId, unionId);
        //用户未支付订单数
        int unPayCount = OrderLogic.GetOrders(unionId, OrderFlag.UNPAIDORDERS).size();
        //账户余额
        double balance = MemberLogic.StrictChoiceSearch(request.getRequestBody().getJsjId());

        resp.setCartCount(cartCount);
        resp.setUnPayCount(unPayCount);
        resp.setBalance(balance);

        return Response.ok(resp);

    }

    //endregion


}
