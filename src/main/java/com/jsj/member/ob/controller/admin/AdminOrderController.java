package com.jsj.member.ob.controller.admin;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.dto.RestResponseBo;
import com.jsj.member.ob.dto.api.order.OrderDto;
import com.jsj.member.ob.dto.api.product.ProductDto;
import com.jsj.member.ob.dto.api.product.ProductSpecDto;
import com.jsj.member.ob.entity.*;
import com.jsj.member.ob.enums.ActivityType;
import com.jsj.member.ob.enums.DictType;
import com.jsj.member.ob.enums.OrderStatus;
import com.jsj.member.ob.enums.ProductImgType;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.DictLogic;
import com.jsj.member.ob.logic.ProductLogic;
import com.jsj.member.ob.service.*;
import com.jsj.member.ob.utils.CCPage;
import com.jsj.member.ob.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
                        @RequestParam(value = "typeId", defaultValue = "0") Integer typeId,
                        @RequestParam(value = "status", defaultValue = "0") Integer status,
                        Model model) {
        EntityWrapper<Order> wrapper = new EntityWrapper<>();

        if (typeId > 0) {
            wrapper.where("type_id={0}", typeId);
        }
        if (status > 0) {
            wrapper.where("status={0}", status);
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

        OrderDto dto = new OrderDto();
        Order order = orderService.selectOne(new EntityWrapper<Order>().where("order_id={0}", orderId));
        BeanUtils.copyProperties(order, dto);

        //根据订单的openId查找昵称
        Wechat wechat = wechatService.selectById(orderId);
        if(wechat == null){
            dto.setNickname("");
        }else{
            dto.setNickname(wechat.getNickname());
        }

        //根据订单的领取优惠券id查找优惠券id查找优惠券名字
        WechatCoupon wechatCoupon = wechatCouponService.selectById(order.getWechatCouponId());
        if(wechatCoupon == null){
            dto.setCouponName("");
        }else{
            Coupon coupon = couponService.selectById(wechatCoupon.getCouponId());
            dto.setCouponName(coupon.getCouponName());
        }

        //根据订单Id查找商品信息
        List<ProductDto> productDtos = new ArrayList<>();
        List<ProductSpecDto> productSpecDtos = new ArrayList<>();
        List<OrderProduct> orderProducts = orderProductService.selectList(new EntityWrapper<OrderProduct>().where("order_id={0}", orderId));
        if(orderProducts == null && orderProducts.size() == 0){
            return "";
        }
        for (OrderProduct orderProduct : orderProducts) {
            ProductDto productDto = ProductLogic.GetProduct(orderProduct.getProductId());
            ProductSpecDto productSpecDto = ProductLogic.GetProductSpec(orderProduct.getProductSpecId());
            productSpecDtos.add(productSpecDto);
            productDto.setProductSpecDtos(productSpecDtos);
            productDtos.add(productDto);
        }
        dto.setProductDtos(productDtos);
        model.addAttribute("info",dto);
        model.addAttribute("orderProducts",orderProducts);
        model.addAttribute("productDtos",productDtos);

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
            case "delete": {
                Order order = orderService.selectById(id);
                order.setDeleteTime(DateUtils.getCurrentUnixTime());
                orderService.updateById(order);
            }
            case "cancel": {
                Order order = orderService.selectById(id);
                order.setStatus(OrderStatus.CANCEL.getValue());
                orderService.updateById(order);
            }
            break;
        }

        return RestResponseBo.ok("操作成功");

    }



}