package com.jsj.member.ob.controller.admin;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jsj.member.ob.controller.BaseController;
import com.jsj.member.ob.dto.RestResponseBo;
import com.jsj.member.ob.dto.api.gift.GiftDto;
import com.jsj.member.ob.dto.api.stock.StockDto;
import com.jsj.member.ob.dto.api.stock.StockFlowDto;
import com.jsj.member.ob.dto.api.wechat.WechatDto;
import com.jsj.member.ob.entity.Stock;
import com.jsj.member.ob.entity.Wechat;
import com.jsj.member.ob.enums.StockStatus;
import com.jsj.member.ob.enums.StockType;
import com.jsj.member.ob.logic.GiftLogic;
import com.jsj.member.ob.logic.StockLogic;
import com.jsj.member.ob.logic.WechatLogic;
import com.jsj.member.ob.service.StockService;
import com.jsj.member.ob.service.WechatService;
import com.jsj.member.ob.utils.CCPage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ApiIgnore
@Controller
@RequestMapping("/admin/wechat")
public class AdminWechatController extends BaseController {

    @Autowired
    private WechatService wechatService;

    @Autowired
    private StockService stockService;

    @Autowired
    ApplicationContext applicationContext;


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
                        @RequestParam(value = "keys", defaultValue = "") String keys,
                        Model model
    ) {

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
            @RequestParam(value = "limit", defaultValue = "10") Integer limit,
            @PathVariable("openId") String openId,
            @RequestParam(value = "stockStatus", defaultValue = "-1") Integer stockStatus,
            @RequestParam(value = "stockType", defaultValue = "-1") Integer stockType,
            HttpServletRequest request) {

        WechatDto wechat = WechatLogic.GetWechat(openId);

        EntityWrapper<Stock> wrapper = new EntityWrapper<>();
        wrapper.where("delete_time is null");
        wrapper.where("open_id={0}", openId);
        wrapper.where(stockStatus > -1, "status={0}", stockStatus);
        wrapper.where(stockType > -1, "type_id={0}", stockType);

        wrapper.orderBy("create_time desc");

        Page<Stock> pageInfo = new Page<>(page, limit);
        Page<Stock> pp = stockService.selectPage(pageInfo, wrapper);

        StockStatus[] stockStatuses = StockStatus.values();
        StockType[] stockTypes = StockType.values();

        request.setAttribute("wechat", wechat);
        request.setAttribute("infos", new CCPage<Stock>(pp, limit));
        request.setAttribute("stockStatuses", stockStatuses);
        request.setAttribute("stockTypes", stockTypes);

        request.setAttribute("stockType", stockType);
        request.setAttribute("stockStatus", stockStatus);

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
