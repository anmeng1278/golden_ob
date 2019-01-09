package com.jsj.member.ob.controller.index;

import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.controller.BaseController;
import com.jsj.member.ob.dto.RestResponseBo;
import com.jsj.member.ob.dto.api.gift.CancelGiftRequ;
import com.jsj.member.ob.dto.api.gift.GiftDto;
import com.jsj.member.ob.dto.api.gift.UserDrawDto;
import com.jsj.member.ob.dto.api.gift.UserGiftDto;
import com.jsj.member.ob.dto.api.stock.StockDto;
import com.jsj.member.ob.enums.GiftStatus;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.GiftLogic;
import com.jsj.member.ob.logic.StockLogic;
import com.jsj.member.ob.service.GiftStockService;
import com.jsj.member.ob.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ApiIgnore
@Controller
@RequestMapping("${webconfig.virtualPath}/gift")
public class GiftController extends BaseController {

    @Autowired
    StockService stockService;

    @Autowired
    GiftStockService giftStockService;

    /**
     * 配送列表
     *
     * @param request
     * @return
     */
    @GetMapping("")
    public String index(HttpServletRequest request) {

        String openId = this.OpenId();

        //用户所有赠送的
        List<UserGiftDto> giftDtos = GiftLogic.GetGives(openId);
        request.setAttribute("giftDtos", giftDtos);

        //用户所有领取的
        List<UserDrawDto> drawDtos = GiftLogic.GetReceived(openId);
        request.setAttribute("drawDtos", drawDtos);

        return "index/gift";
    }

    /**
     * 赠送详情
     *
     * @param giftUniqueCode
     * @param request
     * @return
     */
    @GetMapping("/give/{giftUniqueCode}")
    public String giveInfo(@PathVariable("giftUniqueCode") String giftUniqueCode, HttpServletRequest request) {

        //礼包信息
        GiftDto giftDto = GiftLogic.GetGift(giftUniqueCode);

        //赠送的库存
        List<StockDto> giveStock = GiftLogic.GetGiftStocks(giftDto.getGiftId());
        //去重计算数量
        List<StockDto> giveStocks = StockLogic.FilterData(giveStock);


        //领取的库存
        List<StockDto> receiveStock = GiftLogic.GetGiftRecevied(null, giftDto.getGiftId());
        //去重计算数量
        List<StockDto> receiveStocks = StockLogic.FilterData(receiveStock);

        //未分享
        if(giftDto.getGiftStatus() == GiftStatus.UNSHARE){

            String shareUrl = this.Url(String.format("/share/gift/%s/draw", giftDto.getGiftUniqueCode()), false);
            request.setAttribute("shareUrl", shareUrl);

            //分享图片
            if (giftDto.getStockDtos().get(0).getProductDto().getProductImgDtos() != null) {
                String imgUrl = giftDto.getStockDtos().get(0).getProductDto().getProductImgDtos().get(0).getImgPath();
                request.setAttribute("imgUrl", imgUrl);
            }

        }

        request.setAttribute("receiveStocks", receiveStocks);
        request.setAttribute("giveStocks", giveStocks);
        request.setAttribute("giftDto", giftDto);

        return "index/giftDetail";
    }


    /**
     * 领取详情
     *
     * @param giftUniqueCode
     * @param request
     * @return
     */
    @GetMapping("/received/{giftUniqueCode}")
    public String receivedInfo(@PathVariable("giftUniqueCode") String giftUniqueCode, HttpServletRequest request) {

        //礼包信息
        GiftDto giftDto = GiftLogic.GetGift(giftUniqueCode);

        //赠送的库存
        List<StockDto> giveStock = GiftLogic.GetGiftStocks(giftDto.getGiftId());
        //去重计算数量
        List<StockDto> giveStocks = StockLogic.FilterData(giveStock);


        //领取的库存
        List<StockDto> receiveStock = GiftLogic.GetGiftRecevied(null, giftDto.getGiftId());
        //去重计算数量
        List<StockDto> receiveStocks = StockLogic.FilterData(receiveStock);

        request.setAttribute("receiveStocks", receiveStocks);
        request.setAttribute("giveStocks", giveStocks);
        request.setAttribute("giftDto", giftDto);

        return "index/receiveDetail";
    }


    @PostMapping(value = "/cancel")
    @Transactional(Constant.DBTRANSACTIONAL)
    @ResponseBody
    public RestResponseBo cancelGift(HttpServletRequest request){

        String openId = this.OpenId();

        String giftUniqueCode = request.getParameter("giftUniqueCode");

        //判断是否本人取消
        GiftDto giftDto = GiftLogic.GetGift(giftUniqueCode);
        if (!giftDto.getOpenId().equals(openId)) {
           Redirect("/stock");
        }

        try {
            CancelGiftRequ requ = new CancelGiftRequ();
            requ.getBaseRequ().setOpenId(openId);
            requ.setGiftUniqueCode(giftUniqueCode);

            GiftLogic.CancelGift(requ);

            return RestResponseBo.ok("取消成功");

        }catch (TipException ex) {

            return RestResponseBo.fail(ex.getMessage(), null);

        }

    }
}