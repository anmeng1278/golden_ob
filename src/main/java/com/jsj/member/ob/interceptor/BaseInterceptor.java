package com.jsj.member.ob.interceptor;

import com.jsj.member.ob.logic.*;
import com.jsj.member.ob.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 基础拦截器
 */
@Component
public class BaseInterceptor implements HandlerInterceptor {

    private static final Logger LOGGE = LoggerFactory.getLogger(BaseInterceptor.class);
    private static final String USER_AGENT = "user-agent";

    @Autowired
    DateUtils dateUtils;

    @Autowired
    ProductLogic productLogic;

    @Autowired
    DictLogic dictLogic;

    @Autowired
    EnumLogic enumLogic;

    @Autowired
    OrderLogic orderLogic;

    @Autowired
    WechatLogic wechatLogic;

    @Autowired
    ActivityLogic activityLogic;

    @Autowired
    StockLogic stockLogic;

    @Autowired
    GiftLogic giftLogic;

    @Autowired
    CouponLogic couponLogic;

    @Autowired
    DeliveryLogic deliveryLogic;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
                           ModelAndView modelAndView) throws Exception {
        httpServletRequest.setAttribute("dateUtils", dateUtils);
        httpServletRequest.setAttribute("productLogic", productLogic);
        httpServletRequest.setAttribute("dictLogic", dictLogic);
        httpServletRequest.setAttribute("enumLogic", enumLogic);

        httpServletRequest.setAttribute("orderLogic", orderLogic);
        httpServletRequest.setAttribute("wechatLogic", wechatLogic);
        httpServletRequest.setAttribute("activityLogic", activityLogic);
        httpServletRequest.setAttribute("stockLogic", stockLogic);
        httpServletRequest.setAttribute("giftLogic", giftLogic);
        httpServletRequest.setAttribute("couponLogic", couponLogic);
        httpServletRequest.setAttribute("deliveryLogic", deliveryLogic);
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {

    }

}
