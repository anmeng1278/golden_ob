package com.jsj.member.ob.logic;

import com.alibaba.fastjson.JSON;
import com.jsj.member.ob.App;
import com.jsj.member.ob.dto.api.coupon.CouponProductDto;
import com.jsj.member.ob.dto.api.coupon.WechatCouponDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
public class CouponLogicTest {


    @Test
    public void getCouponProduct(){
        List<CouponProductDto> couponProductDtos = CouponLogic.GetCouponProduct(2);
        for (CouponProductDto couponProductDto : couponProductDtos) {
            System.out.println(couponProductDto.getCouponId());
        }
    }

    @Test
    public void TestGetCoupons() {
        List<WechatCouponDto> dtos = CouponLogic.GetWechatCoupons(7, "111");
        System.out.println(JSON.toJSONString(dtos));
    }

}