package com.jsj.member.ob.rabbitmq.card;

import com.jsj.member.ob.rabbitmq.BaseDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class GoldenSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendNormal(BaseDto mm) {
        this.rabbitTemplate.convertAndSend(GoldenConfig.GOLDEN_NORMAL_QUEUE, mm);
    }

    public void sendError(BaseDto mm) {
        this.rabbitTemplate.convertAndSend(GoldenConfig.GOLDEN_ERROR_QUEUE, mm);
    }

}
