package com.jsj.member.ob.dto.api.coupon;

import com.jsj.member.ob.dto.BaseRequ;

public class UseCouponRequ {

    public UseCouponRequ() {
        this.baseRequ = new BaseRequ();
    }

    private BaseRequ baseRequ;

    /**
     * 使用优惠券编号
     */
    private int wechatCouponId;

    /**
     * 使用金额
     */
    private double useAmount;

    /**
     * 是否使用
     * true时直接把当前券状态改为已使用
     * false时不改状态，用于计算订单金额
     */
    private boolean isUse;

    public BaseRequ getBaseRequ() {
        return baseRequ;
    }

    public void setBaseRequ(BaseRequ baseRequ) {
        this.baseRequ = baseRequ;
    }

    public int getWechatCouponId() {
        return wechatCouponId;
    }

    public void setWechatCouponId(int wechatCouponId) {
        this.wechatCouponId = wechatCouponId;
    }

    public double getUseAmount() {
        return useAmount;
    }

    public void setUseAmount(double useAmount) {
        this.useAmount = useAmount;
    }

    public boolean isUse() {
        return isUse;
    }

    public void setUse(boolean use) {
        isUse = use;
    }
}
