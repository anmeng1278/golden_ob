package com.jsj.member.ob.logic;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jsj.member.ob.dto.api.express.ExpressRequ;
import com.jsj.member.ob.dto.api.express.ExpressResp;
import com.jsj.member.ob.dto.api.stock.StockDto;
import com.jsj.member.ob.entity.Delivery;
import com.jsj.member.ob.entity.DeliveryStock;
import com.jsj.member.ob.enums.DeliveryStatus;
import com.jsj.member.ob.service.DeliveryService;
import com.jsj.member.ob.service.DeliveryStockService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DeliveryLogic {

    public static DeliveryLogic deliveryLogic;


    @PostConstruct
    public void init() {
        deliveryLogic = this;
        deliveryLogic.deliveryService = this.deliveryService;
        deliveryLogic.deliveryStockService = this.deliveryStockService;
    }

    @Autowired
    DeliveryService deliveryService;

    @Autowired
    DeliveryStockService deliveryStockService;

    /**
     * 获取未发货数
     *
     * @return
     */
    public static Integer GetUnDeliveryCount() {

        EntityWrapper<Delivery> wrapper = new EntityWrapper<>();

        wrapper.where("delete_time is null");
        wrapper.where("status={0}", DeliveryStatus.UNDELIVERY.getValue());

        return deliveryLogic.deliveryService.selectCount(wrapper);

    }

    /**
     * 获得配送的库存详情
     *
     * @param deliveryId
     * @return
     */
    public static List<StockDto> GetDeliveryStock(int deliveryId) {

        EntityWrapper<DeliveryStock> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("delivery_id={0}", deliveryId);
        entityWrapper.where("delete_time is null");

        List<StockDto> stockDtos = new ArrayList<>();

        List<DeliveryStock> deliveryStocks = deliveryLogic.deliveryStockService.selectList(entityWrapper);

        if (deliveryStocks.size() == 0) {
            return stockDtos;
        }
        deliveryStocks.forEach(ds -> {
            StockDto stockDto = StockLogic.GetStock(ds.getStockId());
            stockDtos.add(stockDto);
        });

        return stockDtos;
    }

    /**
     * 查询物流详情
     * @param expressNumber
     * @return
     * @throws IOException
     */
    public static List<Map<String, String>> GetDeliveryExpress(String expressNumber) throws IOException {
        ExpressRequ requ = new ExpressRequ();
        List<Map<String, String>> resps = null;
        if (!StringUtils.isBlank(expressNumber)) {
            //查询配送的物流信息
            requ.setText(expressNumber);
            ExpressResp resp = ExpressApiLogic.GetExpressHundred(requ);
            resps = new ArrayList<Map<String, String>>();
            List data = resp.getData();
            for (int i = 0; i < data.size(); i++) {
                Map<String, String> tempMap = new HashMap<String, String>();
                JSONObject temp = (JSONObject) data.get(i);
                tempMap.put("time", (String) temp.get("time"));
                tempMap.put("content", (String) temp.get("context"));
                resps.add(tempMap);
            }
        }
        return resps;
    }

}
