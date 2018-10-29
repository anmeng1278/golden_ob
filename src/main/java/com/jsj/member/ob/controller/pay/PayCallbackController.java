package com.jsj.member.ob.controller.pay;

import com.google.protobuf.InvalidProtocolBufferException;
import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.dto.proto.NotifyModelOuterClass;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.order.OrderBase;
import com.jsj.member.ob.logic.order.OrderFactory;
import com.jsj.member.ob.utils.HttpUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional(Constant.DBTRANSACTIONAL)
    public String index(HttpServletRequest request) throws InvalidProtocolBufferException {

        byte[] bytes = HttpUtils.readAsBytes(request);
        if (bytes == null || bytes.length == 0) {
            return "SUCCESS";
        }

        NotifyModelOuterClass.NotifyModel notifyModel = NotifyModelOuterClass.NotifyModel.parseFrom(bytes);
        if (notifyModel == null) {
            throw new TipException("获取支付参数失败");
        }

        //支付成功
        if (notifyModel.getApplyStatus().equals("68")) {

            int orderId = Integer.parseInt(notifyModel.getOutTradeId());

            OrderBase orderBase = OrderFactory.GetInstance(orderId);
            orderBase.PaySuccessed(orderId, notifyModel);

        }

        System.out.println(notifyModel.getBankName());
        System.out.println(notifyModel.getApplyStatus());
        System.out.println(notifyModel.getCardNo());
        System.out.println(notifyModel.getCardHolderName());

        return "SUCCESS";
    }


}
