package com.jsj.member.ob.logic;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jsj.member.ob.entity.Delivery;
import com.jsj.member.ob.enums.DeliveryStatus;
import com.jsj.member.ob.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DeliveryLogic {

    public static DeliveryLogic deliveryLogic;

    @PostConstruct
    public void init() {
        deliveryLogic = this;
        deliveryLogic.deliveryService = this.deliveryService;
    }

    @Autowired
    DeliveryService deliveryService;

    /**
     * 获取未发货数
     *
     * @return
     */
    public static Integer GetUnDeliveryCount() {

        EntityWrapper<Delivery> wrapper = new EntityWrapper<>();

        wrapper.where("delete_time is null");
        wrapper.where("status={0}", DeliveryStatus.UNDELIVERY.getValue());

        return deliveryLogic.deliveryService.selectCount(wrapper);

    }

}
