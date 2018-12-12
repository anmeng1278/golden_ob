package com.jsj.member.ob.controller.index;

import com.jsj.member.ob.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

@ApiIgnore
@Controller
@RequestMapping("/order")
public class OrderController extends BaseController {

    /**
     * 订单列表
     *
     * @param request
     * @return TODO 显示未支付、已支付的订单
     */
    @GetMapping(value = {"/order"})
    public String order(HttpServletRequest request) {

        String openId = this.OpenId();

        return "index/order";
    }

}