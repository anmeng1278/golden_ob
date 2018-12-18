package com.jsj.member.ob.controller.index;

import com.jsj.member.ob.controller.BaseController;
import com.jsj.member.ob.dto.RestResponseBo;
import com.jsj.member.ob.dto.api.gift.CreateGiftRequ;
import com.jsj.member.ob.dto.api.gift.CreateGiftResp;
import com.jsj.member.ob.dto.api.gift.GiftProductDto;
import com.jsj.member.ob.dto.api.stock.StockDto;
import com.jsj.member.ob.enums.GiftShareType;
import com.jsj.member.ob.logic.GiftLogic;
import com.jsj.member.ob.logic.StockLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@ApiIgnore
@Controller
@RequestMapping("${webconfig.virtualPath}/stock")
public class StockController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(StockController.class);

    @GetMapping(value = {""})
    public String index(HttpServletRequest request) {

        String openId = this.OpenId();

        List<StockDto> stockDtos = StockLogic.GetStocks(openId);
        request.setAttribute("stockDtos", stockDtos);

        return "index/stock";
    }

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

}
