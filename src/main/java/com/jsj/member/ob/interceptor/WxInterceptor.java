package com.jsj.member.ob.interceptor;

import com.jsj.member.ob.config.Webconfig;
import com.jsj.member.ob.dto.http.UserSession;
import com.jsj.member.ob.dto.thirdParty.GetAccessTokenRequ;
import com.jsj.member.ob.dto.thirdParty.GetAccessTokenResp;
import com.jsj.member.ob.enums.SourceType;
import com.jsj.member.ob.logic.ThirdPartyLogic;
import com.jsj.member.ob.logic.WechatLogic;
import com.jsj.member.ob.utils.EncryptUtils;
import com.jsj.member.ob.utils.SpringContextUtils;
import jodd.util.URLDecoder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger logger = LoggerFactory.getLogger(WxInterceptor.class);

    @Autowired
    Webconfig webconfig;


    //region (private) 获取完整Url路径 getFullURL

    /**
     * 获取完整Url路径
     *
     * @param request
     * @return
     */
    private String getFullURL(HttpServletRequest request) {

        StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
        String queryString = request.getQueryString();

        String url = "";
        if (queryString == null) {
            url = requestURL.toString();
        } else {
            url = requestURL.append('?').append(queryString).toString();
        }
        return url;
        //if (url.indexOf("https") > -1) {
        //    return url;
        //}
        //return url.replaceAll("http", "https");
    }
    //endregion

    //region 拦截器，获取微信授权信息

    /**
     * 拦截器，获取微信授权信息
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (SpringContextUtils.getActiveProfile().equals("dev")) {

            UserSession wxUser = new UserSession();

            wxUser.setOpenid("oeQDZt-rcgi9QhWm6F7o2mV3dSYY");
            wxUser.setNickname("测试账户");
            wxUser.setSubscribe(1);
            wxUser.setJsjId(20612968);
            wxUser.setHeadimgurl("http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83epDoc6nkBKtPSZrP762lGiaTok7VtabocBcp0q0OUC1mmNq99ozG9GiaMib4DiauaI9w6u5w26CTgdeVg/132");

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

                //获取会员编号
                int jsjId = this.parseJsjId(request);


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
                //初始化会员
                WechatLogic.Init(wxUser, jsjId);

                //绑定会员关系
                this.bindWechatRelation(request, wxUser.getOpenid());

                UserSession wx = UserSession.Init(wxUser, jsjId);
                request.getSession().setAttribute("wx", wx);

            }

        }

        UserSession wx = (UserSession) request.getSession().getAttribute("wx");
        request.setAttribute("wx", wx);

        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {

    }
    //endregion

    //region (private) 解析会员编号 parseJsjId

    /**
     * 解析会员编号
     *
     * @param request
     * @return
     */
    private int parseJsjId(HttpServletRequest request) {
        int jsjId = 0;
        String key = request.getParameter("key");
        String url = this.getFullURL(request);
        try {
            if (!StringUtils.isEmpty(key)) {
                key = URLDecoder.decode(key, "UTF-8");
                jsjId = Integer.parseInt(EncryptUtils.decrypt2(key));
            }
            logger.info(String.format("解析正常：%s %d %s", key, jsjId, url));
        } catch (Exception ex) {
            logger.error(String.format("解析出错：%s %d %s", key, jsjId, url));
        }
        return jsjId;
    }
    //endregion

    //region (private) 绑定会员关系 bindWechatRelation

    private void bindWechatRelation(HttpServletRequest request, String openId) {

        String typeid = request.getParameter("typeid");
        String relationOpenId = request.getParameter("openid");

        if (StringUtils.isEmpty(typeid)) {
            return;
        }
        if (StringUtils.isEmpty(relationOpenId)) {
            return;
        }
        if (StringUtils.isEmpty(openId)) {
            return;
        }

        try {

            SourceType sourceType = SourceType.valueOf(Integer.parseInt(typeid));
            WechatLogic.BindRelation(openId, relationOpenId, sourceType);

            logger.info(String.format("绑定成功：%s %s %s", openId, relationOpenId, sourceType.getMessage()));

        } catch (Exception ex) {
            logger.error(String.format("绑定失败：%s %s %s", openId, relationOpenId, typeid));
        }

    }
    //endregion

}
