package com.jsj.member.ob.controller.index;

import com.jsj.member.ob.controller.BaseController;
import com.jsj.member.ob.dto.api.gift.GiftDto;
import com.jsj.member.ob.dto.api.stock.StockDto;
import com.jsj.member.ob.logic.GiftLogic;
import com.jsj.member.ob.logic.StockLogic;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@ApiIgnore
@Controller
@RequestMapping("${webconfig.virtualPath}/gift")
public class GiftController extends BaseController {


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
        List<GiftDto> giveStocks = GiftLogic.GetGive(openId);

        request.setAttribute("giveStocks",giveStocks);

        //用户所有领取的
        HashSet<StockDto> receiveStocks = GiftLogic.GetReceived(openId);;
        request.setAttribute("receiveStocks",receiveStocks);

        return "index/gift";
    }

    /**
     * 赠送详情
     *
     * @param giftId
     * @param request
     * @return
     */
    @GetMapping("/give/{giftId}")
    public String giveInfo(@PathVariable("giftId") int giftId, HttpServletRequest request) {

        //赠送的库存
        List<StockDto> giveStocks = GiftLogic.GetGiftStocks(giftId);

        //领取库存信息
        List<StockDto> receiveStocks = new ArrayList<>();
        for (StockDto stockDto : giveStocks) {
            StockDto dto = StockLogic.GetChild(stockDto.getStockId());
            if (dto != null) {
                receiveStocks.add(dto);
            }
        }
        request.setAttribute("receiveStocks",receiveStocks);
        request.setAttribute("giveStocks", giveStocks);

        request.setAttribute("giftId",giftId);

        return "index/giftDetail";
    }


    /**
     * 领取详情
     *
     * @param giftId
     * @param request
     * @return
     */
    @GetMapping("/received/{giftId}")
    public String receivedInfo(@PathVariable("giftId") int giftId, HttpServletRequest request) {

        //赠送的库存
        List<StockDto> giveStocks = GiftLogic.GetGiftStocks(giftId);

        //领取库存信息
        List<StockDto> receiveStocks = new ArrayList<>();
        for (StockDto stockDto : giveStocks) {
            StockDto dto = StockLogic.GetChild(stockDto.getStockId());
            if (dto != null) {
                receiveStocks.add(dto);
            }
        }
        request.setAttribute("receiveStocks",receiveStocks);
        request.setAttribute("giveStocks", giveStocks);
        request.setAttribute("giftId", giftId);

        return "index/receiveDetail";
    }


}