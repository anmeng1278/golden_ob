package com.jsj.member.ob.dto.mini;

import com.jsj.member.ob.dto.api.redpacket.OrderRedpacketCouponDto;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class RedPacketResp {

    @ApiModelProperty(value = "领取的优惠券，为null时已领完", required = true)
    private OrderRedpacketCouponDto couponDto;

    @ApiModelProperty(value = "couponDto不为null时，此值表示是否重复领取", required = true)
    private boolean isRepeatDraw;

    @ApiModelProperty(value = "领取记录", required = true)
    private List<OrderRedpacketCouponDto> redpacketCoupons;


    public OrderRedpacketCouponDto getCouponDto() {
        return couponDto;
    }

    public void setCouponDto(OrderRedpacketCouponDto couponDto) {
        this.couponDto = couponDto;
    }

    public boolean isRepeatDraw() {
        return isRepeatDraw;
    }

    public void setRepeatDraw(boolean repeatDraw) {
        isRepeatDraw = repeatDraw;
    }

    public List<OrderRedpacketCouponDto> getRedpacketCoupons() {
        return redpacketCoupons;
    }

    public void setRedpacketCoupons(List<OrderRedpacketCouponDto> redpacketCoupons) {
        this.redpacketCoupons = redpacketCoupons;
    }
}
