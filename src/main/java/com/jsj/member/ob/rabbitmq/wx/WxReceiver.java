package com.jsj.member.ob.rabbitmq.wx;

import com.alibaba.fastjson.JSON;
import com.jsj.member.ob.enums.TemplateType;
import com.jsj.member.ob.logic.ThirdPartyLogic;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class WxReceiver {

    private static Logger logger = LoggerFactory.getLogger(WxReceiver.class);

    @RabbitListener(queues = WxConfig.WX_NORMAL_QUEUE)
    public void sendMessage(TemplateDto dto, Channel channel, Message message) throws IOException {

        logger.info(JSON.toJSONString(dto));

        try {
            if (dto.getTemplateType().equals(TemplateType.SERVICE)) {
                ThirdPartyLogic.SendWxMessage(dto.getToUser(), dto.getRemark());
            } else {
                String templdateId = this.templateId(dto.getTemplateType());
                if (org.apache.commons.lang3.StringUtils.isEmpty(templdateId)) {
                    return;
                }
                dto.setTemplateId(templdateId);
                ThirdPartyLogic.SendWxTemplate(dto);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }


    //获取模板编号
    private String templateId(TemplateType templateType) {

        switch (templateType) {
            case SERVICE:
                return "";
            case PAYSUCCESSED:
                return this.PaySucccessedTemplateId;
            case OPENCARDCONFIRM:
                return this.OpenCardConfirmTemplateId;
            case QRCODEUSESUCCESSED:
                return this.QrcodeUseSuccessedTemplateId;
            case ENTITYUSESUCCESSED:
                return this.EntityUseSuccessedTemplateId;
            case CANCELUNPAYORDER:
                return this.CancelUnPayOrderTemplateId;
        }
        return "";
    }


    /**
     * 支付成功模板
     */
    @Value(value = "${webconfig.WxTemplate.PaySuccessed}")
    private String PaySucccessedTemplateId;

    /**
     * 开卡确认模板
     */
    @Value(value = "${webconfig.WxTemplate.OpenCardConfirm}")
    private String OpenCardConfirmTemplateId;

    /**
     * 活动码使用成功模板
     */
    @Value(value = "${webconfig.WxTemplate.QrcodeUseSuccessed}")
    private String QrcodeUseSuccessedTemplateId;

    /**
     * 实物商品使用成功模板
     */
    @Value(value = "${webconfig.WxTemplate.EntityUseSuccessed}")
    private String EntityUseSuccessedTemplateId;

    /**
     * 取消待支付订单模板
     */
    @Value(value = "${webconfig.WxTemplate.CancelUnPayOrder}")
    private String CancelUnPayOrderTemplateId;




}
