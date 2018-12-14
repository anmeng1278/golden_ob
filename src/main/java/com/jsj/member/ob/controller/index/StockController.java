package com.jsj.member.ob.controller.index;

import com.jsj.member.ob.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

@ApiIgnore
@Controller
@RequestMapping("${webconfig.virtualPath}/stock")
public class StockController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(StockController.class);

    @GetMapping(value = {""})
    public String index(HttpServletRequest request) {

        return "index/stock";
    }

}
