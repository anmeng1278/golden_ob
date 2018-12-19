package com.jsj.member.ob.logic;

import com.alibaba.fastjson.JSON;
import com.jsj.member.ob.App;
import com.jsj.member.ob.dto.api.airport.JsAirportDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
public class VipHallLogicTest {

    @Test
    public void getVipHallDtos() {

        List<JsAirportDto> jsVipHallDtos = AirportLogic.GetAirportDtos(null);
        System.out.println(JSON.toJSONString(jsVipHallDtos));

    }
}