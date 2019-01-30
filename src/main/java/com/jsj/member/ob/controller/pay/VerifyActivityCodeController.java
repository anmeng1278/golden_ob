package com.jsj.member.ob.controller.pay;

import com.alibaba.fastjson.JSON;
import com.jsj.member.ob.dto.api.delivery.VerifyActivityCodeRequ;
import com.jsj.member.ob.dto.api.delivery.VerifyActivityCodeResp;
import com.jsj.member.ob.logic.DeliveryLogic;
import com.jsj.member.ob.utils.HttpUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

@ApiIgnore
@Controller
@RequestMapping("${webconfig.virtualPath}/ActivityCode")
public class VerifyActivityCodeController {

    @PostMapping(value = {"/Verify"})
    @ResponseBody
    public VerifyActivityCodeResp verify(HttpServletRequest request) {

        VerifyActivityCodeResp resp = new VerifyActivityCodeResp();

        byte[] bytes = HttpUtils.readAsBytes(request);
        if (bytes == null || bytes.length == 0) {
            resp.setRespMsg("非法请求");
            resp.setRespCd("9999");
            return resp;
        }

        VerifyActivityCodeRequ requ = JSON.parseObject(new String(bytes), VerifyActivityCodeRequ.class);

        resp = DeliveryLogic.VerifyActivityCode(requ);
        return resp;
    }
}
