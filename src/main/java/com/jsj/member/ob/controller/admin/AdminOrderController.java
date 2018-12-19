package com.jsj.member.ob.controller.admin;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jsj.member.ob.dto.RestResponseBo;
import com.jsj.member.ob.dto.api.order.OrderDto;
import com.jsj.member.ob.dto.api.order.OrderProductDto;
import com.jsj.member.ob.dto.api.product.ProductDto;
import com.jsj.member.ob.entity.Order;
import com.jsj.member.ob.enums.ActivityType;
import com.jsj.member.ob.enums.OrderStatus;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.OrderLogic;
import com.jsj.member.ob.logic.ProductLogic;
import com.jsj.member.ob.service.*;
import com.jsj.member.ob.utils.CCPage;
import com.jsj.member.ob.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ApiIgnore
@Controller
@RequestMapping("/admin/order")
public class AdminOrderController {


    @Autowired
    OrderService orderService;

    @Autowired
    OrderProductService orderProductService;

    @Autowired
    ProductImgService productImgService;

    @Autowired
    WechatService wechatService;

    @Autowired
    WechatCouponService wechatCouponService;

    @Autowired
    CouponService couponService;

    @Autowired
    ActivityService activityService;


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
                        @RequestParam(value = "typeId", defaultValue = "-1") Integer typeId,
                        @RequestParam(value = "status", defaultValue = "-1") Integer status,
                        @RequestParam(value = "startDate", defaultValue = "") String startDate,
                        @RequestParam(value = "endDate", defaultValue = "") String endDate,
                        @RequestParam(value = "keys", defaultValue = "") String keys,
                        Model model) {
        EntityWrapper<Order> wrapper = new EntityWrapper<>();

        if (typeId > -1) {
            wrapper.where("type_id={0}", typeId);
        }
        if (status > -1) {
            wrapper.where("status={0}", status);
        }
        wrapper.where(!StringUtils.isBlank(keys), "(open_id like concat(concat('%',{0}),'%') or order_id like concat(concat('%',{0}),'%')  )", keys);

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

        Page<Order> pageInfo = new Page<>(page, limit);
        Page<Order> pp = orderService.selectPage(pageInfo, wrapper);

        //订单类型
        List<ActivityType> activityTypes = Arrays.asList(ActivityType.values());

        //订单状态
        List<OrderStatus> orderStatuses = Arrays.asList(OrderStatus.values());

        model.addAttribute("infos", new CCPage<>(pp, limit));
        model.addAttribute("activityTypes", activityTypes);
        model.addAttribute("orderStatuses", orderStatuses);

        model.addAttribute("typeId", typeId);
        model.addAttribute("status", status);
        model.addAttribute("keys", keys);

        return "admin/order/index";
    }


    /**
     * 订单详情
     *
     * @param orderId
     * @param model
     * @return
     */
    @RequestMapping(value = "/{orderId}", method = RequestMethod.GET)
    public String info(@PathVariable("orderId") Integer orderId, Model model) {

        Order order = orderService.selectOne(new EntityWrapper<Order>().where("order_id={0}", orderId));
        OrderDto dto = OrderLogic.ToDto(order);

        //根据订单Id查找商品信息
        List<OrderProductDto> orderProductDtos = OrderLogic.GetOrderProducts(dto.getOrderId());

        List<ProductDto> productDtos = new ArrayList<>();
        orderProductDtos.forEach(orderProductDto -> {
            ProductDto productDto = ProductLogic.GetProduct(orderProductDto.getProductId());
            productDtos.add(productDto);
        });

        dto.setOrderProductDtos(orderProductDtos);
        dto.setProductDtos(productDtos);

        model.addAttribute("info", dto);

        return "admin/order/info";
    }


    /**
     * 修改状态
     *
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value = "/status", method = RequestMethod.POST)
    @ResponseBody
    public RestResponseBo modifyStatus(@RequestParam(value = "id", defaultValue = "0") Integer id,
                                       HttpServletRequest request) {

        String method = request.getParameter("method");
        if (StringUtils.isBlank(method)) {
            throw new TipException("方法名不能为空");
        }

        switch (method) {
            //删除订单
            case "delete": {
                OrderLogic.DeleteOrder(id);
            }
            break;

            //取消订单
            case "cancel": {
                OrderLogic.CancelOrder(id);
            }
            break;
        }

        return RestResponseBo.ok("操作成功");

    }


}