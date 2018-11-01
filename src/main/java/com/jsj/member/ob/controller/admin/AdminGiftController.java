package com.jsj.member.ob.controller.admin;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jsj.member.ob.dto.api.gift.GiftDto;
import com.jsj.member.ob.dto.api.stock.StockDto;
import com.jsj.member.ob.entity.Gift;
import com.jsj.member.ob.enums.GiftShareType;
import com.jsj.member.ob.enums.GiftStatus;
import com.jsj.member.ob.logic.GiftLogic;
import com.jsj.member.ob.service.GiftService;
import com.jsj.member.ob.utils.CCPage;
import com.jsj.member.ob.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ApiIgnore
@Controller
@RequestMapping("/admin/gift")
public class AdminGiftController {

    @Autowired
    GiftService giftService;

    /**
     * 查询用户列表
     *
     * @param page
     * @param limit
     * @return
     */
    @GetMapping(value = {"", "/index"})
    public String index(@RequestParam(value = "page", defaultValue = "1") Integer page,
                        @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                        @RequestParam(value = "giftShareType", defaultValue = "-1") Integer giftShareType,
                        @RequestParam(value = "giftStatus", defaultValue = "-1") Integer giftStatus,

                        @RequestParam(value = "startDate", defaultValue = "") String startDate,
                        @RequestParam(value = "endDate", defaultValue = "") String endDate,

                        HttpServletRequest request) {


        EntityWrapper<Gift> wrapper = new EntityWrapper<>();
        wrapper.where("delete_time is null");
        if (giftShareType > -1) {
            wrapper.where("share_type={0}", giftShareType);
        }
        if (giftStatus > -1) {
            wrapper.where("status={0}", giftStatus);
        }

        if (!org.apache.commons.lang3.StringUtils.isBlank(startDate) && !org.apache.commons.lang3.StringUtils.isBlank(endDate)) {
            int startUnix = DateUtils.getUnixTimeByDate(DateUtils.dateFormat(startDate, "yyyy-MM-dd"));
            int endUnix = DateUtils.getUnixTimeByDate(DateUtils.dateFormat(endDate, "yyyy-MM-dd"));
            endUnix += 60 * 60 * 24;
            wrapper.between("create_time", startUnix, endUnix);
        } else if (!org.apache.commons.lang3.StringUtils.isBlank(startDate)) {
            int startUnix = DateUtils.getUnixTimeByDate(DateUtils.dateFormat(startDate, "yyyy-MM-dd"));
            wrapper.gt("create_time", startUnix);
        } else if (!org.apache.commons.lang3.StringUtils.isBlank(endDate)) {
            int endUnix = DateUtils.getUnixTimeByDate(DateUtils.dateFormat(endDate, "yyyy-MM-dd"));
            endUnix += 60 * 60 * 24;
            wrapper.lt("create_time", endUnix);
        }

        wrapper.orderBy("create_time desc");

        Page<Gift> pageInfo = new Page<>(page, limit);
        Page<Gift> pp = giftService.selectPage(pageInfo, wrapper);

        GiftShareType[] giftShareTypes = GiftShareType.values();
        GiftStatus[] giftStatuses = GiftStatus.values();

        request.setAttribute("giftShareTypes", giftShareTypes);
        request.setAttribute("giftStatuses", giftStatuses);
        request.setAttribute("infos", new CCPage<Gift>(pp, limit));

        request.setAttribute("startDate", startDate);
        request.setAttribute("endDate", endDate);

        request.setAttribute("giftShareType", giftShareType);
        request.setAttribute("giftStatus", giftStatus);

        return "admin/gift/index";

    }


    /**
     * 获取库存流转日志
     *
     * @param giftId
     * @return
     */
    @RequestMapping(value = "/{giftId}", method = RequestMethod.GET)
    public String info(@PathVariable("giftId") Integer giftId,
                       HttpServletRequest request) {

        GiftDto info = GiftLogic.GetGift(giftId);
        List<StockDto> stockDtos = GiftLogic.GetGiftStocks(giftId);

        Integer stockId = 0;
        if (!org.apache.commons.lang3.StringUtils.isBlank(request.getParameter("stockId"))) {
            stockId = Integer.parseInt(request.getParameter("stockId"));
        }

        request.setAttribute("info", info);
        request.setAttribute("stockDtos", stockDtos);
        request.setAttribute("stockId", stockId);

        return "admin/gift/info";
    }
}
