package com.jsj.member.ob.logic;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jsj.member.ob.App;
import com.jsj.member.ob.dto.api.express.ExpressRequ;
import com.jsj.member.ob.dto.api.express.ExpressResp;
import com.jsj.member.ob.entity.Delivery;
import com.jsj.member.ob.enums.DeliveryStatus;
import com.jsj.member.ob.service.DeliveryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
public class TimerTaskTest {

    @Autowired
    DeliveryService deliveryService;

    @Test
    public void test() {

        Calendar date = Calendar.getInstance();

        //设置时间为 xx-xx-xx 00:00:00
        date.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE), 0, 0, 0);

        //一天的毫秒数
        long daySpan = 24 * 60 * 60 * 1000;

        Timer t = new Timer();

        t.schedule(new TimerTask() {

            public void run() {
                System.out.println("定时器执行..");

                //获取所有库存（有快递号，订单类型为配送，状态为已发货，delete_time is null）
                EntityWrapper<Delivery> wrapper = new EntityWrapper<>();
                wrapper.where("delete_time is null");
                wrapper.where("status = 10 and type_is = 20");
                List<Delivery> deliveries = deliveryService.selectList(wrapper);
                for (Delivery delivery : deliveries) {
                    try {
                        //查询订单的物流节点，若已签收修改订单状态为已签收
                        ExpressRequ requ = new ExpressRequ();
                        requ.setText(delivery.getExpressNumber());
                        ExpressResp resp = ExpressApiLogic.GetExpressHundred(requ);
                        if(resp.getState().equals(3)){
                            //已签收
                            delivery.setStatus(DeliveryStatus.SIGNED.getValue());
                            deliveryService.updateById(delivery);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

        }, date.getTime(), daySpan); //daySpan是一天的毫秒数，也是执行间隔

    }


    @Test
    public void testTimer() {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("60秒后执行此方法");

                timer.cancel();
            }
        },24*60*60*1000);
    }


}
