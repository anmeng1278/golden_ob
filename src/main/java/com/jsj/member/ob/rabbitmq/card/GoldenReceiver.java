package com.jsj.member.ob.rabbitmq.card;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.dto.thirdParty.CreateGoldenCardRequ;
import com.jsj.member.ob.dto.thirdParty.CreateGoldenCardResp;
import com.jsj.member.ob.entity.Delivery;
import com.jsj.member.ob.entity.DeliveryStock;
import com.jsj.member.ob.entity.Stock;
import com.jsj.member.ob.enums.DeliveryStatus;
import com.jsj.member.ob.enums.GoldenCardType;
import com.jsj.member.ob.enums.StockStatus;
import com.jsj.member.ob.logic.ThirdPartyLogic;
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
import java.util.List;

@Service
public class GoldenReceiver {

    private final Logger logger = LoggerFactory.getLogger(GoldenReceiver.class);

    @Autowired
    DeliveryService deliveryService;

    @Autowired
    DeliveryStockService deliveryStockService;

    @Autowired
    StockService stockService;

    @RabbitListener(queues = GoldenConfig.GOLDEN_NORMAL_QUEUE)
    @Transactional(Constant.DBTRANSACTIONAL)
    public void sendMessage(CreateGoldenDto dto, Channel channel, Message message) throws IOException {

        try {

            CreateGoldenCardRequ requ = new CreateGoldenCardRequ();

            requ.getRequestBody().setCardType(GoldenCardType.getApiCode(GoldenCardType.valueOf(dto.getCardType())));
            requ.getRequestBody().setMemberIdNumber(dto.getMemberIdNumber());
            requ.getRequestBody().setMemberIDType(dto.getMemberIDType());
            requ.getRequestBody().setMemberMobile(dto.getMemberMobile());
            requ.getRequestBody().setMemberName(dto.getMemberName());
            requ.getRequestBody().setSalePrice(dto.getSalePrice());

            CreateGoldenCardResp resp = ThirdPartyLogic.CreateGoldenCard(requ);

            Delivery delivery = deliveryService.selectById(dto.getDeliveryId());
            List<DeliveryStock> deliveryStocks = deliveryStockService.selectList(new EntityWrapper<DeliveryStock>().where("delivery_id = {0}", delivery.getDeliveryId()));

            delivery.setStatus(DeliveryStatus.DELIVERED.getValue());
            String remark = delivery.getRemarks();

            if (resp.getResponseHead().getCode().equals("0000")) {
                //开卡成功
                if (remark != null) {
                    remark += String.format("开卡成功：%s", GoldenCardType.valueOf(dto.getCardType()).getMessage());
                } else {
                    remark = String.format("开卡成功：%s", GoldenCardType.valueOf(dto.getCardType()).getMessage());
                }

                //TODO 开卡成功发送模板消息
                //示例：您的GoldenCardType.valueOf(dto.getCardType()).getMessage()卡已开通成功

            } else {
                //开卡失败
                if (remark != null) {
                    remark += String.format("开卡失败：%s", resp.getResponseHead().getMessage());
                } else {
                    remark = String.format("开卡失败：%s", resp.getResponseHead().getMessage());
                }
            }
            delivery.setRemarks(remark);
            deliveryService.updateById(delivery);

            //更新发货状态
            deliveryStocks.forEach(ds -> {
                Stock stock = stockService.selectById(ds.getStockId());
                stock.setStatus(StockStatus.SENT.getValue());
                stockService.updateById(stock);
            });

        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("开卡任务执行失败：" + JSON.toJSONString(ex));
        } finally {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }


}
