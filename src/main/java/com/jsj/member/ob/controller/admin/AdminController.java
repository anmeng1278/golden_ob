package com.jsj.member.ob.controller.admin;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jsj.member.ob.entity.Activity;
import com.jsj.member.ob.entity.Delivery;
import com.jsj.member.ob.entity.Product;
import com.jsj.member.ob.entity.Wechat;
import com.jsj.member.ob.enums.DeliveryStatus;
import com.jsj.member.ob.logic.ActivityLogic;
import com.jsj.member.ob.logic.DeliveryLogic;
import com.jsj.member.ob.logic.ProductLogic;
import com.jsj.member.ob.logic.WechatLogic;
import com.jsj.member.ob.service.ActivityService;
import com.jsj.member.ob.service.DeliveryService;
import com.jsj.member.ob.service.ProductService;
import com.jsj.member.ob.service.WechatService;
import com.jsj.member.ob.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
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
    WechatService wechatService;

    @GetMapping(value = {""})
    public String index(ModelMap map) {

        return "admin/index";
    }


    @GetMapping(value = {"/login"})
    public String login(ModelMap map) {
        return "admin/login";
    }


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

        request.setAttribute("activityCount",activityCount);
        request.setAttribute("productCount",productCount);
        request.setAttribute("deliveryCount",deliveryCount);
        request.setAttribute("wechatCount",wechatCount);

        return "admin/main";

    }


}
