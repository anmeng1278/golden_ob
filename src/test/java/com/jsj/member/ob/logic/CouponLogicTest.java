package com.jsj.member.ob.logic;

import com.alibaba.fastjson.JSON;
import com.jsj.member.ob.App;
import com.jsj.member.ob.dto.api.coupon.CouponProductDto;
import com.jsj.member.ob.dto.api.coupon.WechatCouponDto;
import org.apache.commons.lang3.StringUtils;
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
public class CouponLogicTest {


    @Test
    public void getCouponProduct() {
        List<CouponProductDto> couponProductDtos = CouponLogic.GetCouponProduct(2);
        for (CouponProductDto couponProductDto : couponProductDtos) {
            System.out.println(couponProductDto.getCouponId());
        }
    }

    @Test
    public void TestGetCoupons() {

        List<Integer> productIds = new ArrayList<>();
        productIds.add(9);

        String join = StringUtils.join(productIds, ",");
        System.out.println(join);

        List<WechatCouponDto> dtos = CouponLogic.GetWechatCoupons(productIds, "oGCG8t0mY567-U7VBgxh7tOULeJY");
        System.out.println(JSON.toJSONString(dtos));
    }

}