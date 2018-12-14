package com.jsj.member.ob.controller.index;

import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.controller.BaseController;
import com.jsj.member.ob.dto.api.redpacket.OrderRedpacketCouponDto;
import com.jsj.member.ob.logic.RedpacketLogic;
import com.jsj.member.ob.utils.EncryptUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ApiIgnore
@Controller
@RequestMapping("${webconfig.virtualPath}/share")
public class ShareController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(ShareController.class);

    /**
     * 红包领取页面
     *
     * @param request
     * @return 示例链接：
     * http://localhost/share/redPacket?ok=2rpaS3MKnYw%3d
     */
    @GetMapping(value = {"/redPacket/{obs}"})
    @Transactional(Constant.DBTRANSACTIONAL)
    public String redPacket(
            @PathVariable(name = "obs", required = true) String obs,
            HttpServletRequest request) {

        int orderId = 0;
        try {
            orderId = Integer.parseInt(EncryptUtils.decrypt(obs));
        } catch (Exception e) {
            return this.Redirect("/");
        }

        String openId = this.OpenId();

        //领取
        OrderRedpacketCouponDto couponDto = RedpacketLogic.DistributeRedpacket(openId, orderId);
        request.setAttribute("couponDto", couponDto);

        //查看领取记录
        List<OrderRedpacketCouponDto> redpacketCoupons = RedpacketLogic.GetOrderRedpacketDtos(orderId, true);
        request.setAttribute("redpacketCoupons", redpacketCoupons);

        return "index/redPacket";

    }

}
