package com.jsj.member.ob.logic;

import com.alibaba.fastjson.JSON;
import com.jsj.member.ob.App;
import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.dto.BaseRequ;
import com.jsj.member.ob.dto.api.gift.*;
import com.jsj.member.ob.enums.GiftShareType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
public class GiftLogicTest {

    @Test
    public void createGift() {

        BaseRequ baseRequ = new BaseRequ();
        baseRequ.setOpenId("777777");

        CreateGiftRequ requ = new CreateGiftRequ();

        requ.setBlessings("祝你平安");
        requ.setBaseRequ(baseRequ);
        requ.setGiftShareType(GiftShareType.GROUP);

        List<GiftProductDto> dtos = new ArrayList<>();

        GiftProductDto dto1 = new GiftProductDto();
        dto1.setNumber(1);
        dto1.setProductId(1);
        dto1.setProductSpecId(1);
        dtos.add(dto1);

        //GiftProductDto dto2 = new GiftProductDto();
        //dto2.setNumber(2);
        //dto2.setProductId(1);
        //dto2.setProductSpecId(2);
        //dtos.add(dto2);
        //
        //GiftProductDto dto3 = new GiftProductDto();
        //dto3.setNumber(1);
        //dto3.setProductId(1);
        //dto3.setProductSpecId(3);
        //dtos.add(dto3);

        requ.setGiftProductDtos(dtos);

        CreateGiftResp resp = GiftLogic.CreateGift(requ);
        System.out.println(JSON.toJSONString(resp));

    }


    @Test
    @Transactional(Constant.DBTRANSACTIONAL)
    @Rollback(false)
    public void getGift() {


        BaseRequ baseRequ = new BaseRequ();
        baseRequ.setOpenId("888888");


        ReceivedGiftRequ requ = new ReceivedGiftRequ();

        requ.setBaseRequ(baseRequ);
        requ.setGiftUniqueCode("25fc37e43a984016912dc3cac01fd89f");

        ReceivedGiftResp resp = GiftLogic.ReceivedGift(requ);
        System.out.println(JSON.toJSONString(resp));

    }


    @Test
    @Transactional(Constant.DBTRANSACTIONAL)
    @Rollback(false)
    public void cancelGift() {
        BaseRequ baseRequ = new BaseRequ();
        baseRequ.setOpenId("44444");

        CancelGiftRequ requ = new CancelGiftRequ();
        requ.setGiftUniqueCode("25fc37e43a984016912dc3cac01fd89f");
        requ.setBaseRequ(baseRequ);

        CancelGiftResp resp = GiftLogic.CancelGift(requ);
        System.out.println(JSON.toJSONString(resp));

    }




}