package com.jsj.member.ob.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jsj.member.ob.dto.api.coupon.WechatCouponDto;
import com.jsj.member.ob.entity.WechatCoupon;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *   @description : WechatCoupon Mapper 接口
 *   ---------------------------------
 * 	 @author cc
 *   @since 2019-02-11
 */
@Repository
public interface WechatCouponMapper extends BaseMapper<WechatCoupon> {


    /**
     * 获取可用优惠券列表
     * @param unionId
     * @return
     */
    List<WechatCouponDto> getWechatCoupons(@Param("unionId")String unionId);

}