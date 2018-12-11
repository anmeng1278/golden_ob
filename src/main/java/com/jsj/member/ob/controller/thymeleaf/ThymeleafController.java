package com.jsj.member.ob.controller.thymeleaf;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/thymeleaf")
public class ThymeleafController {


    @GetMapping(value = {""})
    public String index(HttpServletRequest request) {

        request.setAttribute("id", 123);
        return "thymeleaf/index";

    }
}
