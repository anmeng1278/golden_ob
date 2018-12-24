package com.jsj.member.ob.controller.index;

import com.jsj.member.ob.controller.BaseController;
import com.jsj.member.ob.dto.RestResponseBo;
import com.jsj.member.ob.dto.api.order.OrderDto;
import com.jsj.member.ob.dto.thirdParty.GetPayTradeResp;
import com.jsj.member.ob.enums.OrderFlag;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.OrderLogic;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@ApiIgnore
@Controller
@RequestMapping("${webconfig.virtualPath}/order")
public class OrderController extends BaseController {

    /**
     * 订单列表
     *
     * @param request
     * @return
     */
    @GetMapping(value = {""})
    public String index(HttpServletRequest request) {

        OrderFlag orderFlag = OrderFlag.ALLORDERS;
        if (!StringUtils.isEmpty(request.getParameter("orderFlag"))) {
            orderFlag = OrderFlag.valueOf(Integer.parseInt(request.getParameter("orderFlag")));
        }

        String openId = this.OpenId();
        List<OrderDto> orderDtos = OrderLogic.GetOrders(openId, orderFlag);

        request.setAttribute("orderDtos", orderDtos);
        request.setAttribute("orderFlag", orderFlag);

        return "index/order";
    }

    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    @ResponseBody
    public RestResponseBo cancel(HttpServletRequest request) {

        int orderId = Integer.parseInt(request.getParameter("orderId"));
        OrderLogic.DeleteOrder(orderId);

        return RestResponseBo.ok("订单取消成功");
    }

    @RequestMapping(value = "/createPay", method = RequestMethod.POST)
    @ResponseBody
    public RestResponseBo createPay(HttpServletRequest request) throws Exception {

        int orderId = Integer.parseInt(request.getParameter("orderId"));
        GetPayTradeResp pay = this.createPay(orderId);

        OrderDto orderDto = OrderLogic.GetOrder(orderId);

        HashMap<String, Object> data = new HashMap<>();

        if (!pay.getResponseHead().getCode().equals("0000")) {
            throw new TipException(pay.getResponseHead().getMessage());
        }
        data.put("pay", pay);

        String successUrl = String.format("/pay/success/%s", orderDto.getOrderUniqueCode());
        data.put("successUrl", this.Url(successUrl));

        String url = this.Url("/order");
        return RestResponseBo.ok("创建订单成功", url, data);

    }

}