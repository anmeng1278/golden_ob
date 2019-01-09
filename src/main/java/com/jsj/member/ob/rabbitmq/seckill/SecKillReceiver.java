package com.jsj.member.ob.rabbitmq.seckill;

import com.jsj.member.ob.dto.api.order.CreateOrderRequ;
import com.jsj.member.ob.dto.api.order.CreateOrderResp;
import com.jsj.member.ob.dto.api.order.OrderProductDto;
import com.jsj.member.ob.enums.ActivityType;
import com.jsj.member.ob.logic.OrderLogic;
import com.jsj.member.ob.rabbitmq.MQSender;
import com.jsj.member.ob.utils.DateUtils;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
public class SecKillReceiver {

    @Autowired
    MQSender mqSender;

    @PostConstruct
    public void init() {
        mqSender.setNormalQueue(SecKillConfig.SECKILL_NORMAL_QUEUE);
        mqSender.setErrorQueue(SecKillConfig.SECKILL_ERROR_QUEUE);
    }

    /**
     * 监听秒杀队列，创建秒杀订单
     *
     * @param dto
     * @param channel
     * @param message
     * @throws IOException
     */
    @RabbitListener(queues = SecKillConfig.SECKILL_NORMAL_QUEUE)
    public void secKillOrder(SecKillDto dto, Channel channel, Message message) throws IOException {

        try {

            CreateOrderRequ requ = new CreateOrderRequ();

            requ.setActivityType(ActivityType.SECKILL);
            requ.setActivityId(dto.getActivityId());
            requ.getBaseRequ().setOpenId(dto.getOpenId());

            OrderProductDto orderProductDto = new OrderProductDto();
            orderProductDto.setProductSpecId(dto.getProductSpecId());
            orderProductDto.setProductId(dto.getProductId());
            requ.getOrderProductDtos().add(orderProductDto);
            requ.setSourceType(dto.getSourceType());

            CreateOrderResp resp = OrderLogic.CreateOrder(requ);

            if (resp.isSuccess()) {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } else {
                throw new Exception(resp.getMessage());
            }

        } catch (Exception ex) {

            dto.setTimeStamp(DateUtils.getCurrentUnixTime());
            dto.setMessage(ex.getMessage());
            dto.setSuccess(false);

            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

            //送至错误队列
            mqSender.sendError(dto);
        }


        //System.out.println(mm);
        //System.out.println(channel);
        //System.out.println(message);


        //告诉服务器收到这条消息 已经被我消费了 可以在队列删掉 这样以后就不会再发了 否则消息服务器以为这条消息没处理掉 后续还会在发
        //channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);

        //丢弃这条消息
        //channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false);

        //重发
        //channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);

    }


}
