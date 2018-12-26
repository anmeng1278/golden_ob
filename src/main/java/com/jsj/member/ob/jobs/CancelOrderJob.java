package com.jsj.member.ob.jobs;

import com.alibaba.fastjson.JSON;
import com.jsj.member.ob.logic.OrderLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 系统自动取消未支付订单
 */
@Component
public class CancelOrderJob {

    private static final Logger logger = LoggerFactory.getLogger(CancelOrderJob.class);

    @Scheduled(cron = "0/10 * * * * ?")
    public synchronized void begin() {

        try {
            List<Integer> orderIds = OrderLogic.CancelOrder();
            if (!orderIds.isEmpty()) {
                logger.info(String.format("自动取消订单：%s", JSON.toJSONString(orderIds)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
