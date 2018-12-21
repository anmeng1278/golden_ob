package com.jsj.member.ob.interceptor;

import com.jsj.member.ob.config.Webconfig;
import com.jsj.member.ob.logic.BaseLogic;
import com.jsj.member.ob.utils.DateUtils;
import com.jsj.member.ob.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 基础拦截器
 */
@Component
public class BaseInterceptor implements HandlerInterceptor {

    private static final Logger LOGGE = LoggerFactory.getLogger(BaseInterceptor.class);
    private static final String USER_AGENT = "user-agent";

    @Autowired
    List<BaseLogic> baseLogics;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
                           ModelAndView modelAndView) throws Exception {
        for (BaseLogic bl : baseLogics) {
            httpServletRequest.setAttribute(StringUtils.camelCase(bl.getClass().getSimpleName()), bl);
        }
        httpServletRequest.setAttribute("ob", this);
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {

    }


    @Autowired
    Webconfig webconfig;

    /**
     * 获取资源文件路径
     *
     * @param url
     * @param enabledCache
     * @return
     */
    public String Url(String url, boolean enabledCache) {
        String format = "%s%s";
        url = String.format(format, webconfig.getVirtualPath(), url);
        if (enabledCache) {
            int timeStamp = DateUtils.getCurrentUnixTime();
            if (url.indexOf("?") > -1) {
                url = String.format("%s&%d", url, timeStamp);
            } else {
                url = String.format("%s?%d", url, timeStamp);
            }
        }
        return url;
    }

    /**
     * 获取资源文件路径
     *
     * @param url
     * @return
     */
    public String Url(String url) {
        return Url(url, true);
    }

    /**
     * 获取图片路径
     *
     * @param url
     * @return
     */
    public String Img(String url) {
        return Url(url, false);
    }

    /**
     * 获取跳转链接
     *
     * @param url
     * @return
     */
    public String Nav(String url) {
        String format = "%s%s";
        url = String.format(format, webconfig.getVirtualPath(), url);
        return url;
    }


}
