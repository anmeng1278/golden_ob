package com.jsj.member.ob.controller.admin;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jsj.member.ob.dto.RestResponseBo;
import com.jsj.member.ob.dto.api.gift.GiftDto;
import com.jsj.member.ob.dto.api.stock.StockDto;
import com.jsj.member.ob.dto.api.stock.StockFlowDto;
import com.jsj.member.ob.dto.api.wechat.WechatDto;
import com.jsj.member.ob.entity.Stock;
import com.jsj.member.ob.entity.Wechat;
import com.jsj.member.ob.logic.GiftLogic;
import com.jsj.member.ob.logic.StockLogic;
import com.jsj.member.ob.logic.WechatLogic;
import com.jsj.member.ob.service.GiftService;
import com.jsj.member.ob.service.GiftStockService;
import com.jsj.member.ob.service.StockService;
import com.jsj.member.ob.service.WechatService;
import com.jsj.member.ob.utils.CCPage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ApiIgnore
@Controller
@RequestMapping("/admin/wechat")
public class AdminWechatController {

    @Autowired
    private WechatService wechatService;

    @Autowired
    private StockService stockService;

    @Autowired
    private GiftService giftService;

    @Autowired
    private GiftStockService giftStockService;

    /**
     * 查询用户列表
     *
     * @param page
     * @param limit
     * @param keys
     * @param model
     * @return
     */
    @GetMapping(value = {"", "/index"})
    public String index(@RequestParam(value = "page", defaultValue = "1") Integer page,
                        @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                        @RequestParam(value = "keys", defaultValue = "") String keys, Model model) {

        EntityWrapper<Wechat> wrapper = new EntityWrapper<>();
        wrapper.where("delete_time is null");
        wrapper.where(!StringUtils.isBlank(keys), "(nickname LIKE concat(concat('%',{0}),'%') or open_id like concat(concat('%',{0}),'%') )", keys);
        wrapper.orderBy("create_time desc");

        Page<Wechat> pageInfo = new Page<>(page, limit);
        Page<Wechat> pp = wechatService.selectPage(pageInfo, wrapper);

        model.addAttribute("keys", keys);
        model.addAttribute("infos", new CCPage<Wechat>(pp, limit));

        return "admin/wechat/index";

    }


    /**
     * 用户库存
     *
     * @param openId
     * @param request
     * @return
     */
    @RequestMapping(value = "/{openId}/stock", method = RequestMethod.GET)
    public String stock(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "limit", defaultValue = "30") Integer limit,
            @PathVariable("openId") String openId,
            HttpServletRequest request) {

        WechatDto wechat = WechatLogic.GetWechat(openId);

        EntityWrapper<Stock> wrapper = new EntityWrapper<>();
        wrapper.where("delete_time is null");
        wrapper.where("open_id={0}", openId);
        wrapper.orderBy("status asc, create_time desc");

        Page<Stock> pageInfo = new Page<>(page, limit);
        Page<Stock> pp = stockService.selectPage(pageInfo, wrapper);

        request.setAttribute("wechat", wechat);
        request.setAttribute("infos", new CCPage<Stock>(pp, limit));
        return "admin/wechat/wechatStock";

    }

    /**
     * 获取库存流转日志
     *
     * @param stockId
     * @return
     */
    @RequestMapping(value = "/{stockId}/stockFlows", method = RequestMethod.POST)
    @ResponseBody
    public RestResponseBo stockFlows(@PathVariable("stockId") Integer stockId) {

        List<StockFlowDto> stockFlows = StockLogic.GetStockFlows(stockId, true);
        return RestResponseBo.ok(stockFlows);

    }

    /**
     * 获取库存流转日志
     *
     * @param giftId
     * @return
     */
    @RequestMapping(value = "/giftStock/{giftId}/{stockId}", method = RequestMethod.GET)
    public String gift(@PathVariable("giftId") Integer giftId,
                       @PathVariable("stockId") Integer stockId,
                       HttpServletRequest request) {

        GiftDto info = GiftLogic.GetGift(giftId);
        List<StockDto> stockDtos = GiftLogic.GetGiftStocks(giftId);

        request.setAttribute("info", info);
        request.setAttribute("stockDtos", stockDtos);
        request.setAttribute("stockId", stockId);

        return "admin/wechat/giftStocks";
    }
}
