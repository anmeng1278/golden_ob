package com.jsj.member.ob.jobs;

import com.jsj.member.ob.entity.WechatCoupon;
import com.jsj.member.ob.enums.CouponStatus;
import com.jsj.member.ob.logic.ConfigLogic;
import com.jsj.member.ob.logic.CouponLogic;
import com.jsj.member.ob.service.WechatCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 修改用户过期优惠券状态
 */
@Component
public class CouponStatusJob {

    @Autowired
    WechatCouponService wechatCouponService;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void updateCouponStatus() {

        if (!ConfigLogic.GetWebConfig().getRunJob()) {
            return;
        }

        try {
            List<WechatCoupon> wechatCoupons = CouponLogic.GetExpiredWechatCoupons();
            if (wechatCoupons.isEmpty()) {
                return;
            }
            for (WechatCoupon wechatCoupon : wechatCoupons) {
                wechatCoupon.setStatus(CouponStatus.EXPIRED.getValue());
                wechatCouponService.updateById(wechatCoupon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}