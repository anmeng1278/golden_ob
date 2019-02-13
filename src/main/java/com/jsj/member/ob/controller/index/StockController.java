package com.jsj.member.ob.controller.index;

import com.alibaba.fastjson.JSON;
import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.controller.BaseController;
import com.jsj.member.ob.dto.RestResponseBo;
import com.jsj.member.ob.dto.api.airport.JsAirportDto;
import com.jsj.member.ob.dto.api.delivery.CreateDeliveryRequ;
import com.jsj.member.ob.dto.api.delivery.CreateDeliveryResp;
import com.jsj.member.ob.dto.api.delivery.DeliveryDto;
import com.jsj.member.ob.dto.api.gift.CreateGiftRequ;
import com.jsj.member.ob.dto.api.gift.CreateGiftResp;
import com.jsj.member.ob.dto.api.gift.GiftProductDto;
import com.jsj.member.ob.dto.api.stock.StockDto;
import com.jsj.member.ob.dto.api.stock.UseProductDto;
import com.jsj.member.ob.dto.proto.CustomerInfoNewOuterClass;
import com.jsj.member.ob.dto.proto.CustomerInformationRequestOuterClass;
import com.jsj.member.ob.dto.proto.CustomerInformationResponseOuterClass;
import com.jsj.member.ob.entity.Banner;
import com.jsj.member.ob.entity.DeliveryStock;
import com.jsj.member.ob.enums.*;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.*;
import com.jsj.member.ob.logic.delivery.DeliveryBase;
import com.jsj.member.ob.logic.delivery.DeliveryFactory;
import com.jsj.member.ob.redis.RedisService;
import com.jsj.member.ob.redis.StockKey;
import com.jsj.member.ob.tuple.TwoTuple;
import com.jsj.member.ob.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@ApiIgnore
@Controller
@RequestMapping("${webconfig.virtualPath}/stock")
public class StockController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(StockController.class);

    @Autowired
    RedisService redisService;

    //region (public) 我的库存 index

    /**
     * 我的库存
     *
     * @param request
     * @return
     */
    @GetMapping(value = {""})
    public String index(HttpServletRequest request) {

        String openId = this.OpenId();
        String unionId = this.UnionId();

        //库存列表
        List<StockDto> stockDtos = StockLogic.GetStocks(openId, unionId);
        request.setAttribute("stockDtos", stockDtos);

        //分享失败的礼包
        int unShareCount = GiftLogic.GetGiveCount(unionId, GiftStatus.UNSHARE);
        request.setAttribute("unShareCount", unShareCount);

        //存在未使用的活动码
        DeliveryDto unDeliveryDto = DeliveryFactory.GetInstance(PropertyType.ACTIVITYCODE).GetUnDeliveryDto(unionId);
        List<DeliveryStock> unUsedActivityCodes = new ArrayList<>();

        if (unDeliveryDto != null) {
            unUsedActivityCodes = DeliveryLogic.GetDeliveryStocks(unDeliveryDto.getDeliveryId());
        }
        request.setAttribute("unUsedActivityCodes", unUsedActivityCodes);

        //库存轮播图
        List<Banner> banners = BannerLogic.GetBanner(BannerType.STOCK.getValue());
        request.setAttribute("banners", banners);

        return "index/stock";
    }
    //endregion

    //region (public) 微信送好友 createGift

    /**
     * 微信送好友
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/createGift", method = RequestMethod.POST)
    @ResponseBody
    public RestResponseBo createGift(HttpServletRequest request) {

        String[] nums = request.getParameterValues("num");

        if (nums == null || nums.length == 0) {
            return RestResponseBo.fail("没有选择要赠送的商品");
        }

        String[] productIds = request.getParameterValues("productId");
        String[] productSpecIds = request.getParameterValues("productSpecId");

        String openId = this.OpenId();
        String unionId = this.UnionId();

        CreateGiftRequ requ = new CreateGiftRequ();
        List<GiftProductDto> giftProductDtos = new ArrayList<>();


        for (int i = 0; i < nums.length; i++) {

            int num = Integer.parseInt(nums[i]);
            int productId = Integer.parseInt(productIds[i]);
            int productSpecId = Integer.parseInt(productSpecIds[i]);

            if (num == 0) {
                continue;
            }

            GiftProductDto dto = new GiftProductDto();
            dto.setProductId(productId);
            dto.setNumber(num);
            dto.setProductSpecId(productSpecId);

            giftProductDtos.add(dto);
        }

        if (giftProductDtos.size() == 0) {
            return RestResponseBo.fail("没有选择要赠送的商品");
        }

        requ.setGiftProductDtos(giftProductDtos);
        requ.setGiftShareType(GiftShareType.GROUP);
        requ.setBlessings("");
        requ.getBaseRequ().setOpenId(openId);
        requ.getBaseRequ().setUnionId(unionId);

        CreateGiftResp resp = GiftLogic.CreateGift(requ);
        String shareUrl = this.Url(String.format("/share/gift/%s/confirm", resp.getGiftUniqueCode()));

        return RestResponseBo.ok("操作成功，请选择分享方式。", shareUrl, resp);

    }
    //endregion

    //region (public) 使用前验证 validateUsed

    /**
     * 使用前验证
     *
     * @param request
     * @return
     */
    @PostMapping(value = {"/validateUsed"})
    @ResponseBody
    public RestResponseBo validateUsed(HttpServletRequest request) throws UnsupportedEncodingException {

        String p = request.getParameter("p");
        String propertyTypeId = request.getParameter("propertyTypeId");
        if (StringUtils.isEmpty(p) || StringUtils.isEmpty(propertyTypeId)) {
            return RestResponseBo.fail("参数错误", null, this.Url("/stock", false));
        }

        p = URLDecoder.decode(p, "UTF-8");

        PropertyType propertyType = PropertyType.valueOf(Integer.valueOf(propertyTypeId));
        String openId = this.OpenId();
        String unionId = this.UnionId();

        //根据商品属性创建实例
        DeliveryBase deliveryBase = DeliveryFactory.GetInstance(propertyType);

        //验证并获取下一步链接
        TwoTuple<String, String> usedNavigate = deliveryBase.GetUsedNavigate(unionId);

        if (!StringUtils.isEmpty(usedNavigate.first)) {
            return RestResponseBo.fail(usedNavigate.first, null, this.Url(usedNavigate.second));
        }

        List<UseProductDto> useProductDtos = JSON.parseArray(p, UseProductDto.class);
        List<StockDto> stockDtos = StockLogic.GetStocks(openId, unionId, useProductDtos, false);

        //验证使用库存
        deliveryBase.validateUsed(stockDtos);

        //添加缓存
        redisService.set(StockKey.token, openId, stockDtos);

        //url = String.format("%s?p=%s", url, p);
        return RestResponseBo.ok("验证成功", this.Url(usedNavigate.second), null);

    }
    //endregion


    //region (public) 实物使用 stockUse1

    /**
     * 实物使用
     *
     * @param request
     * @return
     */
    @GetMapping(value = {"/use1"})
    public String stockUse1(HttpServletRequest request) {

        String openId = this.OpenId();

        //获取缓存数据
        String p = redisService.get(StockKey.token, openId, String.class);
        if (StringUtils.isEmpty(p)) {
            this.Redirect("/stock", false);
        }

        try {

            List<StockDto> stockDtos = JSON.parseArray(p, StockDto.class);
            request.setAttribute("stockDtos", stockDtos);

            //不支持自提
            boolean unSupportPickup = stockDtos.stream().filter(s -> s.getProductDto().getIfpickup().equals(false)).findFirst().isPresent();
            request.setAttribute("unSupportPickup", unSupportPickup);

            //机场贵宾厅
            List<JsAirportDto> airport = AirportLogic.GetAirportDtos(AirportType.AIRPORT);
            List<JsAirportDto> train = AirportLogic.GetAirportDtos(AirportType.TRAIN);

            request.setAttribute("airports", airport);
            request.setAttribute("trains", train);
        } catch (TipException ex) {
            logger.error(JSON.toJSONString(ex));
            this.Redirect("/stock", false);
        }

        return "index/stockUse1";
    }
    //endregion

    //region (public) 实物使用 saveStockUse1

    /**
     * 实物使用
     *
     * @param request
     * @return
     */
    @PostMapping(value = {"/use1"})
    @ResponseBody
    @Transactional(Constant.DBTRANSACTIONAL)
    public RestResponseBo saveStockUse1(HttpServletRequest request) {

        String openId = this.OpenId();
        String unionId = this.UnionId();
        //获取缓存数据
        String p = redisService.get(StockKey.token, openId, String.class);
        if (StringUtils.isEmpty(p)) {
            return RestResponseBo.fail("参数错误");
        }

        List<StockDto> stockDtos = JSON.parseArray(p, StockDto.class);
        if (stockDtos.isEmpty()) {
            return RestResponseBo.fail("参数错误");
        }
        if (!stockDtos.get(0).getProductDto().getPropertyType().equals(PropertyType.ENTITY)) {
            return RestResponseBo.fail("参数错误");
        }

        String contactName = request.getParameter("contactName");
        String mobile = request.getParameter("mobile");

        int provinceId = 0, cityId = 0, districtId = 0;
        if (!StringUtils.isEmpty(request.getParameter("provinceId"))) {
            provinceId = Integer.parseInt(request.getParameter("provinceId"));
        }
        if (!StringUtils.isEmpty(request.getParameter("cityId"))) {
            cityId = Integer.parseInt(request.getParameter("cityId"));
        }
        if (!StringUtils.isEmpty(request.getParameter("districtId"))) {
            districtId = Integer.parseInt(request.getParameter("districtId"));
        }
        String address = request.getParameter("address");
        String effectiveDate = request.getParameter("effectiveDate");

        int effEctDateTimeStamp = 0;
        if (!StringUtils.isEmpty(effectiveDate)) {
            if (!com.jsj.member.ob.utils.StringUtils.isStrDate(effectiveDate)) {
                throw new TipException("生效时间格式错误");
            }
            effEctDateTimeStamp = DateUtils.getUnixTimeByDate(DateUtils.dateFormat(effectiveDate, "yyyy-MM-dd"));
        }

        String airportCode = request.getParameter("airportCode");
        String airportName = request.getParameter("airportName");

        DeliveryType deliveryType = DeliveryType.valueOf(Integer.parseInt(request.getParameter("deliveryType")));

        CreateDeliveryRequ requ = new CreateDeliveryRequ();

        requ.getBaseRequ().setOpenId(openId);
        requ.getBaseRequ().setUnionId(unionId);
        requ.getBaseRequ().setJsjId(this.User().getJsjId());
        requ.setAddress(address);
        requ.setCityId(cityId);
        requ.setContactName(contactName);
        requ.setDeliveryType(deliveryType);

        requ.setDistrictId(districtId);
        requ.setEffectiveDate(effEctDateTimeStamp);
        requ.setMobile(mobile);
        requ.setPropertyType(PropertyType.ENTITY);
        requ.setProvinceId(provinceId);

        requ.setStockDtos(stockDtos);
        requ.setAirportCode(airportCode);
        requ.setAirportName(airportName);


        CreateDeliveryResp resp = DeliveryLogic.CreateDelivery(requ);

        //清空缓存
        redisService.delete(StockKey.token, openId);

        return RestResponseBo.ok("操作成功", this.Url("/delivery"), resp);


    }
    //endregion

    //region (public) 次卡使用 stockUse2

    /**
     * 次卡使用
     *
     * @param request
     * @return
     */
    @GetMapping(value = {"/use2"})
    public String stockUse2(HttpServletRequest request) {

        String openId = this.OpenId();
        //获取缓存数据
        String p = redisService.get(StockKey.token, openId, String.class);
        if (StringUtils.isEmpty(p)) {
            this.Redirect("/stock", false);
        }

        try {

            List<StockDto> stockDtos = JSON.parseArray(p, StockDto.class);
            request.setAttribute("stockDtos", stockDtos);

            //机场贵宾厅
            List<JsAirportDto> airport = AirportLogic.GetAirportDtos(AirportType.AIRPORT);
            List<JsAirportDto> train = AirportLogic.GetAirportDtos(AirportType.TRAIN);

            request.setAttribute("airports", airport);
            request.setAttribute("trains", train);

        } catch (TipException ex) {
            logger.error(JSON.toJSONString(ex));
            this.Redirect("/stock", false);
        }

        return "index/stockUse2";
    }
    //endregion

    //region (public) 次卡使用 saveStockUse2

    /**
     * 次卡使用
     *
     * @param request
     * @return
     */
    @PostMapping(value = {"/use2"})
    @ResponseBody
    @Transactional(Constant.DBTRANSACTIONAL)
    public RestResponseBo saveStockUse2(HttpServletRequest request) {

        String openId = this.OpenId();
        String unionId = this.UnionId();
        //获取缓存数据
        String p = redisService.get(StockKey.token, openId, String.class);
        if (StringUtils.isEmpty(p)) {
            return RestResponseBo.fail("参数错误");
        }

        List<StockDto> stockDtos = JSON.parseArray(p, StockDto.class);
        if (stockDtos.isEmpty()) {
            return RestResponseBo.fail("参数错误");
        }
        if (!stockDtos.get(0).getProductDto().getPropertyType().equals(PropertyType.ACTIVITYCODE)) {
            return RestResponseBo.fail("参数错误");
        }

        String contactName = request.getParameter("contactName");
        String mobile = request.getParameter("mobile");
        String airportCode = request.getParameter("airportCode");
        String airportName = request.getParameter("airportName");
        String flightNumber = request.getParameter("flightNumber");


        CreateDeliveryRequ requ = new CreateDeliveryRequ();

        requ.getBaseRequ().setOpenId(openId);
        requ.getBaseRequ().setUnionId(unionId);
        requ.getBaseRequ().setJsjId(this.User().getJsjId());
        requ.setContactName(contactName);
        requ.setMobile(mobile);
        requ.setPropertyType(PropertyType.ACTIVITYCODE);

        requ.setStockDtos(stockDtos);
        requ.setAirportCode(airportCode);
        requ.setAirportName(airportName);
        requ.setFlightNumber(flightNumber);

        CreateDeliveryResp resp = DeliveryLogic.CreateDelivery(requ);

        //清空缓存
        redisService.delete(StockKey.token, openId);

        String url = this.Url(String.format("/stock/qrcode/%d/%d", resp.getDeliveryId(), resp.getStockId()));
        return RestResponseBo.ok("操作成功", url, resp);

    }
    //endregion

    //region (public) 会员卡使用 stockUse3

    /**
     * 会员卡使用
     *
     * @param request
     * @return
     */
    @GetMapping(value = {"/use3"})
    public String stockUse3(HttpServletRequest request) {

        String openId = this.OpenId();
        //获取缓存数据
        String p = redisService.get(StockKey.token, openId, String.class);
        if (StringUtils.isEmpty(p)) {
            this.Redirect("/stock", false);
        }
        try {
            List<StockDto> stockDtos = JSON.parseArray(p, StockDto.class);
            request.setAttribute("stockDtos", stockDtos);
        } catch (TipException ex) {
            logger.error(JSON.toJSONString(ex));
            this.Redirect("/stock", false);
        }

        return "index/stockUse3";
    }
    //endregion

    //region (public) 会员卡使用 saveStockUse3

    /**
     * 会员卡使用
     *
     * @param request
     * @return
     */
    @PostMapping(value = {"/use3"})
    @ResponseBody
    @Transactional(Constant.DBTRANSACTIONAL)
    public RestResponseBo saveStockUse3(HttpServletRequest request) {


        String openId = this.OpenId();
        String unionId = this.UnionId();
        //获取缓存数据
        String p = redisService.get(StockKey.token, openId, String.class);
        if (StringUtils.isEmpty(p)) {
            return RestResponseBo.fail("参数错误");
        }

        List<StockDto> stockDtos = JSON.parseArray(p, StockDto.class);
        if (stockDtos.isEmpty()) {
            return RestResponseBo.fail("参数错误");
        }
        if (!stockDtos.get(0).getProductDto().getPropertyType().isMmeberCard()) {
            return RestResponseBo.fail("参数错误");
        }

        String contactName = request.getParameter("contactName");
        String mobile = request.getParameter("mobile");
        String idNumber = request.getParameter("idNumber");
        String effectiveDate = request.getParameter("effectiveDate");

        if (!com.jsj.member.ob.utils.StringUtils.isStrDate(effectiveDate)) {
            throw new TipException("生效时间格式错误，示例：2019-01-01");
        }
        Date effectDate = DateUtils.dateFormat(effectiveDate, "yyyy-MM-dd");
        int effEctDateTimeStamp = DateUtils.getUnixTimeByDate(effectDate);

        CreateDeliveryRequ requ = new CreateDeliveryRequ();

        requ.getBaseRequ().setOpenId(openId);
        requ.getBaseRequ().setUnionId(unionId);
        requ.getBaseRequ().setJsjId(this.User().getJsjId());
        requ.setContactName(contactName);
        requ.setMobile(mobile);
        requ.setPropertyType(stockDtos.get(0).getProductDto().getPropertyType());

        requ.setIdNumber(idNumber);
        requ.setIdTypeId(1);
        requ.setEffectiveDate(effEctDateTimeStamp);
        requ.setStockDtos(stockDtos);

        //开卡方法
        CreateDeliveryResp resp = DeliveryLogic.CreateDelivery(requ);

        //清空缓存
        redisService.delete(StockKey.token, openId);

        return RestResponseBo.ok("开通成功", this.Url("/delivery"), resp);

    }
    //endregion


    //region (public) 开通plus权益 stockUse4

    /**
     * 开通plus权益
     *
     * @param request
     * @return
     */
    @GetMapping(value = {"/use4"})
    public String stockUse4(HttpServletRequest request) {

        //查询会员信息
        CustomerInformationRequestOuterClass.CustomerInformationRequest.Builder requ = CustomerInformationRequestOuterClass.CustomerInformationRequest.newBuilder();
        requ.setJSJID(this.User().getJsjId() + "");

        CustomerInformationResponseOuterClass.CustomerInformationResponse resp = MemberLogic.CustomerInformation(requ.build());
        if (!resp.getBaseResponse().getIsSuccess()) {
            //没有查询到会员信息
            this.Redirect("/stock", false);
        }

        if (resp.getListCount() == 0) {
            //没有查询到会员信息
            this.Redirect("/stock", false);
        }

        CustomerInfoNewOuterClass.CustomerInfoNew userInfo = resp.getList(0);

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

        String openId = this.OpenId();
        //获取缓存数据
        String p = redisService.get(StockKey.token, openId, String.class);
        if (StringUtils.isEmpty(p)) {
            this.Redirect("/stock", false);
        }
        try {
            List<StockDto> stockDtos = JSON.parseArray(p, StockDto.class);

            request.setAttribute("stockDtos", stockDtos);
            request.setAttribute("cardId", cardId);
            request.setAttribute("cardTypeIdName", cardTypeIdName);
            request.setAttribute("mobile", mobile);
            request.setAttribute("cardInvalidDate", cardInvalidDate);
            request.setAttribute("customerName", customerName);

        } catch (TipException ex) {
            logger.error(JSON.toJSONString(ex));
            this.Redirect("/stock", false);
        }

        return "index/stockUse4";
    }
    //endregion

    //region (public) 开通plus权益 saveStockUse4

    /**
     * 开通plus权益
     *
     * @param request
     * @return
     */
    @PostMapping(value = {"/use4"})
    @ResponseBody
    @Transactional(Constant.DBTRANSACTIONAL)
    public RestResponseBo saveStockUse4(HttpServletRequest request) {


        String openId = this.OpenId();
        String unionId = this.UnionId();
        //获取缓存数据
        String p = redisService.get(StockKey.token, openId, String.class);
        if (StringUtils.isEmpty(p)) {
            return RestResponseBo.fail("参数错误");
        }

        List<StockDto> stockDtos = JSON.parseArray(p, StockDto.class);
        if (stockDtos.isEmpty()) {
            return RestResponseBo.fail("参数错误");
        }
        if (!stockDtos.get(0).getProductDto().getPropertyType().equals(PropertyType.PLUS)) {
            return RestResponseBo.fail("参数错误");
        }

        CreateDeliveryRequ requ = new CreateDeliveryRequ();

        String contactName = request.getParameter("contactName");
        String mobile = request.getParameter("mobile");

        requ.getBaseRequ().setJsjId(this.User().getJsjId());
        requ.getBaseRequ().setOpenId(openId);
        requ.getBaseRequ().setUnionId(unionId);
        requ.setPropertyType(stockDtos.get(0).getProductDto().getPropertyType());
        requ.setContactName(contactName);
        requ.setMobile(mobile);

        requ.setStockDtos(stockDtos);

        //开通权益方法
        CreateDeliveryResp resp = DeliveryLogic.CreateDelivery(requ);

        //清空缓存
        redisService.delete(StockKey.token, openId);

        return RestResponseBo.ok("开通成功", this.Url("/delivery"), resp);

    }
    //endregion

    //region (public) 当前活动码 qrCode

    /**
     * 当前活动码
     *
     * @param request
     * @return
     */
    @GetMapping(value = {"/qrcode/{deliveryId}/{stockId}"})
    public String qrCode(
            @PathVariable(name = "deliveryId") int deliveryId,
            @PathVariable(name = "stockId") int stockId,
            HttpServletRequest request) {

        String openId = this.OpenId();
        DeliveryDto deliveryDto = DeliveryLogic.GetDelivery(deliveryId);

        //非本人的
        if (!deliveryDto.getOpenId().equals(openId)) {
            this.Redirect("/");
        }

        String imgUrl;
        try {

            if (!deliveryDto.getDeliveryStatus().equals(DeliveryStatus.DELIVERED)) {
                throw new TipException("当前配送状态不允许使用");
            }

            List<DeliveryStock> deliveryStocks = DeliveryLogic.GetDeliveryStocks(deliveryId);
            if (deliveryStocks.isEmpty()) {
                throw new TipException("没有找到库存信息");
            }

            Optional<DeliveryStock> deliveryStock = deliveryStocks.stream().filter(ds -> ds.getStockId().equals(stockId)).findFirst();
            if (!deliveryStock.isPresent()) {
                throw new TipException("没有找到库存信息");
            }

            if (StringUtils.isEmpty(deliveryStock.get().getActivityCode())) {
                throw new TipException("没有找到活动码信息");
            }

            imgUrl = String.format(super.webconfig.getQrcodeUrl(), deliveryStock.get().getActivityCode());

        } catch (Exception ex) {
            imgUrl = this.Img("/index/images/qrcodeUsed.png");
        }

        request.setAttribute("imgUrl", imgUrl);
        return "index/qrCode";

    }
    //endregion

}
