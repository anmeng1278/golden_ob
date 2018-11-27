package com.jsj.member.ob.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MQSender {

    private static Logger log = LoggerFactory.getLogger(MQSender.class);

    private String normalQueue;
    private String errorQueue;

    public String getNormalQueue() {
        return normalQueue;
    }

    public void setNormalQueue(String normalQueue) {
        this.normalQueue = normalQueue;
    }

    public String getErrorQueue() {
        return errorQueue;
    }

    public void setErrorQueue(String errorQueue) {
        this.errorQueue = errorQueue;
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //@Override
    //public void returnedMessage(Message message, int i, String s, String s1, String s2) {
    //    //System.out.println("sender return success" + message.toString() + "===" + i + "===" + s1 + "===" + s2);
    //}

    public void sendNormal(BaseDto mm) {

        //this.rabbitTemplate.setReturnCallback(this);
        //this.rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
        //    //if (!ack) {
        //    //    System.out.println("HelloSender消息发送失败" + cause + correlationData.toString());
        //    //} else {
        //    //    System.out.println("HelloSender 消息发送成功 ");
        //    //}
        //});
        this.rabbitTemplate.convertAndSend(this.getNormalQueue(), mm);
    }

    public void sendError(BaseDto mm) {

        //this.rabbitTemplate.setReturnCallback(this);
        //this.rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
        //    //if (!ack) {
        //    //    System.out.println("HelloSender消息发送失败" + cause + correlationData.toString());
        //    //} else {
        //    //    System.out.println("HelloSender 消息发送成功 ");
        //    //}
        //});
        this.rabbitTemplate.convertAndSend(this.getErrorQueue(), mm);

    }


}
