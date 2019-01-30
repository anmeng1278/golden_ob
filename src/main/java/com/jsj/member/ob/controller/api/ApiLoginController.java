package com.jsj.member.ob.controller.api;

import com.jsj.member.ob.dto.api.wechat.Jscode2SessionRequest;
import com.jsj.member.ob.dto.api.wechat.Jscode2SessionResponse;
import com.jsj.member.ob.logic.WechatLogic;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${webconfig.virtualPath}/mini")
public class ApiLoginController {

    /**
     * 小程序Jscode换取openId
     *
     * @param request
     * @return
     */
    @PostMapping("/Jscode2Session")
    public Jscode2SessionResponse Jscode2Session(@RequestBody Jscode2SessionRequest request) {
        Jscode2SessionResponse resp = WechatLogic.JsCode2Session(request);
        return resp;
    }

}
