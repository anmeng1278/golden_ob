package com.jsj.member.ob.controller;

import com.jsj.member.ob.logic.BaseLogic;
import com.jsj.member.ob.redis.AccessKey;
import com.jsj.member.ob.redis.RedisService;
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


}
