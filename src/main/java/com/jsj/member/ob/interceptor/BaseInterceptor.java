package com.jsj.member.ob.interceptor;

import com.jsj.member.ob.logic.DictLogic;
import com.jsj.member.ob.logic.ProductLogic;
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
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {

    }

}
