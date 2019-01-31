package com.jsj.member.ob.controller.api;

import com.jsj.member.ob.dto.api.Request;
import com.jsj.member.ob.dto.api.Response;
import com.jsj.member.ob.dto.api.order.CreateOrderRequ;
import com.jsj.member.ob.dto.api.order.CreateOrderResp;
import com.jsj.member.ob.logic.order.OrderBase;
import com.jsj.member.ob.logic.order.OrderFactory;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${webconfig.virtualPath}/mini")
public class ApiProductController {


    /**
     * 创建订单
     *
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "创建订单")
    @RequestMapping(value = "/createOrder", method = RequestMethod.POST)
    public Response<CreateOrderResp> createOrder(@ApiParam(value = "请求实体", required = true)
                                              @RequestBody @Validated Request<CreateOrderRequ> requ) throws Exception {

        OrderBase orderBase = OrderFactory.GetInstance(requ.getRequestBody().getActivityType());
        CreateOrderResp resp = orderBase.CalculateOrder(requ.getRequestBody());

        return Response.ok(resp);

    }
}
