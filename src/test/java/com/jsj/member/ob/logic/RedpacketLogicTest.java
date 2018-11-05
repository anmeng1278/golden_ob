package com.jsj.member.ob.logic;

import com.jsj.member.ob.App;
import com.jsj.member.ob.dto.api.redpacket.RedpacketCouponDto;
import com.jsj.member.ob.dto.api.redpacket.RedpacketDto;
import com.jsj.member.ob.entity.WechatCoupon;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
public class RedpacketLogicTest {

    @Test
    public void getRedpacketCoupon() {
        List<RedpacketCouponDto> redpacketCouponDtos = RedpacketLogic.GetRedpacketCoupon(7);
        for (RedpacketCouponDto redpacketCouponDto : redpacketCouponDtos) {
            System.out.println(redpacketCouponDto);
        }
    }

    @Test
    public void getRedpacket() {
        RedpacketDto redpacketDto = RedpacketLogic.GetRedpacket(2);
        System.out.println(redpacketDto);
    }

    @Test
    public void getOrderRedpacket() {
        RedpacketLogic.GetOrderRedpacket(10024);
    }

    @Test
    public void distributeRedpacket() {
        WechatCoupon wechatCoupon = RedpacketLogic.DistributeRedpacket("11", 10023);
        System.out.println(wechatCoupon);
    }
}