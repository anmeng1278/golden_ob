package com.jsj.member.ob.jobs;

import com.jsj.member.ob.dto.api.delivery.DeliveryDto;
import com.jsj.member.ob.dto.api.stock.StockDto;
import com.jsj.member.ob.logic.DeliveryLogic;
import com.jsj.member.ob.rabbitmq.card.CreateGoldenDto;
import com.jsj.member.ob.rabbitmq.card.GoldenSender;
import com.jsj.member.ob.service.DeliveryService;
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
    GoldenSender goldenSender;


    @Scheduled(cron = "0 0/1 * * * ?")
    public void updateDeliveryStatus() {

        //if (SpringContextUtils.getActiveProfile().equals("dev")) {
        //    return;
        //}

        try {
            List<DeliveryDto> deliveryDtos = DeliveryLogic.GetUnCreateGoldenCard();

            for (DeliveryDto dl : deliveryDtos) {

                List<StockDto> stockDtos = DeliveryLogic.GetDeliveryStock(dl.getDeliveryId());
                if (stockDtos.isEmpty()) {
                    continue;
                }

                StockDto dt = stockDtos.get(0);
                int cardTypeId = dt.getProductDto().getCardTypeId();
                if (cardTypeId == 0) {
                    continue;
                }

                CreateGoldenDto dto = new CreateGoldenDto();
                dto.setCardType(cardTypeId);
                dto.setDeliveryId(dl.getDeliveryId());
                dto.setMemberIdNumber(dl.getIdNumber());
                dto.setMemberIDType(dl.getIdTypeId() + "");
                dto.setMemberMobile(dl.getMobile() + "");
                dto.setMemberName(dl.getContactName());
                dto.setSalePrice(dt.getProductDto().getProductSpecDtos().get(0).getSalePrice() + "");

                goldenSender.sendNormal(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}