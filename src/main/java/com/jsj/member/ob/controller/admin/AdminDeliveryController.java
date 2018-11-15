package com.jsj.member.ob.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.dto.RestResponseBo;
import com.jsj.member.ob.dto.api.express.ExpressRequ;
import com.jsj.member.ob.dto.api.express.ExpressResp;
import com.jsj.member.ob.entity.*;
import com.jsj.member.ob.enums.*;
import com.jsj.member.ob.logic.DeliveryLogic;
import com.jsj.member.ob.logic.ExpressApiLogic;
import com.jsj.member.ob.service.*;
import com.jsj.member.ob.utils.CCPage;
import com.jsj.member.ob.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@ApiIgnore
@Controller
@RequestMapping("/admin/delivery")
public class AdminDeliveryController {


    @Autowired
    DeliveryService deliveryService;

    @Autowired
    DeliveryStockService deliveryStockService;

    /**
     * 查询所有配送列表
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
                        @RequestParam(value = "startDate", defaultValue = "") String startDate,
                        @RequestParam(value = "endDate", defaultValue = "") String endDate,
                        @RequestParam(value = "status", defaultValue = "-1") Integer status,
                        Model model) {
        EntityWrapper<Delivery> wrapper = new EntityWrapper<>();
        wrapper.where("delete_time is null");

        if (typeId > 0) {
            wrapper.where("type_id={0}", typeId);
        }
        if (status >= 0) {
            wrapper.where("status={0}", status);
        }
        if (!StringUtils.isBlank(startDate) && !StringUtils.isBlank(endDate)) {
            int startUnix = DateUtils.getUnixTimeByDate(DateUtils.dateFormat(startDate, "yyyy-MM-dd"));
            int endUnix = DateUtils.getUnixTimeByDate(DateUtils.dateFormat(endDate, "yyyy-MM-dd"));
            endUnix += 60 * 60 * 24;
            wrapper.between("create_time", startUnix, endUnix);
        } else if (!StringUtils.isBlank(startDate)) {
            int startUnix = DateUtils.getUnixTimeByDate(DateUtils.dateFormat(startDate, "yyyy-MM-dd"));
            wrapper.gt("create_time", startUnix);
        } else if (!StringUtils.isBlank(endDate)) {
            int endUnix = DateUtils.getUnixTimeByDate(DateUtils.dateFormat(endDate, "yyyy-MM-dd"));
            endUnix += 60 * 60 * 24;
            wrapper.lt("create_time", endUnix);
        }
        wrapper.orderBy("create_time desc");

        Page<Delivery> pageInfo = new Page<>(page, limit);
        Page<Delivery> pp = deliveryService.selectPage(pageInfo, wrapper);

        //配送类型
        List<DeliveryType> deliveryTypes = Arrays.asList(DeliveryType.values());

        //配送状态
        List<DeliveryStatus> deliveryStatuses = Arrays.asList(DeliveryStatus.values());

        model.addAttribute("infos", new CCPage<>(pp, limit));
        model.addAttribute("deliveryTypes", deliveryTypes);
        model.addAttribute("deliveryStatuses", deliveryStatuses);

        model.addAttribute("typeId", typeId);
        model.addAttribute("status", status);

        return "admin/delivery/index";
    }

    /**
     * 配送详情
     *
     * @param deliveryId
     * @param model
     * @return
     * @throws IOException
     */
    @GetMapping("/sendInfo/{deliveryId}")
    public String sendInfo(@PathVariable("deliveryId") Integer deliveryId, Model model) throws IOException {

        Delivery delivery = new Delivery();
        delivery = deliveryService.selectById(deliveryId);

        List<Map<String, String>> resps = DeliveryLogic.GetDeliveryExpress(delivery.getExpressNumber());

        model.addAttribute("resps", resps);
        model.addAttribute("info", delivery);
        model.addAttribute("deliveryId", deliveryId);
        return "admin/delivery/info";

    }


    /**
     * 去发货页面
     *
     * @param deliveryId
     * @param model
     * @return
     * @throws IOException
     */
    @GetMapping("/sendProduct/{deliveryId}")
    public String sendProduct(@PathVariable("deliveryId") Integer deliveryId, Model model) throws IOException {

        Delivery delivery = new Delivery();
        delivery = deliveryService.selectById(deliveryId);

        model.addAttribute("info", delivery);
        model.addAttribute("deliveryId", deliveryId);
        return "admin/delivery/send";
    }


    @RequestMapping(value = "/sendProduct/{deliveryId}", method = RequestMethod.POST)
    @ResponseBody
    @Transactional(Constant.DBTRANSACTIONAL)
    public RestResponseBo updateInfo(@PathVariable("deliveryId") Integer deliveryId, HttpServletRequest request) {

        Delivery delivery = new Delivery();

        String expressNumber = request.getParameter("expressNumber");
        if (StringUtils.isBlank(expressNumber)) {
            expressNumber = "";
        }
        String openId = request.getParameter("openId");
        String contactName = request.getParameter("contactName");
        Integer mobile = Integer.valueOf(request.getParameter("mobile"));
        String province = request.getParameter("province");
        String city = request.getParameter("city");
        String district = request.getParameter("district");
        String address = request.getParameter("address");

        if (deliveryId > 0) {

            delivery = deliveryService.selectById(deliveryId);
            //修改状态已发货
            delivery.setExpressNumber(expressNumber);
            delivery.setMobile(mobile);
            delivery.setOpenId(openId);
            delivery.setProvince(province);
            delivery.setCity(city);
            delivery.setContactName(contactName);
            delivery.setDistrict(district);
            delivery.setAddress(address);
            delivery.setStatus(DeliveryStatus.DELIVERED.getValue());
            delivery.setUpdateTime(DateUtils.getCurrentUnixTime());
            deliveryService.updateById(delivery);
        }
        return RestResponseBo.ok("发货成功");

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