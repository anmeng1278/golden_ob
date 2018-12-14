package com.jsj.member.ob.controller.index;

import com.jsj.member.ob.controller.BaseController;
import com.jsj.member.ob.dto.api.coupon.WechatCouponDto;
import com.jsj.member.ob.dto.api.gift.GiftDto;
import com.jsj.member.ob.dto.api.stock.StockDto;
import com.jsj.member.ob.logic.CouponLogic;
import com.jsj.member.ob.logic.GiftLogic;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;

@ApiIgnore
@Controller
@RequestMapping("${webconfig.virtualPath}/coupon")
public class CouponController extends BaseController {


    /**
     * 优惠券
     *
     * @param request
     * @return
     */
    @GetMapping("")
    public String index(HttpServletRequest request) {

        String openId = this.OpenId();

        List<WechatCouponDto> wechatCouponDtos = CouponLogic.GetWechatCoupons(openId);

        request.setAttribute("wechatCouponDtos",wechatCouponDtos);

        return "index/coupon";
    }



}