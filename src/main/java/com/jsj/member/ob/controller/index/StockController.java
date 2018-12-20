package com.jsj.member.ob.controller.index;

import com.alibaba.fastjson.JSON;
import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.controller.BaseController;
import com.jsj.member.ob.dto.RestResponseBo;
import com.jsj.member.ob.dto.api.airport.JsAirportDto;
import com.jsj.member.ob.dto.api.delivery.CreateDeliveryRequ;
import com.jsj.member.ob.dto.api.delivery.CreateDeliveryResp;
import com.jsj.member.ob.dto.api.delivery.DeliveryDto;
import com.jsj.member.ob.dto.api.delivery.DeliveryStockDto;
import com.jsj.member.ob.dto.api.gift.CreateGiftRequ;
import com.jsj.member.ob.dto.api.gift.CreateGiftResp;
import com.jsj.member.ob.dto.api.gift.GiftDto;
import com.jsj.member.ob.dto.api.gift.GiftProductDto;
import com.jsj.member.ob.dto.api.stock.StockDto;
import com.jsj.member.ob.dto.api.stock.UseProductDto;
import com.jsj.member.ob.entity.DeliveryStock;
import com.jsj.member.ob.enums.*;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.AirportLogic;
import com.jsj.member.ob.logic.DeliveryLogic;
import com.jsj.member.ob.logic.GiftLogic;
import com.jsj.member.ob.logic.StockLogic;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApiIgnore
@Controller
@RequestMapping("${webconfig.virtualPath}/stock")
public class StockController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(StockController.class);

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

        List<StockDto> stockDtos = StockLogic.GetStocks(openId);
        request.setAttribute("stockDtos", stockDtos);

        List<GiftDto> giftDtos = GiftLogic.GetGives(openId, GiftStatus.UNSHARE);
        request.setAttribute("giftDtos", giftDtos);

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
            return RestResponseBo.fail("参数错误", null, this.Url("/stock"));
        }

        PropertyType propertyType = PropertyType.valueOf(Integer.valueOf(propertyTypeId));
        String url = "";
        String openId = this.OpenId();

        switch (propertyType) {

            case ENTITY:
                url = this.Url("/stock/use1", false);
                break;
            case ACTIVITYCODE:

                //判断是否存在未使用的活动码
                List<DeliveryStockDto> unUsedActivityCodes = DeliveryLogic.getUnUsedActivityCodes(openId);
                if (!unUsedActivityCodes.isEmpty()) {
                    url = this.Url(String.format("/stock/qrcode/%d/%d", unUsedActivityCodes.get(0).getDeliveryId(), unUsedActivityCodes.get(0).getStockId()));
                    return RestResponseBo.fail("您还有未使用的次卡", null, url);
                }

                url = this.Url("/stock/use2", false);
                break;
            case GOLDENCARD:

                DeliveryDto unCreateGoldenCard = DeliveryLogic.getUnCreateGoldenCard(openId);
                if (unCreateGoldenCard != null) {
                    return RestResponseBo.fail("您的开卡正在确认中，不能重复开卡……");
                }

                url = this.Url("/stock/use3", false);
                break;
        }

        url = String.format("%s?p=%s", url, p);
        return RestResponseBo.ok("验证成功", url, null);

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

        String p = request.getParameter("p");
        if (StringUtils.isEmpty(p)) {
            return this.Redirect("/stock");
        }

        String openId = this.OpenId();
        List<UseProductDto> useProductDtos = JSON.parseArray(p, UseProductDto.class);

        List<StockDto> stockDtos = StockLogic.GetStocks(openId, useProductDtos, true);
        request.setAttribute("stockDtos", stockDtos);

        //不支持自提
        boolean unSupportPickup = stockDtos.stream().filter(s -> s.getProductDto().getIfpickup().equals(false)).findFirst().isPresent();
        request.setAttribute("unSupportPickup", unSupportPickup);

        //机场贵宾厅
        List<JsAirportDto> airport = AirportLogic.GetAirportDtos(AirportType.AIRPORT);
        List<JsAirportDto> train = AirportLogic.GetAirportDtos(AirportType.TRAIN);

        request.setAttribute("airports", airport);
        request.setAttribute("trains", train);

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

        String p = request.getParameter("p");
        if (StringUtils.isEmpty(p)) {
            return RestResponseBo.fail("参数错误");
        }

        String openId = this.OpenId();
        List<UseProductDto> useProductDtos = JSON.parseArray(p, UseProductDto.class);

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

        String airportCode = request.getParameter("airportCode");
        String airportName = request.getParameter("airportName");

        DeliveryType deliveryType = DeliveryType.valueOf(Integer.parseInt(request.getParameter("deliveryType")));

        CreateDeliveryRequ requ = new CreateDeliveryRequ();

        requ.getBaseRequ().setOpenId(openId);
        requ.setAddress(address);
        requ.setCityId(cityId);
        requ.setContactName(contactName);
        requ.setDeliveryType(deliveryType);

        requ.setDistrictId(districtId);
        requ.setEffectiveDate(effectiveDate);
        requ.setMobile(mobile);
        requ.setPropertyType(PropertyType.ENTITY);
        requ.setProvinceId(provinceId);

        requ.setUseProductDtos(useProductDtos);
        requ.setAirportCode(airportCode);
        requ.setAirportName(airportName);

        CreateDeliveryResp resp = DeliveryLogic.CreateDelivery(requ);
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

        String p = request.getParameter("p");
        if (StringUtils.isEmpty(p)) {
            return this.Redirect("/stock");
        }

        String openId = this.OpenId();
        List<UseProductDto> useProductDtos = JSON.parseArray(p, UseProductDto.class);

        List<StockDto> stockDtos = StockLogic.GetStocks(openId, useProductDtos, true);
        request.setAttribute("stockDtos", stockDtos);

        //机场贵宾厅
        List<JsAirportDto> airport = AirportLogic.GetAirportDtos(AirportType.AIRPORT);
        List<JsAirportDto> train = AirportLogic.GetAirportDtos(AirportType.TRAIN);

        request.setAttribute("airports", airport);
        request.setAttribute("trains", train);

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

        String p = request.getParameter("p");
        if (StringUtils.isEmpty(p)) {
            return RestResponseBo.fail("参数错误");
        }

        String openId = this.OpenId();
        List<UseProductDto> useProductDtos = JSON.parseArray(p, UseProductDto.class);

        String contactName = request.getParameter("contactName");
        String mobile = request.getParameter("mobile");
        String airportCode = request.getParameter("airportCode");
        String airportName = request.getParameter("airportName");
        String flightNumber = request.getParameter("flightNumber");


        CreateDeliveryRequ requ = new CreateDeliveryRequ();

        requ.getBaseRequ().setOpenId(openId);
        requ.setContactName(contactName);
        requ.setMobile(mobile);
        requ.setPropertyType(PropertyType.ACTIVITYCODE);

        requ.setUseProductDtos(useProductDtos);
        requ.setAirportCode(airportCode);
        requ.setAirportName(airportName);
        requ.setFlightNumber(flightNumber);

        CreateDeliveryResp resp = DeliveryLogic.CreateDelivery(requ);
        return RestResponseBo.ok("操作成功", this.Url("/delivery"), resp);

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

        String p = request.getParameter("p");
        if (StringUtils.isEmpty(p)) {
            return this.Redirect("/stock");
        }

        String openId = this.OpenId();
        List<UseProductDto> useProductDtos = JSON.parseArray(p, UseProductDto.class);

        List<StockDto> stockDtos = StockLogic.GetStocks(openId, useProductDtos, true);
        request.setAttribute("stockDtos", stockDtos);

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

        String p = request.getParameter("p");
        if (StringUtils.isEmpty(p)) {
            return RestResponseBo.fail("参数错误");
        }

        String openId = this.OpenId();
        List<UseProductDto> useProductDtos = JSON.parseArray(p, UseProductDto.class);

        String contactName = request.getParameter("contactName");
        String mobile = request.getParameter("mobile");
        String idNumber = request.getParameter("idNumber");
        String effectiveDate = request.getParameter("effectiveDate");


        CreateDeliveryRequ requ = new CreateDeliveryRequ();

        requ.getBaseRequ().setOpenId(openId);
        requ.setContactName(contactName);
        requ.setMobile(mobile);
        requ.setPropertyType(PropertyType.GOLDENCARD);

        requ.setIdNumber(idNumber);
        requ.setEffectiveDate(effectiveDate);
        requ.setUseProductDtos(useProductDtos);

        //开卡方法
        CreateDeliveryResp resp = DeliveryLogic.CreateDelivery(requ);
        return RestResponseBo.ok("操作成功", this.Url("/delivery"), resp);

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
            return this.Redirect("/");
        }

        String imgUrl;
        try {

            if (!deliveryDto.getDeliveryStatus().equals(DeliveryStatus.DELIVERED.getValue())) {
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
