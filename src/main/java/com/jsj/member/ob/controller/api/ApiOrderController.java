package com.jsj.member.ob.controller.api;

import com.jsj.member.ob.controller.BaseController;
import com.jsj.member.ob.dto.api.Request;
import com.jsj.member.ob.dto.api.Response;
import com.jsj.member.ob.dto.api.order.OrderDto;
import com.jsj.member.ob.dto.api.order.UserOrderDto;
import com.jsj.member.ob.dto.api.pay.PayDto;
import com.jsj.member.ob.dto.mini.*;
import com.jsj.member.ob.dto.thirdParty.GetPayTradeRequ;
import com.jsj.member.ob.dto.thirdParty.GetPayTradeResp;
import com.jsj.member.ob.enums.OrderFlag;
import com.jsj.member.ob.enums.OrderStatus;
import com.jsj.member.ob.enums.SourceType;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.OrderLogic;
import com.jsj.member.ob.logic.ThirdPartyLogic;
import com.jsj.member.ob.tuple.TwoTuple;
import com.jsj.member.ob.utils.DateUtils;
import com.jsj.member.ob.utils.TupleUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${webconfig.virtualPath}/mini")
public class ApiOrderController extends BaseController {

    //region (public) 订单列表 index

    /**
     * 订单列表
     *
     * @param requ
     * @return
     */
    @ApiOperation(value = "订单列表")
    @RequestMapping(value = "/orders", method = RequestMethod.POST)
    public Response<OrdersResp> orders(@ApiParam(value = "请求实体", required = true) @RequestBody @Validated Request<OrdersRequ> requ) {

        OrdersResp resp = new OrdersResp();

        OrderFlag orderFlag = OrderFlag.ALLORDERS;
        if (!StringUtils.isEmpty(requ.getRequestBody().getOrderFlag())) {
            orderFlag = OrderFlag.valueOf(Integer.parseInt(requ.getRequestBody().getOrderFlag()));
        }

        String unionId = requ.getRequestBody().getUnionId();
        List<UserOrderDto> orderDtos = OrderLogic.GetOrders(unionId, orderFlag);

        resp.setOrderDtos(orderDtos);
        resp.setOrderFlag(requ.getRequestBody().getOrderFlag());

        return Response.ok(resp);

    }
    //endregion

    //region (public) 取消订单 cancel

    /**
     * 取消订单
     *
     * @param requ
     * @return
     */
    @ApiOperation(value = "取消订单")
    @RequestMapping(value = "/cancelOrder", method = RequestMethod.POST)
    public Response<CancelOrderResp> cancelOrder(@ApiParam(value = "请求实体", required = true)
                                                 @RequestBody @Validated Request<CancelOrderRequ> requ) {

        int orderId = requ.getRequestBody().getOrderId();
        OrderLogic.CancelOrder(orderId);

        CancelOrderResp resp = new CancelOrderResp();
        resp.setOrderId(orderId);

        return Response.ok(resp);
    }
    //endregion

    //region (public) 创建支付请求 createPay

    /**
     * 创建支付请求
     *
     * @param requ
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "创建支付请求")
    @RequestMapping(value = "/createPay", method = RequestMethod.POST)
    public Response<CreatePayResp> createPay(@ApiParam(value = "请求实体", required = true)
                                             @RequestBody @Validated Request<CreatePayRequ> requ) {

        CreatePayResp resp = new CreatePayResp();

        int orderId = requ.getRequestBody().getOrderId();
        TwoTuple<GetPayTradeResp, SourceType> twoTuple = this.createPay(orderId);

        OrderDto orderDto = OrderLogic.GetOrder(orderId);

        if (!twoTuple.first.getResponseHead().getCode().equals("0000")) {
            throw new TipException(twoTuple.first.getResponseHead().getMessage());
        }
        resp.setPay(twoTuple.first);
        resp.setOrderDto(orderDto);

        return Response.ok(resp);

    }
    //endregion

    //region (public) 创建微信支付订单 createPay

    /**
     * 创建微信支付订单
     *
     * @param orderId
     * @return
     */
    public TwoTuple<GetPayTradeResp, SourceType> createPay(int orderId) {

        OrderDto orderDto = OrderLogic.GetOrder(orderId);
        //PayDto payDto = this.GetPayDto(orderDto.getSourceType());

        PayDto payDto = this.GetPayDto(SourceType.AWKMINI);

        //if (!orderDto.getOpenId().equals(payDto.getOpenId())) {
        //    throw new TipException("非操作人订单不允许支付");
        //}
        if (orderDto.getPayAmount() <= 0) {
            throw new TipException("当前订单不需要支付");
        }
        if (!orderDto.getStatus().equals(OrderStatus.UNPAY) && !orderDto.getStatus().equals(OrderStatus.PAYFAIL)) {
            throw new TipException("当前订单状态不允许支付");
        }

        GetPayTradeRequ requ = new GetPayTradeRequ();

        requ.getRequestBody().setOutTradeId(orderDto.getOrderId() + "");
        requ.getRequestBody().setPayAmount(orderDto.getPayAmount() + "");
        requ.getRequestBody().setOpenId(payDto.getOpenId());
        requ.getRequestBody().setOrderTimeOut(DateUtils.formatDateByUnixTime(Long.parseLong(orderDto.getExpiredTime() + ""), "yyyyMMddHHmmss"));

        requ.getRequestBody().setPlatformAppId(payDto.getPlatformAppId());
        requ.getRequestBody().setPlatformToken(payDto.getPlatformToken());

        GetPayTradeResp resp = ThirdPartyLogic.GetPayTrade(requ);

        if (!resp.getResponseHead().getCode().equals("0000")) {
            throw new TipException(resp.getResponseHead().getMessage());
        }

        return TupleUtils.tuple(resp, orderDto.getSourceType());

    }
    //endregion

}
