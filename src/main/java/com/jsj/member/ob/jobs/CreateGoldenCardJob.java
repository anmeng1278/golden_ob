package com.jsj.member.ob.jobs;

import com.jsj.member.ob.dto.api.delivery.DeliveryDto;
import com.jsj.member.ob.dto.api.stock.StockDto;
import com.jsj.member.ob.logic.DeliveryLogic;
import com.jsj.member.ob.rabbitmq.card.CardSender;
import com.jsj.member.ob.rabbitmq.card.CreateCardDto;
import com.jsj.member.ob.service.DeliveryService;
import com.jsj.member.ob.utils.SpringContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 开卡任务
 */
@Component
public class CreateGoldenCardJob {

    @Autowired
    DeliveryService deliveryService;

    @Autowired
    CardSender goldenSender;


    @Scheduled(cron = "0 0/1 * * * ?")
    public void updateDeliveryStatus() {

        if (SpringContextUtils.getActiveProfile().equals("dev")) {
            return;
        }

        try {
            List<DeliveryDto> deliveryDtos = DeliveryLogic.GetUnCreateGoldenCard();

            for (DeliveryDto dl : deliveryDtos) {

                List<StockDto> stockDtos = DeliveryLogic.GetDeliveryStock(dl.getDeliveryId());
                if (stockDtos.isEmpty()) {
                    continue;
                }

                CreateCardDto dto = new CreateCardDto();
                dto.setDeliveryId(dl.getDeliveryId());
                dto.setPropertyType(dl.getPropertyType());

                goldenSender.sendNormal(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}