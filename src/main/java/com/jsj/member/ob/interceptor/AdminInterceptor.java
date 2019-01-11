package com.jsj.member.ob.interceptor;

import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.utils.SpringContextUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class AdminInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!SpringContextUtils.getActiveProfile().equals("dep")) {
            return true;
        }

        //当前用户是否登录
        if (request.getSession().getAttribute(Constant.LOGIN_SESSION_ADMIN_KEY) == null) {
            response.getWriter().write("<script>top.window.location.href='/admin/login';</script>");
            return false;
        }

        return true;
    }

}
