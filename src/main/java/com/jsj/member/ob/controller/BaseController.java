package com.jsj.member.ob.controller;

import com.jsj.member.ob.config.Webconfig;
import com.jsj.member.ob.dto.api.order.OrderDto;
import com.jsj.member.ob.dto.api.pay.PayDto;
import com.jsj.member.ob.dto.http.UserSession;
import com.jsj.member.ob.dto.thirdParty.GetPayTradeRequ;
import com.jsj.member.ob.dto.thirdParty.GetPayTradeResp;
import com.jsj.member.ob.entity.WechatRelation;
import com.jsj.member.ob.enums.OrderStatus;
import com.jsj.member.ob.enums.SourceType;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.*;
import com.jsj.member.ob.redis.AccessKey;
import com.jsj.member.ob.redis.RedisService;
import com.jsj.member.ob.tuple.TwoTuple;
import com.jsj.member.ob.utils.DateUtils;
import com.jsj.member.ob.utils.TupleUtils;
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
    public UserSession User() {

        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();

        Object wx = request.getSession().getAttribute("wx");
        return (UserSession) wx;

    }

    @Autowired
    protected Webconfig webconfig;

    /**
     * 跳转实际路径
     *
     * @param path
     * @return
     */
    protected String Redirect(String path) {
        return Redirect(path, true);
    }


    /**
     * 跳转实际路径
     *
     * @param path
     * @return
     */
    protected String Redirect(String path, boolean enabledCache) {
        int timeStamp = DateUtils.getCurrentUnixTime();
        if (enabledCache) {
            return String.format("redirect:%s%s", webconfig.getVirtualPath(), path);
        }
        if (path.indexOf("?") > -1) {
            return String.format("redirect:%s%s&%d", webconfig.getVirtualPath(), path, timeStamp);
        }
        return String.format("redirect:%s%s?%d", webconfig.getVirtualPath(), path, timeStamp);
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
    public TwoTuple<GetPayTradeResp, SourceType> createPay(int orderId) {

        OrderDto orderDto = OrderLogic.GetOrder(orderId);
        PayDto payDto = this.GetPayDto(orderDto.getSourceType());

        //if (!orderDto.getOpenId().equals(payDto.getOpenId())) {
        //    throw new TipException("非操作人订单不允许支付");
        //}
        if (orderDto.getPayAmount() <= 0) {
            throw new TipException("当前订单不需要支付");
        }
        if (!orderDto.getStatus().equals(OrderStatus.UNPAY) && !orderDto.getStatus().equals(OrderStatus.PAYFAIL)) {
            throw new TipException("当前订单状态不允许支付");
        }

        GetPayTradeRequ requ = new GetPayTradeRequ();

        requ.getRequestBody().setOutTradeId(orderDto.getOrderId() + "");
        requ.getRequestBody().setPayAmount(orderDto.getPayAmount() + "");
        requ.getRequestBody().setOpenId(payDto.getOpenId());
        requ.getRequestBody().setOrderTimeOut(DateUtils.formatDateByUnixTime(Long.parseLong(orderDto.getExpiredTime() + ""), "yyyyMMddHHmmss"));

        requ.getRequestBody().setPlatformAppId(payDto.getPlatformAppId());
        requ.getRequestBody().setPlatformToken(payDto.getPlatformToken());

        GetPayTradeResp resp = ThirdPartyLogic.GetPayTrade(requ);

        if (!resp.getResponseHead().getCode().equals("0000")) {
            throw new TipException(resp.getResponseHead().getMessage());
        }

        return TupleUtils.tuple(resp, orderDto.getSourceType());

    }

    //region (public) 获取支付实体 GetPayDto

    /**
     * 获取支付实体
     * 必须在登录完成后调用
     *
     * @param request
     * @return
     */
    public PayDto GetPayDto(HttpServletRequest request) {

        SourceType sourceType = this.GetSourceType(request);
        return this.GetPayDto(sourceType);

    }


    public PayDto GetPayDto(SourceType sourceType) {

        PayDto payDto = new PayDto();

        //初始化默认值
        payDto.setOpenId(this.OpenId());

        switch (sourceType) {
            case AWKTC:
                payDto.setOpenId(this.OpenId());
                break;
            default:
                WechatRelation wechatRelation = WechatLogic.GetWechatRelation(this.OpenId(), sourceType);
                if (wechatRelation == null) {
                    throw new TipException("未获取用户信息，暂无法支付");
                }
                payDto.setOpenId(wechatRelation.getRelationOpenId());
                break;
        }

        payDto.setPlatformAppId(ConfigLogic.GetPlatformAppId(sourceType));
        payDto.setPlatformToken(ConfigLogic.GetPlatformToken(sourceType));

        return payDto;

    }

    //endregion

    //region (public) 获取请求来源 GetSourceType

    /**
     * 获取请求来源
     *
     * @param request
     * @return
     */
    public SourceType GetSourceType(HttpServletRequest request) {

        String userAgent = request.getHeader("User-Agent");
        if (!org.apache.commons.lang3.StringUtils.isEmpty(userAgent)) {
            userAgent = userAgent.toLowerCase();
            if (userAgent.indexOf("miniprogram") > -1) {
                return SourceType.AWKMINI;
            }
        }

        return SourceType.AWKTC;
    }
    //endregion
}
