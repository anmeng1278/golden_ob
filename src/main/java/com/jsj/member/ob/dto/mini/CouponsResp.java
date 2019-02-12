package com.jsj.member.ob.dto.mini;

import com.jsj.member.ob.dto.api.coupon.WechatCouponDto;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class CouponsResp {

    @ApiModelProperty(value = "用户券列表", required = true)
    List<WechatCouponDto> wechatCouponDtos;

    public List<WechatCouponDto> getWechatCouponDtos() {
        return wechatCouponDtos;
    }

    public void setWechatCouponDtos(List<WechatCouponDto> wechatCouponDtos) {
        this.wechatCouponDtos = wechatCouponDtos;
    }
}
