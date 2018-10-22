package com.jsj.member.ob.controller.admin;

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

        request.setAttribute("os", props.getProperty("os.name"));
        request.setAttribute("version", props.getProperty("java.version"));

        request.setAttribute("port", request.getLocalPort());
        request.setAttribute("ip", request.getLocalAddr());

        return "admin/main";

    }

}
