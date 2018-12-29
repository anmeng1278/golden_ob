package com.jsj.member.ob.rabbitmq.card;

import com.alibaba.fastjson.JSON;
import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.dto.api.delivery.OpreationDeliveryRequ;
import com.jsj.member.ob.logic.delivery.DeliveryBase;
import com.jsj.member.ob.logic.delivery.DeliveryFactory;
import com.jsj.member.ob.service.DeliveryService;
import com.jsj.member.ob.service.DeliveryStockService;
import com.jsj.member.ob.service.StockService;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
public class CardReceiver {

    private final Logger logger = LoggerFactory.getLogger(CardReceiver.class);

    @Autowired
    DeliveryService deliveryService;

    @Autowired
    DeliveryStockService deliveryStockService;

    @Autowired
    StockService stockService;

    @RabbitListener(queues = CardConfig.GOLDEN_NORMAL_QUEUE)
    @Transactional(Constant.DBTRANSACTIONAL)
    public void sendMessage(CreateCardDto dto, Channel channel, Message message) throws IOException {

        try {

            OpreationDeliveryRequ requ = new OpreationDeliveryRequ();
            requ.setDeliveryId(dto.getDeliveryId());

            DeliveryBase deliveryBase = DeliveryFactory.GetInstance(dto.getPropertyType());
            deliveryBase.OpreationDelivery(requ);

        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("开卡任务执行失败：" + JSON.toJSONString(ex));
        } finally {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }


}
