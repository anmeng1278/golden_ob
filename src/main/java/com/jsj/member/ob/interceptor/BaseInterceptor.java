package com.jsj.member.ob.interceptor;

import com.jsj.member.ob.config.Webconfig;
import com.jsj.member.ob.logic.BaseLogic;
import com.jsj.member.ob.utils.DateUtils;
import com.jsj.member.ob.utils.StringUtils;
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


    @Autowired
    List<BaseLogic> baseLogics;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

        for (BaseLogic bl : baseLogics) {
            request.setAttribute(StringUtils.camelCase(bl.getClass().getSimpleName()), bl);
        }
        request.setAttribute("ob", this);

        if (request.getAttribute("mini") == null) {
            //获取浏览器版本
            String userAgent = request.getHeader("User-Agent");
            Boolean mini = false;
            if (!org.apache.commons.lang3.StringUtils.isEmpty(userAgent)) {
                userAgent = userAgent.toLowerCase();
                mini = userAgent.indexOf("miniprogram") > -1;
            }
            request.setAttribute("mini", mini);
        }

        request.setAttribute("virtualPath", webconfig.getVirtualPath());
        request.setAttribute("host", webconfig.getHost());

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse httpServletResponse, Object o,
                           ModelAndView modelAndView) throws Exception {


    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {

    }


    @Autowired
    Webconfig webconfig;

    //region (public) 获取资源文件路径 Url

    /**
     * 获取资源文件路径
     *
     * @param url
     * @param enabledCache
     * @return
     */
    public String Url(String url, boolean enabledCache) {
        String format = "%s/static/%s";
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
    //endregion

    //region (public) 获取资源文件路径 Url

    /**
     * 获取资源文件路径
     *
     * @param url
     * @return
     */
    public String Url(String url) {
        return Url(url, true);
    }
    //endregion

    //region (public) 获取图片路径 Img

    /**
     * 获取图片路径
     *
     * @param url
     * @return
     */
    public String Img(String url) {
        return Url(url, false);
    }
    //endregion

    //region (public) 获取跳转链接 Nav

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
    //endregion


}
