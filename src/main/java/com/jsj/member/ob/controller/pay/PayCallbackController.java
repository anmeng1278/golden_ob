package com.jsj.member.ob.controller.pay;

import com.google.protobuf.InvalidProtocolBufferException;
import com.jsj.member.ob.dto.proto.NotifyModelOuterClass;
import com.jsj.member.ob.utils.HttpUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 支付回调控制器
 */
@Controller
@RequestMapping("/paycallback")
public class PayCallbackController {

    @PostMapping(value = {""})
    @ResponseBody
    public String index(HttpServletRequest request) throws InvalidProtocolBufferException {

        byte[] bytes = HttpUtils.readAsBytes(request);


        NotifyModelOuterClass.NotifyModel notifyModel = NotifyModelOuterClass.NotifyModel.parseFrom(bytes);

        System.out.println(notifyModel.getBankName());
        System.out.println(notifyModel.getApplyStatus());
        System.out.println(notifyModel.getCardNo());
        System.out.println(notifyModel.getCardHolderName());

        return "";
    }


}
