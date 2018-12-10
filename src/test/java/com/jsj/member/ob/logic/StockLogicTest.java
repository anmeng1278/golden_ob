package com.jsj.member.ob.logic;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jsj.member.ob.App;
import com.jsj.member.ob.dto.BaseRequ;
import com.jsj.member.ob.dto.api.stock.GetMyStockRequ;
import com.jsj.member.ob.dto.api.stock.StockDto;
import com.jsj.member.ob.dto.api.stock.StockFlowDto;
import com.jsj.member.ob.entity.Stock;
import com.jsj.member.ob.enums.StockType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
public class StockLogicTest {

    @Test
    public void getMyStock() {
        GetMyStockRequ requ = new GetMyStockRequ();
        BaseRequ baseRequ = new BaseRequ();
        baseRequ.setOpenId("111");
        requ.setBaseRequ(baseRequ);

        List<StockDto> stockDtoList = new ArrayList<>();

        StockDto stockDto = new StockDto();
        stockDto.setNumber(2);
        stockDto.setOpenId(baseRequ.getOpenId());
        stockDto.setOrderId(15);
        stockDto.setStockId(2);
        stockDto.setProductId(2);

        stockDtoList.add(stockDto);
        /*
        GetMyStockResp resp = new GetMyStockResp();
        resp.setStockDtoList(stockDtoList);*/

        StockLogic.GetMyStock("111");
    }

    @Test
    public void getStockFlows() {

        List<StockFlowDto> dtos = StockLogic.GetStockFlows(26, true);
        System.out.println(JSON.toJSONString(dtos));

    }

}