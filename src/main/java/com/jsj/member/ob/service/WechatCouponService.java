package com.jsj.member.ob.service;

import com.baomidou.mybatisplus.service.IService;
import com.jsj.member.ob.dto.api.coupon.WechatCouponDto;
import com.jsj.member.ob.entity.WechatCoupon;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *   @description : WechatCoupon 服务接口
 *   ---------------------------------
 * 	 @author cc
 *   @since 2019-02-11
 */
public interface WechatCouponService extends IService<WechatCoupon> {

    /**
     * 获取可用优惠券列表
     * @param unionId
     * @return
     */
    List<WechatCouponDto> getWechatCoupons(@Param("unionId")String unionId);

}
