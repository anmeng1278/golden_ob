package com.jsj.member.ob.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.jsj.member.ob.dao.WechatCouponMapper;
import com.jsj.member.ob.dto.api.coupon.WechatCouponDto;
import com.jsj.member.ob.entity.WechatCoupon;
import com.jsj.member.ob.service.WechatCouponService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *   @description : WechatCoupon 服务实现类
 *   ---------------------------------
 * 	 @author cc
 *   @since 2019-02-11
 */
@Service
public class WechatCouponServiceImpl extends ServiceImpl<WechatCouponMapper, WechatCoupon> implements WechatCouponService {

    @Override
    public List<WechatCouponDto> getWechatCoupons(String unionId) {
        return baseMapper.getWechatCoupons(unionId);
    }

}
