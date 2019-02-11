package com.jsj.member.ob.controller.index;

import com.jsj.member.ob.controller.BaseController;
import com.jsj.member.ob.dto.RestResponseBo;
import com.jsj.member.ob.dto.api.order.OrderDto;
import com.jsj.member.ob.dto.api.order.UserOrderDto;
import com.jsj.member.ob.dto.thirdParty.GetPayTradeResp;
import com.jsj.member.ob.enums.OrderFlag;
import com.jsj.member.ob.enums.SourceType;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.OrderLogic;
import com.jsj.member.ob.tuple.TwoTuple;
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

    //region (public) 订单列表 index

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

        String unionId = this.UnionId();
        List<UserOrderDto> orderDtos = OrderLogic.GetOrders(unionId, orderFlag);

        request.setAttribute("orderDtos", orderDtos);
        request.setAttribute("orderFlag", orderFlag);
        return "index/order";
    }
    //endregion

    //region (public) 取消订单 cancel

    /**
     * 取消订单
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    @ResponseBody
    public RestResponseBo cancel(HttpServletRequest request) {

        int orderId = Integer.parseInt(request.getParameter("orderId"));
        OrderLogic.CancelOrder(orderId);

        return RestResponseBo.ok("订单取消成功");
    }
    //endregion

    //region (public) 创建支付请求 createPay

    /**
     * 创建支付请求
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/createPay", method = RequestMethod.POST)
    @ResponseBody
    public RestResponseBo createPay(HttpServletRequest request) throws Exception {

        int orderId = Integer.parseInt(request.getParameter("orderId"));
        TwoTuple<GetPayTradeResp, SourceType> twoTuple = this.createPay(request, orderId);

        OrderDto orderDto = OrderLogic.GetOrder(orderId);
        HashMap<String, Object> data = new HashMap<>();

        if (!twoTuple.first.getResponseHead().getCode().equals("0000")) {
            throw new TipException(twoTuple.first.getResponseHead().getMessage());
        }
        data.put("pay", twoTuple.first);
        data.put("source", twoTuple.second.getValue());

        String successUrl = String.format("/pay/success/%s", orderDto.getOrderUniqueCode());
        data.put("successUrl", this.Url(successUrl));

        String url = this.Url("/order");
        return RestResponseBo.ok("请求成功", url, data);

    }
    //endregion

}