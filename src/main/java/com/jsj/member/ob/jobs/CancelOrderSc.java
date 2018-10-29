package com.jsj.member.ob.jobs;

import com.alibaba.fastjson.JSON;
import com.jsj.member.ob.logic.OrderLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 系统自动取消未支付订单
 */
@Component("CancelOrderSc")
public class CancelOrderSc {

    private static final Logger LOGGER = LoggerFactory.getLogger(CancelOrderSc.class);
    public synchronized void begin() {

        try {
            List<Integer> orderIds = OrderLogic.CancelOrder();
            if (!orderIds.isEmpty()) {
                LOGGER.info(String.format("自动取消订单：%s", JSON.toJSONString(orderIds)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
