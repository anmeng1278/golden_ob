package com.jsj.member.ob.controller.admin;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.dto.RestResponseBo;
import com.jsj.member.ob.entity.Admin;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.ActivityLogic;
import com.jsj.member.ob.logic.DeliveryLogic;
import com.jsj.member.ob.logic.ProductLogic;
import com.jsj.member.ob.logic.WechatLogic;
import com.jsj.member.ob.redis.AccessKey;
import com.jsj.member.ob.redis.RedisService;
import com.jsj.member.ob.service.*;
import com.jsj.member.ob.utils.Md5Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Properties;

@ApiIgnore
@Controller
@RequestMapping("/admin")
public class AdminController {


    @Autowired
    ActivityService activityService;

    @Autowired
    ProductService productService;

    @Autowired
    DeliveryService deliveryService;

    @Autowired
    AdminService adminService;

    @Autowired
    WechatService wechatService;

    @GetMapping(value = {""})
    public String index(ModelMap map) {

        return "admin/index";
    }


    /**
     * 登录首页
     *
     * @return
     */
    @GetMapping(value = {"/login"})
    public String login() {
        return "admin/login";
    }


    /**
     * 登录系统
     *
     * @param username
     * @param password
     * @param request
     * @return
     */
    @PostMapping(value = "/login")
    @ResponseBody
    public RestResponseBo doLogin(@RequestParam String username, @RequestParam String password,
                                  HttpServletRequest request) {

        if (StringUtils.isBlank(username)) {
            throw new TipException("登录名不能为空");
        }
        if (StringUtils.isBlank(password)) {
            throw new TipException("登录密码不能为空");
        }
        password = Md5Utils.MD5(password);

        Wrapper wrapper = new EntityWrapper<Admin>();
        wrapper.where("login_name={0} and password={1}", username, password);

        Admin admin = adminService.selectOne(wrapper);

        if (admin == null) {
            throw new TipException("用户名或密码错误");
        }

        if (!admin.getIfpass()) {
            throw new TipException("当前账户已被禁止登录");
        }

        request.getSession().setAttribute(Constant.LOGIN_SESSION_ADMIN_KEY, admin);
        return RestResponseBo.ok("登录成功", "/admin");

    }

    /**
     * 注销
     *
     * @param session
     * @param response
     */
    @RequestMapping("/logout")
    public void logout(HttpSession session, HttpServletResponse response, HttpServletRequest request) throws IOException {
        session.removeAttribute(Constant.LOGIN_SESSION_ADMIN_KEY);
        response.sendRedirect("/admin/login");
    }

    /**
     * 系统主页
     *
     * @param request
     * @param map
     * @return
     */
    @GetMapping(value = {"/main"})
    public String main(HttpServletRequest request, ModelMap map) {

        String domain = request.getScheme() + "://" + request.getServerName();
        request.setAttribute("domain", domain);

        String root = request.getServletContext().getRealPath("/");
        request.setAttribute("root", root);

        Properties props = System.getProperties(); // 系统属性

        //待审活动
        int activityCount = ActivityLogic.GetUnPassActivity();

        //待审商品
        int productCount = ProductLogic.GetUnPassProduct();

        //等待发货
        int deliveryCount = DeliveryLogic.GetUnDeliveryCount();

        //新增用户
        int wechatCount = WechatLogic.GetNewUsers();


        request.setAttribute("os", props.getProperty("os.name"));
        request.setAttribute("version", props.getProperty("java.version"));

        request.setAttribute("port", request.getLocalPort());
        request.setAttribute("ip", request.getLocalAddr());

        request.setAttribute("activityCount", activityCount);
        request.setAttribute("productCount", productCount);
        request.setAttribute("deliveryCount", deliveryCount);
        request.setAttribute("wechatCount", wechatCount);

        return "admin/main";

    }


    @Autowired
    RedisService redisService;

    /**
     * 更新缓存
     *
     * @return
     */
    @PostMapping(value = "/updateCache")
    @ResponseBody
    public RestResponseBo updateCache(HttpServletRequest request) {

        String method = request.getParameter("method");

        switch (method) {
            case "index":
                redisService.delete(AccessKey.pageIndex);
                break;
            case "exchange":
                redisService.delete(AccessKey.pageExchange);
        }

        return RestResponseBo.ok("更新成功");

    }
}
