package com.jsj.member.ob.controller.admin;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jsj.member.ob.dto.RestResponseBo;
import com.jsj.member.ob.entity.*;
import com.jsj.member.ob.enums.*;
import com.jsj.member.ob.logic.DictLogic;
import com.jsj.member.ob.service.*;
import com.jsj.member.ob.utils.CCPage;
import com.jsj.member.ob.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@ApiIgnore
@Controller
@RequestMapping("/admin/delivery")
public class AdminDeliveryController {


    @Autowired
    DeliveryService deliveryService;

    @Autowired
    DeliveryStockService deliveryStockService;

    /**
     * 查询所有订单列表
     *
     * @param page  当前页
     * @param limit 每页显示条数
     * @param model
     * @return
     */
    @GetMapping(value = {"", "/index"})
    public String index(@RequestParam(value = "page", defaultValue = "1") Integer page,
                        @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                        @RequestParam(value = "typeId", defaultValue = "0") Integer typeId,
                        @RequestParam(value = "status", defaultValue = "0") Integer status,
                        Model model) {
        EntityWrapper<Delivery> wrapper = new EntityWrapper<>();

        if (typeId > 0) {
            wrapper.where("type_id={0}", typeId);
        }
        if (status >= 0) {
            wrapper.where("status={0}", status);
        }
        wrapper.orderBy("create_time desc");

        Page<Delivery> pageInfo = new Page<>(page, limit);
        Page<Delivery> pp = deliveryService.selectPage(pageInfo, wrapper);

        //配送类型
        List<DeliveryType> deliveryTypes = Arrays.asList(DeliveryType.values());

        //配送状态
        List<DeliveryStatus> deliveryStatuses = Arrays.asList(DeliveryStatus.values());

        model.addAttribute("infos", new CCPage<>(pp, limit));
        model.addAttribute("activityTypes", deliveryTypes);
        model.addAttribute("orderStatuses", deliveryStatuses);

        model.addAttribute("typeId", typeId);
        model.addAttribute("status", status);

        return "admin/delivery/index";
    }

    @GetMapping("/send/{deliveryId}")
    public String sendInfo(@PathVariable("deliveryId") Integer deliveryId, Model model){
            Delivery delivery = new Delivery();
            if (deliveryId > 0) {
                delivery = deliveryService.selectById(deliveryId);
            }
            model.addAttribute("info", delivery);
            model.addAttribute("deliveryId", deliveryId);
            return "admin/delivery/info";

    }

    /**
     * 修改状态
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/status", method = RequestMethod.POST)
    @ResponseBody
    public RestResponseBo status(HttpServletRequest request) {

        int id = Integer.parseInt(request.getParameter("id"));
        String method = request.getParameter("method");

        Delivery delivery = deliveryService.selectById(id);

        if (method.equals("ifpass")) {
        }
        if (method.equals("send")) {
        }

        return RestResponseBo.ok("操作成功");

    }


}