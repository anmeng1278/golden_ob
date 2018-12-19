package com.jsj.member.ob.controller.admin;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.dto.RestResponseBo;
import com.jsj.member.ob.dto.api.delivery.DeliveryDto;
import com.jsj.member.ob.dto.api.dict.DictDto;
import com.jsj.member.ob.dto.api.dict.GetAreasResp;
import com.jsj.member.ob.dto.api.express.ExpressRequ;
import com.jsj.member.ob.dto.api.express.ExpressResp;
import com.jsj.member.ob.entity.Delivery;
import com.jsj.member.ob.entity.OrderProduct;
import com.jsj.member.ob.enums.DeliveryStatus;
import com.jsj.member.ob.enums.DeliveryType;
import com.jsj.member.ob.logic.DeliveryLogic;
import com.jsj.member.ob.logic.DictLogic;
import com.jsj.member.ob.logic.ExpressApiLogic;
import com.jsj.member.ob.service.DeliveryService;
import com.jsj.member.ob.service.DeliveryStockService;
import com.jsj.member.ob.service.OrderProductService;
import com.jsj.member.ob.service.StockService;
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
import java.util.ArrayList;
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

    @Autowired
    StockService stockService;

    @Autowired
    OrderProductService orderProductService;

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
                        @RequestParam(value = "keys", defaultValue = "") String keys,
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
        wrapper.where(!StringUtils.isBlank(keys), "(express_number like concat(concat('%',{0}),'%') or mobile like concat(concat('%',{0}),'%') or contact_name like concat(concat('%',{0}),'%') or open_id like concat(concat('%',{0}),'%')  )", keys);

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

        Delivery delivery = deliveryService.selectById(deliveryId);

        DeliveryDto deliveryDto = DeliveryLogic.ToDto(delivery);

        //查询配送的物流信息
        List data = null;
        if (!StringUtils.isBlank(delivery.getExpressNumber())) {
            ExpressRequ requ = new ExpressRequ();
            requ.setText(delivery.getExpressNumber());
            ExpressResp resp = ExpressApiLogic.GetExpressHundred(requ);
            data = resp.getData();
        }

        //配送的库存
        List<OrderProduct> orderProducts = new ArrayList<>();
        deliveryDto.getStockDtos().stream().forEach(s -> {
            OrderProduct orderProduct = orderProductService.selectOne(new EntityWrapper<OrderProduct>().where("order_id={0} and product_id={1} and product_spec_id={2}", s.getOrderId(), s.getProductId(), s.getProductSpecId()));
            orderProducts.add(orderProduct);
        });

        model.addAttribute("orderProducts", orderProducts);
        model.addAttribute("data", data);
        model.addAttribute("info", deliveryDto);
        model.addAttribute("deliveryId", deliveryId);
        return "admin/delivery/info";

    }


    /**
     * 发货页面
     *
     * @param deliveryId
     * @param model
     * @return
     * @throws IOException
     */
    @GetMapping("/sendOrUpdate/{deliveryId}/{id}")
    public String sendOrUpdate(@PathVariable("deliveryId") Integer deliveryId, @PathVariable("id") Integer id, Model model) throws IOException {

        Delivery delivery = deliveryService.selectById(deliveryId);

        model.addAttribute("info", delivery);
        model.addAttribute("id", id);
        model.addAttribute("deliveryId", deliveryId);
        if (id == 0) {
            //去发货
            return "admin/delivery/sendProduct";
        }
        //修改地址
        return "admin/delivery/updateAddress";
    }


    /**
     * 修改地址
     *
     * @param deliveryId
     * @param request
     * @return
     */
    @RequestMapping(value = "/sendOrUpdate/{deliveryId}/{id}", method = RequestMethod.POST)
    @ResponseBody
    @Transactional(Constant.DBTRANSACTIONAL)
    public RestResponseBo updateInfo(@PathVariable("deliveryId") Integer deliveryId, @PathVariable("id") Integer id, HttpServletRequest request) {

        Delivery delivery = deliveryService.selectById(deliveryId);

        switch (id){
            //去发货
            case 0:{
                String expressNumber = request.getParameter("expressNumber");
                if (StringUtils.isBlank(expressNumber)) {
                    expressNumber = "";
                }
                delivery.setExpressNumber(expressNumber);
                delivery.setStatus(DeliveryStatus.DELIVERED.getValue());
                delivery.setUpdateTime(DateUtils.getCurrentUnixTime());
            }
            break;
            //修改地址
            case 1:{
                String contactName = request.getParameter("contactName");
                Integer mobile = Integer.valueOf(request.getParameter("mobile"));

                Integer provinceId = 0, cityId = 0, districtId = 0;
                if (!StringUtils.isBlank(request.getParameter("provinceId"))) {
                    provinceId = Integer.valueOf(request.getParameter("provinceId"));
                }
                if (!StringUtils.isBlank(request.getParameter("cityId"))) {
                    cityId = Integer.valueOf(request.getParameter("cityId"));
                }
                if (!StringUtils.isBlank(request.getParameter("districtId"))) {
                    districtId = Integer.valueOf(request.getParameter("districtId"));
                }

                String address = request.getParameter("address");
                //修改状态已发货
                delivery.setMobile(mobile);
                delivery.setProvinceId(provinceId);
                delivery.setCityId(cityId);
                delivery.setDistrictId(districtId);
                delivery.setContactName(contactName);
                delivery.setAddress(address);
                delivery.setUpdateTime(DateUtils.getCurrentUnixTime());
            }
            break;
        }
        deliveryService.updateAllColumnById(delivery);
        return RestResponseBo.ok("操作成功");

    }

    /**
     * 获取省市区
     *
     * @param parentAreaId
     * @return
     */
    @RequestMapping(value = "/chooseArea/{parentAreaId}", method = RequestMethod.POST)
    @ResponseBody
    public RestResponseBo chooseArea(@PathVariable("parentAreaId") int parentAreaId) {
        GetAreasResp getAreasResp = DictLogic.GetCascade(parentAreaId);
        List<DictDto> areas = getAreasResp.getAreas();
        return RestResponseBo.ok(areas);
    }


}