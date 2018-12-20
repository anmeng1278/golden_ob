package com.jsj.member.ob.jobs;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jsj.member.ob.dto.api.express.ExpressRequ;
import com.jsj.member.ob.dto.api.express.ExpressResp;
import com.jsj.member.ob.entity.Delivery;
import com.jsj.member.ob.enums.DeliveryStatus;
import com.jsj.member.ob.enums.DeliveryType;
import com.jsj.member.ob.enums.PropertyType;
import com.jsj.member.ob.logic.DeliveryLogic;
import com.jsj.member.ob.logic.ExpressApiLogic;
import com.jsj.member.ob.service.DeliveryService;
import com.jsj.member.ob.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 每晚0点执行查询物流信息任务，若已签收修改配送状态为已签收
 */
@Component
public class DeliveryStatusJob {

    @Autowired
    DeliveryService deliveryService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void updateDeliveryStatus() {

        System.out.println("任务执行啦");

        List<Delivery> deliveries = DeliveryLogic.GetDelivery(DeliveryStatus.DELIVERED, DeliveryType.DISTRIBUTE, PropertyType.ENTITY);

        deliveries.forEach(delivery -> {
            //查询订单的物流节点，若已签收修改订单状态为已签收
            ExpressRequ requ = new ExpressRequ();
            requ.setText(delivery.getExpressNumber());
            ExpressResp resp = ExpressApiLogic.GetExpressHundred(requ);
            if (resp.getState().equals("3")) {
                //已签收
                delivery.setStatus(DeliveryStatus.SIGNED.getValue());
                delivery.setUpdateTime(DateUtils.getCurrentUnixTime());
                deliveryService.updateById(delivery);
            }
        });
    }
}