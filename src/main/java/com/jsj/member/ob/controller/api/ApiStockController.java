package com.jsj.member.ob.controller.api;

import com.alibaba.fastjson.JSON;
import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.controller.BaseController;
import com.jsj.member.ob.dto.api.Request;
import com.jsj.member.ob.dto.api.Response;
import com.jsj.member.ob.dto.api.airport.JsAirportDto;
import com.jsj.member.ob.dto.api.delivery.CreateDeliveryRequ;
import com.jsj.member.ob.dto.api.delivery.CreateDeliveryResp;
import com.jsj.member.ob.dto.api.delivery.DeliveryDto;
import com.jsj.member.ob.dto.api.stock.StockDto;
import com.jsj.member.ob.dto.api.stock.UseProductDto;
import com.jsj.member.ob.dto.mini.*;
import com.jsj.member.ob.dto.proto.CustomerInfoNewOuterClass;
import com.jsj.member.ob.dto.proto.CustomerInformationRequestOuterClass;
import com.jsj.member.ob.dto.proto.CustomerInformationResponseOuterClass;
import com.jsj.member.ob.entity.Banner;
import com.jsj.member.ob.entity.DeliveryStock;
import com.jsj.member.ob.enums.AirportType;
import com.jsj.member.ob.enums.BannerType;
import com.jsj.member.ob.enums.GiftStatus;
import com.jsj.member.ob.enums.PropertyType;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.*;
import com.jsj.member.ob.logic.delivery.DeliveryBase;
import com.jsj.member.ob.logic.delivery.DeliveryFactory;
import com.jsj.member.ob.redis.RedisService;
import com.jsj.member.ob.redis.StockKey;
import com.jsj.member.ob.tuple.TwoTuple;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ApiStockController extends BaseController {


    @Autowired
    RedisService redisService;

    //region (public) 我的库存 stocks

    /**
     * 我的库存
     *
     * @param requ
     * @return
     */
    @ApiOperation(value = "我的库存")
    @RequestMapping(value = "/stocks", method = RequestMethod.POST)
    public Response<StocksResp> stocks(@ApiParam(value = "请求实体", required = true)
                                       @RequestBody
                                       @Validated Request<StocksRequ> requ) {

        StocksResp resp = new StocksResp();

        String openId = requ.getRequestBody().getOpenId();
        String unionId = requ.getRequestBody().getUnionId();

        //库存列表
        List<StockDto> stockDtos = StockLogic.GetStocks(openId, unionId);

        //分享失败的礼包
        int unShareCount = GiftLogic.GetGiveCount(unionId, GiftStatus.UNSHARE);

        //存在未使用的活动码
        DeliveryDto unDeliveryDto = DeliveryFactory.GetInstance(PropertyType.ACTIVITYCODE).GetUnDeliveryDto(unionId);
        List<DeliveryStock> unUsedActivityCodes = new ArrayList<>();

        if (unDeliveryDto != null) {
            unUsedActivityCodes = DeliveryLogic.GetDeliveryStocks(unDeliveryDto.getDeliveryId());
        }

        //库存轮播图
        List<Banner> banners = BannerLogic.GetBanner(BannerType.STOCK.getValue());

        resp.setBanners(banners);
        resp.setUnUsedActivityCodes(unUsedActivityCodes);
        resp.setUnShareCount(unShareCount);
        resp.setStockDtos(stockDtos);

        return Response.ok(resp);

    }
    //endregion

    //region (public) 使用前验证 validateUsed

    /**
     * 使用前验证
     *
     * @param requ
     * @return
     */
    @ApiOperation(value = "使用前验证")
    @RequestMapping(value = "/validateUsed", method = RequestMethod.POST)
    public Response<ValidateUsedResp> validateUsed(@ApiParam(value = "请求实体", required = true)
                                                   @RequestBody
                                                   @Validated Request<ValidateUsedRequ> requ) {

        ValidateUsedResp resp = new ValidateUsedResp();

        String openId = requ.getRequestBody().getOpenId();
        String unionId = requ.getRequestBody().getUnionId();

        //根据商品属性创建实例
        DeliveryBase deliveryBase = DeliveryFactory.GetInstance(requ.getRequestBody().getPropertyType());

        //验证并获取下一步链接
        TwoTuple<String, String> usedNavigate = deliveryBase.GetUsedNavigate(unionId);

        if (!StringUtils.isEmpty(usedNavigate.first)) {
            throw new TipException(usedNavigate.first);
        }

        List<UseProductDto> useProductDtos = requ.getRequestBody().getUseProductDtos();
        List<StockDto> stockDtos = StockLogic.GetStocks(openId, unionId, useProductDtos, false);

        //验证使用库存
        deliveryBase.validateUsed(stockDtos);

        //添加缓存
        redisService.set(StockKey.token, openId, stockDtos);

        return Response.ok(resp);

    }
    //endregion

    //region (public) 获取使用前信息 readyDelivery

    /**
     * 获取使用前信息
     *
     * @param requ
     * @return
     */
    @ApiOperation(value = "获取使用前信息")
    @RequestMapping(value = "/readyDelivery", method = RequestMethod.POST)
    public Response<ReadyDeliveryResp> readyDelivery(@ApiParam(value = "请求实体", required = true)
                                                     @RequestBody
                                                     @Validated Request<ReadyDeliveryRequ> requ) {

        ReadyDeliveryResp resp = new ReadyDeliveryResp();
        String openId = requ.getRequestBody().getOpenId();

        //获取缓存数据
        String p = redisService.get(StockKey.token, openId, String.class);
        if (StringUtils.isEmpty(p)) {
            throw new TipException("缓存信息丢失，请重新使用。");
        }

        List<StockDto> stockDtos = JSON.parseArray(p, StockDto.class);

        //不支持自提
        boolean unSupportPickup = stockDtos.stream().filter(s -> s.getProductDto().getIfpickup().equals(false)).findFirst().isPresent();

        //机场贵宾厅
        List<JsAirportDto> airports = AirportLogic.GetAirportDtos(AirportType.AIRPORT);
        List<JsAirportDto> trains = AirportLogic.GetAirportDtos(AirportType.TRAIN);

        resp.setUnSupportPickup(unSupportPickup);
        resp.setAirports(airports);
        resp.setTrains(trains);
        resp.setStockDtos(stockDtos);

        int jsjId = requ.getRequestBody().getJsjId();

        if (jsjId > 0) {

            //查询会员信息
            CustomerInformationRequestOuterClass.CustomerInformationRequest.Builder builder = CustomerInformationRequestOuterClass.CustomerInformationRequest.newBuilder();
            builder.setJSJID(jsjId + "");

            CustomerInformationResponseOuterClass.CustomerInformationResponse customerInformation = MemberLogic.CustomerInformation(builder.build());

            if (customerInformation.getBaseResponse().getIsSuccess() && customerInformation.getListCount() > 0) {

                CustomerInfoNewOuterClass.CustomerInfoNew userInfo = customerInformation.getList(0);

                String cardId = userInfo.getCardID();
                String cardTypeIdName = userInfo.getCardTypeName();
                String mobile = userInfo.getContactmeans();
                String cardInvalidDate = userInfo.getCardInvalidDate();
                String customerName = userInfo.getCustomerName();

                if (StringUtils.isEmpty(mobile)) {
                    mobile = "15210000000";
                }
                if (StringUtils.isEmpty(customerName)) {
                    customerName = "未填写";
                }

                resp.setCardId(cardId);
                resp.setCardTypeIdName(cardTypeIdName);
                resp.setMobile(mobile);
                resp.setCardInvalidDate(cardInvalidDate);
                resp.setCustomerName(customerName);

            }

        }

        return Response.ok(resp);

    }
    //endregion

    //region (public) 创建配送 createDelivery

    /**
     * 创建配送
     *
     * @param requ
     * @return
     */
    @Transactional(Constant.DBTRANSACTIONAL)
    @ApiOperation(value = "创建配送")
    @RequestMapping(value = "/createDelivery", method = RequestMethod.POST)
    public Response<CreateDeliveryResp> createDelivery(@ApiParam(value = "请求实体", required = true)
                                                       @RequestBody
                                                       @Validated Request<CreateDeliveryRequ> requ) {

        String openId = requ.getRequestBody().getBaseRequ().getOpenId();

        //获取缓存数据
        String p = redisService.get(StockKey.token, openId, String.class);
        if (StringUtils.isEmpty(p)) {
            throw new TipException("参数错误");
        }

        List<StockDto> stockDtos = JSON.parseArray(p, StockDto.class);
        if (stockDtos.isEmpty()) {
            throw new TipException("参数错误");
        }

        requ.getRequestBody().setStockDtos(stockDtos);
        CreateDeliveryResp resp = DeliveryLogic.CreateDelivery(requ.getRequestBody());

        //清空缓存
        redisService.delete(StockKey.token, openId);

        return Response.ok(resp);

    }
    //endregion

}
