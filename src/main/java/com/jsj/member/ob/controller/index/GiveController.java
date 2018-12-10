package com.jsj.member.ob.controller.index;

import com.jsj.member.ob.dto.api.gift.GiftDto;
import com.jsj.member.ob.dto.api.stock.StockDto;
import com.jsj.member.ob.logic.GiftLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/give")
public class GiveController {

    /**
     * 赠送详情
     * @param openId
     * @param giftId
     * @param model
     * @return
     */
    @GetMapping("/giveDetail")
    public String getGiveDetail(@RequestParam(value = "openId", defaultValue = "111") String openId,
                                @RequestParam("giftId") Integer giftId, Model model){

        List<StockDto> stockDtos = GiftLogic.GetGiftStocks(giftId);

        model.addAttribute("openId",openId);
        model.addAttribute("giftId",giftId);
        model.addAttribute("stockDtos",stockDtos);

        return "index/share/GiveDetail";

    }


}
