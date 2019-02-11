package com.jsj.member.ob.logic;

import com.jsj.member.ob.App;
import com.jsj.member.ob.dto.api.redpacket.RedpacketCouponDto;
import com.jsj.member.ob.dto.api.redpacket.RedpacketDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
public class RedpacketLogicTest {

    @Test
    public void getRedpacketCoupon() {
        List<RedpacketCouponDto> redpacketCouponDtos = RedpacketLogic.GetRedpacketCoupon(8);
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
    public void createOrderRedpacket() {
        RedpacketLogic.CreateOrderRedpacket(10023);
    }

    @Test
    public void distributeRedpacket() {
        RedpacketLogic.DistributeRedpacket("12", "", 10023);
        RedpacketLogic.DistributeRedpacket("13", "", 10023);
        RedpacketLogic.DistributeRedpacket("14", "", 10023);
        RedpacketLogic.DistributeRedpacket("15", "", 10023);
        RedpacketLogic.DistributeRedpacket("16", "", 10023);
        RedpacketLogic.DistributeRedpacket("17", "", 10023);
        RedpacketLogic.DistributeRedpacket("18", "", 10023);
        RedpacketLogic.DistributeRedpacket("19", "", 10023);
        RedpacketLogic.DistributeRedpacket("19", "", 10024);

    }
}