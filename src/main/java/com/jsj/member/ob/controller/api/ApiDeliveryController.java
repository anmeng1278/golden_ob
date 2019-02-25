package com.jsj.member.ob.controller.api;

import com.jsj.member.ob.controller.BaseController;
import com.jsj.member.ob.dto.api.Request;
import com.jsj.member.ob.dto.api.Response;
import com.jsj.member.ob.dto.api.delivery.DeliveryDto;
import com.jsj.member.ob.dto.api.express.ExpressRequ;
import com.jsj.member.ob.dto.api.express.ExpressResp;
import com.jsj.member.ob.dto.api.stock.StockDto;
import com.jsj.member.ob.dto.mini.*;
import com.jsj.member.ob.entity.DeliveryStock;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.DeliveryLogic;
import com.jsj.member.ob.logic.ExpressApiLogic;
import com.jsj.member.ob.logic.StockLogic;
import com.jsj.member.ob.service.DeliveryService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/mini")
public class ApiDeliveryController extends BaseController {

    @Autowired
    DeliveryService deliveryService;


    //region (public) 配送列表 deliverys

    /**
     * 配送列表
     *
     * @param requ
     * @return
     */

    @ApiOperation(value = "配送列表")
    @RequestMapping(value = "/deliverys", method = RequestMethod.POST)
    public Response<DeliverysResp> deliverys(@ApiParam(value = "请求实体", required = true)
                                             @RequestBody
                                             @Validated Request<DeliverysRequ> requ) {

        String unionId = requ.getRequestBody().getUnionId();
        DeliverysResp resp = new DeliverysResp();

        List<DeliveryDto> deliveryDtos = DeliveryLogic.GetDelivery(requ.getRequestBody().getUnionId());
        for (DeliveryDto deliveryDto : deliveryDtos) {

            List<Integer> stockIds = deliveryDto.getStockDtos().stream().map(StockDto::getStockId).collect(Collectors.toList());

            deliveryDto.getStockDtos().forEach(stockDto -> {
                        Integer count = StockLogic.GetProductCount(unionId, stockDto.getProductId(), stockDto.getProductSpecId(), stockIds);
                        stockDto.setNumber(count);
                    }
            );

            ArrayList<StockDto> stockDto = deliveryDto.getStockDtos().stream().collect(
                    Collectors.collectingAndThen(
                            Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(StockDto::getProductSpecId))), ArrayList::new));
            deliveryDto.setStockDtos(stockDto);
        }

        resp.setDeliveryDtos(deliveryDtos);
        return Response.ok(resp);
    }
    //endregion

    //region (public) 配送详情 deliveryDetail

    /**
     * 配送详情
     *
     * @param requ1
     * @return
     */

    @ApiOperation(value = "配送详情")
    @RequestMapping(value = "/deliveryDetail", method = RequestMethod.POST)
    public Response<DeliveryDetailResp> deliveryDetail(@ApiParam(value = "请求实体", required = true)
                                                       @RequestBody
                                                       @Validated Request<DeliveryDetailRequ> requ1) {

        DeliveryDetailResp resp1 = new DeliveryDetailResp();

        int deliveryId = requ1.getRequestBody().getDeliveryId();
        DeliveryDto deliveryDto = DeliveryLogic.GetDelivery(deliveryId);

        if (!org.apache.commons.lang3.StringUtils.isEmpty(deliveryDto.getExpressNumber())) {
            //查询配送的物流信息
            ExpressRequ requ = new ExpressRequ();
            requ.setText(deliveryDto.getExpressNumber());

            ExpressResp resp = ExpressApiLogic.GetExpressHundred(requ);
            List data = resp.getData();
            resp1.setData(data);
        }

        List<DeliveryStock> deliveryStocks = DeliveryLogic.GetDeliveryStocks(deliveryId);
        if (deliveryStocks.isEmpty()) {
            throw new TipException("没有找到库存信息");
        }

        int stockId = requ1.getRequestBody().getStockId();
        if (stockId > 0) {

            Optional<DeliveryStock> deliveryStock = deliveryStocks.stream().filter(ds -> ds.getStockId().equals(stockId)).findFirst();
            if (!deliveryStock.isPresent()) {
                throw new TipException("没有找到库存信息");
            }

            if (StringUtils.isEmpty(deliveryStock.get().getActivityCode())) {
                throw new TipException("没有找到活动码信息");
            }

            String imgUrl = String.format(super.webconfig.getQrcodeUrl(), deliveryStock.get().getActivityCode());
            resp1.setImgUrl(imgUrl);
        }

        resp1.setDeliveryDto(deliveryDto);

        return Response.ok(resp1);
    }
    //endregion

    //region (public) 修改配送状态 updateDelivery

    /**
     * 修改配送状态
     *
     * @param requ
     * @return
     */
    @ApiOperation(value = "修改配送状态")
    @RequestMapping(value = "/updateDelivery", method = RequestMethod.POST)
    public Response<UpdateDeliveryResp> updateDelivery(@ApiParam(value = "请求实体", required = true)
                                                       @RequestBody
                                                       @Validated Request<UpdateDeliveryRequ> requ) {

        UpdateDeliveryResp resp = new UpdateDeliveryResp();

        int deliveryId = requ.getRequestBody().getDeliveryId();
        String method = requ.getRequestBody().getMethod();

        DeliveryLogic.UpdateDeliveryStatus(deliveryId, method);

        return Response.ok(resp);
    }
    //endregion

}