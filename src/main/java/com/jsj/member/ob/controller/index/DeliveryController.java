package com.jsj.member.ob.controller.index;

import com.jsj.member.ob.controller.BaseController;
import com.jsj.member.ob.dto.api.delivery.DeliveryDto;
import com.jsj.member.ob.logic.DeliveryLogic;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ApiIgnore
@Controller
@RequestMapping("${webconfig.virtualPath}/delivery")
public class DeliveryController extends BaseController {


    /**
     * 配送列表
     *
     * @param request
     * @return
     */
    @GetMapping("")
    public String index(HttpServletRequest request) {

        String openId = this.OpenId();

        List<DeliveryDto> deliveryDtos = DeliveryLogic.GetDelivery(openId);
        request.setAttribute("deliveryDtos", deliveryDtos);

        return "index/delivery";
    }

    /**
     * 查看物流
     *
     * @param deliveryId
     * @param request
     * @return
     */
    @GetMapping("/{deliveryId}")
    public String info(@PathVariable("deliveryId") int deliveryId, HttpServletRequest request) {



        ////查询配送的物流信息
        //ExpressRequ requ = new ExpressRequ();
        //requ.setText(expressNumber);
        //
        //ExpressResp resp = ExpressApiLogic.GetExpressHundred(requ);
        //
        //List data = resp.getData();
        //
        //request.setAttribute("data", data);
        //request.setAttribute("expressNumber", expressNumber);

        return "index/deliveryDetail";
    }


}