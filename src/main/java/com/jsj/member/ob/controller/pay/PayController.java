package com.jsj.member.ob.controller.pay;

import com.google.protobuf.InvalidProtocolBufferException;
import com.jsj.member.ob.config.Webconfig;
import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.controller.BaseController;
import com.jsj.member.ob.dto.proto.NotifyModelOuterClass;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.order.OrderBase;
import com.jsj.member.ob.logic.order.OrderFactory;
import com.jsj.member.ob.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

/**
 * 支付回调控制器
 */
@Controller
@RequestMapping("${webconfig.virtualPath}/pay")
public class PayController extends BaseController {


    @Autowired
    Webconfig webconfig;


    @PostMapping(value = {"/callback"})
    @ResponseBody
    @Transactional(Constant.DBTRANSACTIONAL)
    public String callback(HttpServletRequest request) throws InvalidProtocolBufferException {

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

    @GetMapping(value = {"/success/{obs}"})
    public String paySuccessed(
            @PathVariable(name = "obs", required = true) String obs,
            HttpServletRequest request) throws UnsupportedEncodingException {

        //分享红包地址
        String shareUrl = this.Url(String.format("/share/redPacket/%s", obs), false);
        request.setAttribute("shareUrl", shareUrl);

        return "index/paySuccessed";
    }


}
