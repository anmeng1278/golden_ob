package com.jsj.member.ob.interceptor;

import com.jsj.member.ob.config.Webconfig;
import com.jsj.member.ob.dto.http.UserSession;
import com.jsj.member.ob.dto.thirdParty.GetAccessTokenRequ;
import com.jsj.member.ob.dto.thirdParty.GetAccessTokenResp;
import com.jsj.member.ob.logic.ThirdPartyLogic;
import com.jsj.member.ob.logic.WechatLogic;
import com.jsj.member.ob.utils.SpringContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import weixin.popular.api.SnsAPI;
import weixin.popular.api.UserAPI;
import weixin.popular.bean.sns.SnsToken;
import weixin.popular.bean.user.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 微信授权拦截
 */
@Component
public class WxInterceptor extends HandlerInterceptorAdapter {


    @Autowired
    Webconfig webconfig;

    private String getFullURL(HttpServletRequest request) {

        StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
        String queryString = request.getQueryString();

        if (queryString == null) {
            return requestURL.toString();
        } else {
            return requestURL.append('?').append(queryString).toString();
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (SpringContextUtils.getActiveProfile().equals("dev")) {

            UserSession wxUser = new UserSession();

            wxUser.setOpenid("oeQDZt-rcgi9QhWm6F7o2mV3dSYY");
            wxUser.setNickname("测试账户");
            wxUser.setSubscribe(1);

            request.getSession().setAttribute("wx", wxUser);

        }

        //当前用户是否登录
        if (request.getSession().getAttribute("wx") == null) {

            //跳转微信链接地址
            if (request.getParameter("code") == null || request.getParameter("code").isEmpty()) {

                String lastAccessUrl = getFullURL(request);
                String url = SnsAPI.connectOauth2Authorize(webconfig.getAppId(), lastAccessUrl, true, null);
                response.sendRedirect(url);

                return false;

            } else {

                int jsjId = 0;
                if (!StringUtils.isEmpty(request.getParameter("jsjid"))) {
                    jsjId = Integer.parseInt(request.getParameter("jsjid"));
                }

                //获取用户信息
                String code = request.getParameter("code");
                SnsToken snsToken = SnsAPI.oauth2AccessToken(webconfig.getAppId(), webconfig.getAppSecrect(), code);

                if (!StringUtils.isEmpty(snsToken.getErrcode())) {
                    //授权失败
                    response.getWriter().write(snsToken.getErrmsg());
                    return false;
                }

                User wxUser = SnsAPI.userinfo(snsToken.getAccess_token(), snsToken.getOpenid(), "UTF8", 1);
                if (!StringUtils.isEmpty(wxUser.getErrcode())) {
                    response.getWriter().write(wxUser.getErrmsg());
                    return false;
                }
                //{"openid":"o2JcesxmAIQWeqEEqA-vM-i44Miw","nickname":"张宁","sex":1,"language":"zh_CN","city":"Chaoyang","province":"Beijing","country":"China","headimgurl":"http:\/\/thirdwx.qlogo.cn\/mmopen\/vi_32\/DYAIOgq83erTkr9EdnUkzMDx5XM2slCL16ibrG6T1aibGlKZjicZFt16oVibkHxoA4Fft6akHQ10X62dzfBF3ojctw\/132","privilege":[]}

                GetAccessTokenResp getAccessTokenResp;
                if (SpringContextUtils.getActiveProfile().equals("dev")) {
                    getAccessTokenResp = ThirdPartyLogic.GetAccessTokenDev(null);
                } else {
                    getAccessTokenResp = ThirdPartyLogic.GetAccessToken(new GetAccessTokenRequ());
                }

                User user = UserAPI.userInfo(getAccessTokenResp.getResponseBody().getAccessToken(), snsToken.getOpenid());
                if (!StringUtils.isEmpty(user.getErrcode())) {
                    response.getWriter().write(user.getErrmsg());
                    return false;
                }

                if (user.getSubscribe() != null) {
                    wxUser.setSubscribe(user.getSubscribe().intValue());
                } else {
                    wxUser.setSubscribe(0);
                }
                if (user.getSubscribe_time() != null) {
                    wxUser.setSubscribe_time(user.getSubscribe_time().intValue());
                } else {
                    wxUser.setSubscribe_time(0);
                }

                //{"subscribe":1,"openid":"o2JcesxmAIQWeqEEqA-vM-i44Miw","nickname":"张宁","sex":1,"language":"zh_CN","city":"朝阳","province":"北京","country":"中国","headimgurl":"http:\/\/thirdwx.qlogo.cn\/mmopen\/PiajxSqBRaEKFLsWN4XS5v1yCavjGU69d4MhTotaNU1oe0C5w9cdHTt2J1x3VTeEnDcfT4B3b5ml3ekmlcJHrNA\/132","subscribe_time":1538198776,"remark":"","groupid":0,"tagid_list":[],"subscribe_scene":"ADD_SCENE_SEARCH","qr_scene":0,"qr_scene_str":""}
                WechatLogic.Init(wxUser,jsjId);

                UserSession wx = UserSession.Init(wxUser);
                request.getSession().setAttribute("wx", wx);

            }

        }

        UserSession wx = (UserSession) request.getSession().getAttribute("wx");
        request.setAttribute("wx", wx);

        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {

        /*
        GetAccessTokenResp getAccessTokenResp = null;

        if (SpringContextUtils.getActiveProfile().equals("dev")) {
            getAccessTokenResp = ThirdPartyLogic.GetAccessTokenDev(null);
        } else {
            getAccessTokenResp = ThirdPartyLogic.GetAccessToken(new GetAccessTokenRequ());
        }

        String timestamp = DateUtils.getCurrentUnixTime() + "";
        String noncestr = UUID.randomUUID().toString();
        String url = this.getFullURL(request);
        String signature = "";

        Ticket ticket = TicketAPI.ticketGetticket(getAccessTokenResp.getResponseBody().getAccessToken());
        if (!StringUtils.isEmpty(ticket.getErrcode())) {
            String ticket1 = ticket.getTicket();
            signature = JsUtil.generateConfigSignature(noncestr, ticket1, timestamp, url);
        }

        request.setAttribute("signature", signature);
        request.setAttribute("appId", webconfig.getAppId());
        request.setAttribute("timestamp", timestamp);
        request.setAttribute("noncestr", noncestr);
             */
        request.setAttribute("virtualPath", webconfig.getVirtualPath());


    }
}
