package com.jsj.member.ob.controller;

import com.jsj.member.ob.config.Webconfig;
import com.jsj.member.ob.dto.api.order.OrderDto;
import com.jsj.member.ob.dto.thirdParty.GetPayTradeRequ;
import com.jsj.member.ob.dto.thirdParty.GetPayTradeResp;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.BaseLogic;
import com.jsj.member.ob.logic.OrderLogic;
import com.jsj.member.ob.logic.ThirdPartyLogic;
import com.jsj.member.ob.redis.AccessKey;
import com.jsj.member.ob.redis.RedisService;
import com.jsj.member.ob.utils.DateUtils;
import com.jsj.member.ob.utils.SpringContextUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import springfox.documentation.annotations.ApiIgnore;
import weixin.popular.bean.user.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@ApiIgnore
@Controller
public abstract class BaseController {

    @Autowired
    List<BaseLogic> baseLogics;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    RedisService redisService;

    /**
     * 设置页面缓存
     *
     * @param keys
     * @param request
     * @param response
     * @param model
     * @param expireSeconds
     * @return
     */
    public String SetAccessCache(
            String keys,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model,
            int expireSeconds
    ) {

        for (BaseLogic bl : baseLogics) {
            String logicName = com.jsj.member.ob.utils.StringUtils.camelCase(bl.getClass().getSimpleName());
            model.addAttribute(logicName, bl);
        }
        SpringWebContext ctx = new SpringWebContext(request, response,
                request.getServletContext(), request.getLocale(), model.asMap(), applicationContext);
        String html = thymeleafViewResolver.getTemplateEngine().process("admin/wechat/index", ctx);

        if (!StringUtils.isBlank(html)) {
            redisService.set(AccessKey.withExpire(expireSeconds), keys, html);
        }

        return html;
    }

    /**
     * 获取页面缓存
     *
     * @param keys
     * @return
     */
    public String GetAccessCache(String keys) {
        String s = redisService.get(AccessKey.withExpire(0), keys, String.class);
        return s;
    }

    /**
     * 登录用户OpenId
     *
     * @return
     */
    public String OpenId() {
        return this.User().getOpenid();
    }

    /**
     * 登录用户
     *
     * @return
     */
    public User User() {

        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();

        Object wx = request.getSession().getAttribute("wx");
        if (wx != null) {
            return (User) wx;
        } else {
            if (SpringContextUtils.getActiveProfile().equals("dev")) {
                User u = new User();
                u.setOpenid("oeQDZtx7YcrYSkKAO7YOrl1CN_aI");
                u.setNickname("测试账户");
                return u;
            } else {
                throw new NotImplementedException("未获取到登录用户信息");
            }
        }
    }

    @Autowired
    Webconfig webconfig;

    /**
     * 跳转实际路径
     *
     * @param path
     * @return
     */
    protected String Redirect(String path) {
        return String.format("redirect:%s%s", webconfig.getVirtualPath(), path);
    }

    /**
     * 获取资源文件路径
     *
     * @param url
     * @param enabledCache
     * @return
     */
    protected String Url(String url, boolean enabledCache) {
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
    protected String Url(String url) {
        return Url(url, true);
    }

    /**
     * 获取图片路径
     *
     * @param url
     * @return
     */
    protected String Img(String url) {
        return Url(url, false);
    }

    /**
     * 获取跳转链接
     *
     * @param url
     * @return
     */
    protected String Nav(String url) {
        return Url(url, false);
    }

    /**
     * 创建微信支付订单
     *
     * @param orderId
     * @return
     */
    public GetPayTradeResp createPay(int orderId) {

        String openId = this.OpenId();
        OrderDto orderDto = OrderLogic.GetOrder(orderId);

        if (!orderDto.getOpenId().equals(openId)) {
            throw new TipException("非操作人订单不允许支付");
        }

        GetPayTradeRequ requ = new GetPayTradeRequ();

        requ.getRequestBody().setOutTradeId(orderDto.getOrderId() + "");
        requ.getRequestBody().setPayAmount(orderDto.getPayAmount() + "");
        requ.getRequestBody().setOpenId(openId);
        requ.getRequestBody().setOrderTimeOut(DateUtils.formatDateByUnixTime(Long.parseLong(orderDto.getExpiredTime() + ""), "yyyyMMddHHmmss"));

        GetPayTradeResp resp = ThirdPartyLogic.GetPayTrade(requ);

        if (!resp.getResponseHead().getCode().equals("0000")) {
            throw new TipException(resp.getResponseHead().getMessage());
        }

        return resp;

    }


}
