package com.jsj.member.ob.controller.index;

import com.jsj.member.ob.controller.BaseController;
import com.jsj.member.ob.dto.api.delivery.DeliveryDto;
import com.jsj.member.ob.dto.api.express.ExpressRequ;
import com.jsj.member.ob.dto.api.express.ExpressResp;
import com.jsj.member.ob.dto.api.order.OrderDto;
import com.jsj.member.ob.logic.DeliveryLogic;
import com.jsj.member.ob.logic.ExpressApiLogic;
import com.jsj.member.ob.logic.OrderLogic;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@ApiIgnore
@Controller
@RequestMapping("/delivery")
public class DeliveryController extends BaseController {


    /**
     * 配送列表
     *
     * @param request
     * @return
     */
    @GetMapping("/delivery")
    public String delivery(HttpServletRequest request) {

        String openId = this.OpenId();

        List<DeliveryDto> deliveryDtos = DeliveryLogic.GetMyDelivery(openId);
        request.setAttribute("deliveryDtos", deliveryDtos);

        return "index/DeliveryList";
    }

    /**
     * 查看物流
     *
     * @param expressNumber
     * @param request
     * @return
     */
    @GetMapping("/logistics/{expressNumber}")
    public String checkLogistics(@PathVariable("expressNumber") String expressNumber, HttpServletRequest request) {
        //查询配送的物流信息
        ExpressRequ requ = new ExpressRequ();
        requ.setText(expressNumber);
        ExpressResp resp = null;
        try {
            resp = ExpressApiLogic.GetExpressHundred(requ);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List data = resp.getData();

        request.setAttribute("data", data);
        request.setAttribute("expressNumber", expressNumber);

        return "index/LogisticsInformation";
    }


}