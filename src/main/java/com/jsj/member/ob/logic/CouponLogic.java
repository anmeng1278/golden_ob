package com.jsj.member.ob.logic;

import com.jsj.member.ob.dto.api.coupon.UseCouponRequ;
import com.jsj.member.ob.dto.api.coupon.UseCouponResp;
import com.jsj.member.ob.entity.Coupon;
import com.jsj.member.ob.entity.WechatCoupon;
import com.jsj.member.ob.enums.CouponStatus;
import com.jsj.member.ob.enums.CouponType;
import com.jsj.member.ob.enums.CouponUseRange;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.service.CouponService;
import com.jsj.member.ob.service.WechatCouponService;
import com.jsj.member.ob.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class CouponLogic {


    public static CouponLogic couponLogic;

    // 初始化的时候，将本类中的sysConfigManager赋值给静态的本类变量
    @PostConstruct
    public void init() {
        couponLogic = this;
        couponLogic.couponService = this.couponService;
    }

    @Autowired
    CouponService couponService;

    @Autowired
    WechatCouponService wechatCouponService;

    //region (public) 使用优惠券 UseCoupon

    /**
     * 使用优惠券
     *
     * @param requ
     * @return
     */
    public static UseCouponResp UseCoupon(UseCouponRequ requ) {

        UseCouponResp resp = new UseCouponResp();

        //参数校验
        if (requ.getWechatCouponId() <= 0) {
            throw new TipException("使用优惠券编号不能为空");
        }
        if (requ.getUseAmount() <= 0) {
            return resp;
        }

        //查询当前使用券信息
        WechatCoupon wechatCoupon = couponLogic.wechatCouponService.selectById(requ.getWechatCouponId());
        if (wechatCoupon.getStatus() != CouponStatus.UNUSE.getValue()) {
            throw new TipException(String.format("当前优惠券不可用，%s", CouponStatus.valueOf(wechatCoupon.getStatus()).getMessage()));
        }
        if (wechatCoupon.getExpireTime() < DateUtils.getCurrentUnixTime()) {
            throw new TipException("当前优惠券不可用，已过期");
        }

        //应支付金额
        double payAmount = 0d;
        //抵扣金额
        double discountAmount = 0d;

        switch (CouponType.valueOf(wechatCoupon.getTypeId())) {
            //直减券
            case CUT: {
                if (requ.getUseAmount() <= wechatCoupon.getAmount()) {
                    //支付金额小于券面额时，支付金额置为0
                    payAmount = 0;
                    discountAmount = wechatCoupon.getAmount();
                } else {
                    //支付金额大于券面额时，支付金额 = 请求金额 - 券面额
                    payAmount = requ.getUseAmount() - wechatCoupon.getAmount();
                    discountAmount = wechatCoupon.getAmount();
                }
            }
            break;
            //折扣券
            case DISCOUNT: {
                //支付金额 = 请求金额 * 折扣
                payAmount = requ.getUseAmount() * wechatCoupon.getAmount();
                discountAmount = (requ.getUseAmount() - payAmount);
            }
            break;
            default:
                throw new TipException("当前优惠券不可用，未知的优惠券类型");
        }

        if (requ.isUse()) {
            wechatCoupon.setStatus(CouponStatus.USED.getValue());
            couponLogic.wechatCouponService.updateById(wechatCoupon);
        }
        resp.setWechatCouponId(requ.getWechatCouponId());
        resp.setPayAmount(payAmount);
        resp.setDiscountAmount(discountAmount);

        return resp;

    }
    //endregion

    //region (public) 获取优惠券详情信息 GetCoupon

    /**
     * 获取优惠券详情信息
     *
     * @param couponId
     * @return
     */
    public static com.jsj.member.ob.dto.api.coupon.Coupon GetCoupon(int couponId) {

        Coupon entity = couponLogic.couponService.selectById(couponId);

        com.jsj.member.ob.dto.api.coupon.Coupon coupon = new com.jsj.member.ob.dto.api.coupon.Coupon();

        coupon.setAmount(entity.getAmount());
        coupon.setCouponId(entity.getCouponId());
        coupon.setCouponName(entity.getCouponName());
        coupon.setCouponType(CouponType.valueOf(entity.getTypeId()));
        coupon.setCouponUseRange(CouponUseRange.valueOf(entity.getUserRange()));

        coupon.setExpiredDays(entity.getExpiredDays());

        return coupon;
    }
    //endregion

    //region (public) 获取领取券详情 GetWechatCoupon

    /**
     * 获取领取券详情
     *
     * @param wechatCouponId
     * @return
     */
    public static com.jsj.member.ob.dto.api.coupon.WechatCoupon GetWechatCoupon(int wechatCouponId) {

        WechatCoupon entity = couponLogic.wechatCouponService.selectById(wechatCouponId);

        com.jsj.member.ob.dto.api.coupon.WechatCoupon wechatCoupon = new com.jsj.member.ob.dto.api.coupon.WechatCoupon();

        wechatCoupon.setAmount(entity.getAmount());
        wechatCoupon.setCouponId(entity.getCouponId());
        wechatCoupon.setCouponType(CouponType.valueOf(entity.getTypeId()));
        wechatCoupon.setExpireTime(entity.getExpireTime());
        wechatCoupon.setOpenId(entity.getOpenId());

        wechatCoupon.setCouponStatus(CouponStatus.valueOf(entity.getStatus()));
        wechatCoupon.setWechatCouponId(entity.getWechatCouponId());

        return wechatCoupon;

    }
    //endregion

}
