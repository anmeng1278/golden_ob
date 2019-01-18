package com.jsj.member.ob.controller.common;

import com.jsj.member.ob.config.Webconfig;
import com.jsj.member.ob.controller.BaseController;
import com.jsj.member.ob.dto.RestResponseBo;
import com.jsj.member.ob.dto.thirdParty.GetAccessTokenRequ;
import com.jsj.member.ob.dto.thirdParty.GetAccessTokenResp;
import com.jsj.member.ob.logic.ThirdPartyLogic;
import com.jsj.member.ob.redis.AccessKey;
import com.jsj.member.ob.utils.DateUtils;
import com.jsj.member.ob.utils.HttpUtils;
import com.jsj.member.ob.utils.SpringContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;
import weixin.popular.api.TicketAPI;
import weixin.popular.bean.ticket.Ticket;
import weixin.popular.util.JsUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

@ApiIgnore
@Controller
@RequestMapping("${webconfig.virtualPath}/config")
public class ConfigController extends BaseController {


    @Autowired
    Webconfig webconfig;

    /**
     * 获取微信配置
     *
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/wechat")
    @ResponseBody
    public RestResponseBo config(HttpServletRequest request) {

        GetAccessTokenResp getAccessTokenResp = null;
        if (SpringContextUtils.getActiveProfile().equals("dev")) {
            getAccessTokenResp = ThirdPartyLogic.GetAccessTokenDev(null);
        } else {
            getAccessTokenResp = ThirdPartyLogic.GetAccessToken(new GetAccessTokenRequ());
        }

        String timestamp = DateUtils.getCurrentUnixTime() + "";
        String noncestr = UUID.randomUUID().toString();
        String url = request.getHeader("referer");
        String signature = "";

        Ticket ticket = TicketAPI.ticketGetticket(getAccessTokenResp.getResponseBody().getAccessToken());
        if (!StringUtils.isEmpty(ticket.getErrcode())) {
            String ticket1 = ticket.getTicket();
            signature = JsUtil.generateConfigSignature(noncestr, ticket1, timestamp, url);
        }

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("signature", signature);
        hashMap.put("appId", webconfig.getAppId());
        hashMap.put("timestamp", timestamp);
        hashMap.put("noncestr", noncestr);
        hashMap.put("virtualPath", webconfig.getVirtualPath());
        hashMap.put("url", url);

        return RestResponseBo.ok(hashMap);

    }

    /**
     * 获取微信js
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/jweixin")
    @ResponseBody
    public String jweixin(HttpServletRequest request) throws IOException {

        String userAgent = request.getHeader("User-Agent");
        Boolean mini = false;
        if (!org.apache.commons.lang3.StringUtils.isEmpty(userAgent)) {
            userAgent = userAgent.toLowerCase();
            mini = userAgent.indexOf("miniprogram") > -1;
        }

        String url = "";
        if (mini) {
            url = "https://res.wx.qq.com/open/js/jweixin-1.4.0.js";
        } else {
            url = "https://res.wx.qq.com/open/js/jweixin-1.0.0.js";
        }

        AccessKey accessKey = new AccessKey(60 * 60 * 24 * 30, "jWeixin" + mini);
        String js = this.GetAccessCache(accessKey);
        if (!StringUtils.isEmpty(js)) {
            return js;
        }

        js = HttpUtils.get(url);
        this.SetAccessCache(accessKey, js);
        return js;
    }
}


