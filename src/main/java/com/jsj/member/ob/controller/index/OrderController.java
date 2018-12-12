package com.jsj.member.ob.controller.index;

import com.jsj.member.ob.controller.BaseController;
import com.jsj.member.ob.dto.RestResponseBo;
import com.jsj.member.ob.dto.api.order.OrderDto;
import com.jsj.member.ob.dto.api.order.OrderProductDto;
import com.jsj.member.ob.enums.OrderFlag;
import com.jsj.member.ob.logic.OrderLogic;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ApiIgnore
@Controller
@RequestMapping("/order")
public class OrderController extends BaseController {

    /**
     * 订单列表
     *
     * @param request
     * @return
     */
    @GetMapping(value = {"/order/{orderFlag}"})
    public String order(@PathVariable("orderFlag") int orderFlag, HttpServletRequest request) {

        String openId = this.OpenId();

        List<OrderDto> orderDtos = OrderLogic.GetMyOrder(openId, orderFlag);

        request.setAttribute("orderDtos",orderDtos);
        request.setAttribute("orderFlag",orderFlag);

        return "index/OrderList";
    }

    @GetMapping(value = {"/cancelOrder/{orderId}"})
    public String cancelOrder(@PathVariable("orderId") int orderId, HttpServletRequest request){

        OrderLogic.CancelOrder(orderId);

        return "redirect:index/OrderList/"+orderId;
    }

}