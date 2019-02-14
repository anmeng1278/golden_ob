package com.jsj.member.ob.controller.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jsj.member.ob.dto.api.Request;
import com.jsj.member.ob.dto.api.Response;
import com.jsj.member.ob.dto.api.activity.ActivityDto;
import com.jsj.member.ob.dto.api.activity.ActivityProductDto;
import com.jsj.member.ob.dto.api.product.ProductDto;
import com.jsj.member.ob.dto.mini.*;
import com.jsj.member.ob.entity.Banner;
import com.jsj.member.ob.enums.ActivityType;
import com.jsj.member.ob.enums.BannerType;
import com.jsj.member.ob.enums.OrderFlag;
import com.jsj.member.ob.enums.ProductType;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${webconfig.virtualPath}/mini")
public class ApiIndexController {

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

    //region (public) 兑换专区 exchange

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

        resp.setCartCount(cartCount);
        resp.setUnPayCount(unPayCount);

        return Response.ok(resp);

    }

    //endregion

    //region (public) 会员资产 asset

    /**
     * 会员资产
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "会员资产")
    @PostMapping(value = {"/asset"})
    public Response<AssetResp> asset(@ApiParam(value = "会员资产", required = true)
                                     @RequestBody
                                     @Validated Request<AssetRequ> request) {

        AssetResp resp = new AssetResp();

        if (request.getRequestBody().getJsjId() <= 0) {
            throw new TipException("会员编号不能为空");
        }

        //账户余额
        double balance = MemberLogic.StrictChoiceSearch(request.getRequestBody().getJsjId());
        resp.setGiftBalance(balance);

        return Response.ok(resp);

    }
    //endregion


    //region (public) 礼品券使用明细 strictChoiceDetail

    /**
     * 礼品券使用明细
     *
     * @param requ
     * @return
     */
    @ApiOperation(value = "礼品券使用明细")
    @PostMapping(value = {"/strictChoiceDetail"})
    public Response<JSONObject> strictChoiceDetail(@ApiParam(value = "礼品券使用明细", required = true)
                                                   @RequestBody
                                                   @Validated Request<Map<String, Object>> requ) throws Exception {

        JSONArray objects = MemberLogic.StrictChoiceDetail(requ.getRequestBody());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("strictChoices", objects);

        return Response.ok(jsonObject);

    }
    //endregion


    //region (public) 会员资产(全部) getCustAsset

    /**
     * 会员资产(全部)
     *
     * @param requ
     * @return
     */
    @ApiOperation(value = "会员资产(全部)")
    @PostMapping(value = {"/assets"})
    public Response<JSONObject> getCustAsset(@ApiParam(value = "会员资产(全部)", required = true)
                                             @RequestBody
                                             @Validated Request<Map<String, Object>> requ) throws Exception {

        JSONObject jsonObject = MemberLogic.GetCustAsset(requ.getRequestBody());
        return Response.ok(jsonObject);

    }
    //endregion


    //region (public) plus券使用明细 plusDetail

    /**
     * plus券使用明细
     *
     * @param requ
     * @return
     */
    @ApiOperation(value = "plus券使用明细")
    @PostMapping(value = {"/plusDetail"})
    public Response<JSONObject> plusDetail(@ApiParam(value = "plus券使用明细", required = true)
                                           @RequestBody
                                           @Validated Request<Map<String, Object>> requ) throws Exception {

        JSONObject jsonObject = MemberLogic.GetPlusDetail(requ.getRequestBody());
        return Response.ok(jsonObject);

    }
    //endregion
}
