package com.jsj.member.ob.controller.index;

import com.jsj.member.ob.controller.BaseController;
import com.jsj.member.ob.dto.RestResponseBo;
import com.jsj.member.ob.dto.api.delivery.DeliveryDto;
import com.jsj.member.ob.dto.api.express.ExpressRequ;
import com.jsj.member.ob.dto.api.express.ExpressResp;
import com.jsj.member.ob.dto.api.stock.StockDto;
import com.jsj.member.ob.logic.DeliveryLogic;
import com.jsj.member.ob.logic.ExpressApiLogic;
import com.jsj.member.ob.logic.StockLogic;
import com.jsj.member.ob.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

@ApiIgnore
@Controller
@RequestMapping("${webconfig.virtualPath}/delivery")
public class DeliveryController extends BaseController {

    @Autowired
    DeliveryService deliveryService;


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
        for (DeliveryDto deliveryDto : deliveryDtos) {

            List<Integer> stockIds = deliveryDto.getStockDtos().stream().map(StockDto::getStockId).collect(Collectors.toList());

            deliveryDto.getStockDtos().forEach(stockDto -> {
                        Integer count = StockLogic.GetProductCount(stockDto.getProductId(), stockDto.getProductSpecId(), stockIds);
                        stockDto.setNumber(count);
                    }
            );

            ArrayList<StockDto> stockDto = deliveryDto.getStockDtos().stream().collect(
                    Collectors.collectingAndThen(
                            Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(StockDto::getProductSpecId))), ArrayList::new));
            deliveryDto.setStockDtos(stockDto);
        }

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

        DeliveryDto deliveryDto = DeliveryLogic.GetDelivery(deliveryId);

        //查询配送的物流信息
        ExpressRequ requ = new ExpressRequ();
        requ.setText(deliveryDto.getExpressNumber());

        ExpressResp resp = ExpressApiLogic.GetExpressHundred(requ);

        List data = resp.getData();

        request.setAttribute("data", data);
        request.setAttribute("deliveryDto", deliveryDto);

        return "index/deliveryDetail";
    }



    /**
     * 修改状态
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public RestResponseBo updateStatus(HttpServletRequest request) {

        int id = Integer.parseInt(request.getParameter("id"));
        String method = request.getParameter("method");

        DeliveryLogic.UpdateDeliveryStatus(id, method);

        return RestResponseBo.ok("操作成功");
    }

}