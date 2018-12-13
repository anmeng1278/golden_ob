package com.jsj.member.ob.controller.index;

import com.jsj.member.ob.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

@ApiIgnore
@Controller
@RequestMapping("${webconfig.virtualPath}/gift")
public class GiftController extends BaseController {


    /**
     * 配送列表
     *
     * @param request
     * @return
     */
    @GetMapping("")
    public String index(HttpServletRequest request) {

        String openId = this.OpenId();

        return "index/gift";
    }

    /**
     * 赠送详情
     *
     * @param giftId
     * @param request
     * @return
     */
    @GetMapping("/give/{giftId}")
    public String giveInfo(@PathVariable("giftId") int giftId, HttpServletRequest request) {


        return "index/giftDetail";
    }


    /**
     * 领取详情
     *
     * @param giftId
     * @param request
     * @return
     */
    @GetMapping("/received/{giftId}")
    public String receivedInfo(@PathVariable("giftId") int giftId, HttpServletRequest request) {

        return "index/receivedDetail";
    }


}