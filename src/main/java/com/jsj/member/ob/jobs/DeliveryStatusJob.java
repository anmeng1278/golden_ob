package com.jsj.member.ob.jobs;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jsj.member.ob.dto.api.delivery.DeliveryDto;
import com.jsj.member.ob.dto.api.express.ExpressRequ;
import com.jsj.member.ob.dto.api.express.ExpressResp;
import com.jsj.member.ob.entity.Delivery;
import com.jsj.member.ob.enums.DeliveryStatus;
import com.jsj.member.ob.enums.DeliveryType;
import com.jsj.member.ob.logic.ExpressApiLogic;
import com.jsj.member.ob.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
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
        //获取所有配送订单（有快递号，订单类型为配送，状态为已发货，delete_time is null）
        System.out.println("任务执行啦");
        EntityWrapper<Delivery> wrapper = new EntityWrapper<>();
        wrapper.where("delete_time is null and express_number is not null");
        wrapper.where("status = {0} and type_id = {1}",DeliveryStatus.DELIVERED.getValue(), DeliveryType.DISTRIBUTE.getValue());
        List<Delivery> deliveries = deliveryService.selectList(wrapper);
        for (Delivery delivery : deliveries) {
            try {
                //查询订单的物流节点，若已签收修改订单状态为已签收
                ExpressRequ requ = new ExpressRequ();
                requ.setText(delivery.getExpressNumber());
                ExpressResp resp = ExpressApiLogic.GetExpressHundred(requ);
                if (resp.getState().equals("3")) {
                    //已签收
                    delivery.setStatus(DeliveryStatus.SIGNED.getValue());
                    deliveryService.updateById(delivery);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}