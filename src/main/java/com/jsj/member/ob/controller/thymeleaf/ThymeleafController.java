package com.jsj.member.ob.controller.thymeleaf;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

@ApiIgnore
@Controller
@RequestMapping("/thymeleaf")
public class ThymeleafController {


    @GetMapping(value = {""})
    public String index(HttpServletRequest request) {

        request.setAttribute("id", 123);
        return "thymeleaf/index";

    }
}
