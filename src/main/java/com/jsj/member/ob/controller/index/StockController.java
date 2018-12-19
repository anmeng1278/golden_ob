package com.jsj.member.ob.controller.index;

import com.alibaba.fastjson.JSON;
import com.jsj.member.ob.controller.BaseController;
import com.jsj.member.ob.dto.RestResponseBo;
import com.jsj.member.ob.dto.api.delivery.CreateDeliveryRequ;
import com.jsj.member.ob.dto.api.delivery.CreateDeliveryResp;
import com.jsj.member.ob.dto.api.gift.CreateGiftRequ;
import com.jsj.member.ob.dto.api.gift.CreateGiftResp;
import com.jsj.member.ob.dto.api.gift.GiftProductDto;
import com.jsj.member.ob.dto.api.stock.StockDto;
import com.jsj.member.ob.dto.api.stock.UseProductDto;
import com.jsj.member.ob.dto.api.airport.JsAirportDto;
import com.jsj.member.ob.enums.DeliveryType;
import com.jsj.member.ob.enums.GiftShareType;
import com.jsj.member.ob.enums.PropertyType;
import com.jsj.member.ob.enums.AirportType;
import com.jsj.member.ob.logic.DeliveryLogic;
import com.jsj.member.ob.logic.GiftLogic;
import com.jsj.member.ob.logic.StockLogic;
import com.jsj.member.ob.logic.AirportLogic;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@ApiIgnore
@Controller
@RequestMapping("${webconfig.virtualPath}/stock")
public class StockController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(StockController.class);

    //region 我的库存 index

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

        return "index/stock";
    }
    //endregion

    //region 微信送好友 createGift

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

    /**
     * 实物使用
     * @param request
     * @return
     */
    @PostMapping(value = {"/use1"})
    @ResponseBody
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
}
