package com.jsj.member.ob.controller.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jsj.member.ob.controller.BaseController;
import com.jsj.member.ob.dto.api.Request;
import com.jsj.member.ob.dto.api.Response;
import com.jsj.member.ob.dto.api.activity.ActivityDto;
import com.jsj.member.ob.dto.api.activity.ActivityProductDto;
import com.jsj.member.ob.dto.api.airport.JsAirportDto;
import com.jsj.member.ob.dto.api.product.ProductDto;
import com.jsj.member.ob.dto.mini.*;
import com.jsj.member.ob.entity.Banner;
import com.jsj.member.ob.entity.Dict;
import com.jsj.member.ob.enums.*;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.*;
import com.jsj.member.ob.redis.AccessKey;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${webconfig.virtualPath}/mini")
public class ApiIndexController extends BaseController {

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

        IndexResp indexResp = this.GetAccessCache(AccessKey.pageIndex2, IndexResp.class);
        if (indexResp != null) {
            return Response.ok(indexResp);
        }

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

        this.SetAccessCache(AccessKey.pageIndex2, resp);

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

        ExchangeResp exchangeResp = this.GetAccessCache(AccessKey.pageExchange2, ExchangeResp.class);
        if (exchangeResp != null) {
            return Response.ok(this.filterExchangeResp(exchangeResp, request.getRequestBody().getJsjId()));
        }

        ExchangeResp resp = new ExchangeResp();

        ActivityDto exchange = ActivityLogic.GetActivity(ActivityType.EXCHANGE);
        List<ActivityProductDto> exchangeProducts = new ArrayList<>();

        if (exchange != null) {
            exchangeProducts = ActivityLogic.GetActivityProductDtos(exchange.getActivityId());
        }

        resp.setExchange(exchange);
        resp.setExchangeProducts(exchangeProducts);


        this.SetAccessCache(AccessKey.pageExchange2, resp);

        return Response.ok(this.filterExchangeResp(exchangeResp, request.getRequestBody().getJsjId()));

    }

    //endregion

    //region (private) 筛选plus权益 filterExchangeResp

    /**
     * 筛选plus权益
     *
     * @param resp
     * @param jsjId
     * @return
     */
    private ExchangeResp filterExchangeResp(ExchangeResp resp, int jsjId) {

        if (jsjId <= 0) {
            return resp;
        }
        if (resp == null || resp.getExchangeProducts() == null || resp.getExchangeProducts().isEmpty()) {
            return null;
        }
        Optional<ActivityProductDto> first = resp.getExchangeProducts().stream().filter(x -> x.getProductDto().getPropertyType().equals(PropertyType.PLUS)).findFirst();
        if (!first.isPresent()) {
            return resp;
        }

        JSONObject js = new JSONObject();
        js.put("JSJID", jsjId);

        JSONObject jsonObject = null;
        boolean isAllowBuy = true;
        try {
            jsonObject = MemberLogic.GetCustAsset(js);
            isAllowBuy = jsonObject.getBoolean("IsBuyPlus");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!isAllowBuy) {
            List<ActivityProductDto> collect = resp.getExchangeProducts().stream()
                    .filter(x -> !x.getProductDto().getPropertyType().equals(PropertyType.PLUS)).collect(Collectors.toList());
            resp.setExchangeProducts(collect);
        }
        return resp;

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

    //region (public) 机场高铁列表 plusDetail

    /**
     * 机场高铁列表
     *
     * @param requ
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "机场高铁列表")
    @PostMapping(value = {"/airports"})
    public Response<AirportResp> airports(@ApiParam(value = "机场高铁列表", required = true)
                                          @RequestBody
                                          @Validated Request<AirportRequ> requ) throws Exception {

        AirportResp resp = new AirportResp();

        //机场贵宾厅
        List<JsAirportDto> airports = AirportLogic.GetAirportDtos(AirportType.AIRPORT);
        List<JsAirportDto> trains = AirportLogic.GetAirportDtos(AirportType.TRAIN);

        resp.setAirports(airports);
        resp.setTrains(trains);

        return Response.ok(resp);

    }
    //endregion

    //region (public) 获取字典 dicts

    /**
     * 获取字典
     *
     * @param requ
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取字典")
    @PostMapping(value = {"/dicts"})
    public Response<DictsResp> dicts(@ApiParam(value = "获取字典", required = true)
                                     @RequestBody
                                     @Validated Request<DictsRequ> requ) throws Exception {


        DictType dictType = requ.getRequestBody().getDictType();
        int count = requ.getRequestBody().getCount();

        if (count <= 0) {
            count = 3;
        }

        //文案类型
        List<Dict> dicts = DictLogic.GetDicts(dictType, count);

        DictsResp dictsResp = new DictsResp();
        dictsResp.setDicts(dicts);

        return Response.ok(dictsResp);

    }
    //endregion

}
