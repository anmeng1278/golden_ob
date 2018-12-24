package com.jsj.member.ob.rabbitmq.wx;

import com.jsj.member.ob.rabbitmq.BaseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class WxSender {

    private static Logger log = LoggerFactory.getLogger(WxSender.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendNormal(BaseDto mm) {
        this.rabbitTemplate.convertAndSend(WxConfig.WX_NORMAL_QUEUE, mm);
    }

    public void sendError(BaseDto mm) {
        this.rabbitTemplate.convertAndSend(WxConfig.WX_ERROR_QUEUE, mm);
    }


}
