package com.jsj.member.ob.controller.api;

import com.jsj.member.ob.controller.BaseController;
import com.jsj.member.ob.dto.api.Request;
import com.jsj.member.ob.dto.api.Response;
import com.jsj.member.ob.dto.api.coupon.WechatCouponDto;
import com.jsj.member.ob.dto.mini.CouponsRequ;
import com.jsj.member.ob.dto.mini.CouponsResp;
import com.jsj.member.ob.logic.CouponLogic;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("${webconfig.virtualPath}/mini")
public class ApiCouponController extends BaseController {


    //region (public) 优惠券列表 coupons

    /**
     * 优惠券列表
     *
     * @param requ
     * @return
     */
    @ApiOperation(value = "优惠券列表")
    @RequestMapping(value = "/coupons", method = RequestMethod.POST)
    public Response<CouponsResp> coupons(@ApiParam(value = "请求实体", required = true)
                                         @RequestBody
                                         @Validated Request<CouponsRequ> requ) {

        CouponsResp resp = new CouponsResp();

        List<WechatCouponDto> wechatCouponDtos;
        if (requ.getRequestBody().getProductIds() == null || requ.getRequestBody().getProductIds().isEmpty()) {
            wechatCouponDtos = CouponLogic.GetWechatCoupons(requ.getRequestBody().getUnionId());
        } else {
            wechatCouponDtos = CouponLogic.GetWechatCoupons(requ.getRequestBody().getProductIds(), requ.getRequestBody().getUnionId());
        }
        resp.setWechatCouponDtos(wechatCouponDtos);

        return Response.ok(resp);

    }
    //endregion


}