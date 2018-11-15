package com.jsj.member.ob.logic;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jsj.member.ob.dto.api.coupon.*;
import com.jsj.member.ob.dto.api.product.ProductDto;
import com.jsj.member.ob.entity.Coupon;
import com.jsj.member.ob.entity.CouponProduct;
import com.jsj.member.ob.entity.WechatCoupon;
import com.jsj.member.ob.enums.CouponStatus;
import com.jsj.member.ob.enums.CouponType;
import com.jsj.member.ob.enums.CouponUseRange;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.service.CouponProductService;
import com.jsj.member.ob.service.CouponService;
import com.jsj.member.ob.service.WechatCouponService;
import com.jsj.member.ob.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class CouponLogic {


    public static CouponLogic couponLogic;

    // 初始化的时候，将本类中的sysConfigManager赋值给静态的本类变量
    @PostConstruct
    public void init() {
        couponLogic = this;
        couponLogic.couponService = this.couponService;
        couponLogic.couponProductService = this.couponProductService;
    }

    @Autowired
    CouponService couponService;

    @Autowired
    WechatCouponService wechatCouponService;

    @Autowired
    CouponProductService couponProductService;

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
        if (wechatCoupon.getExpiredTime() < DateUtils.getCurrentUnixTime()) {
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
                    discountAmount = requ.getUseAmount();
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
    public static CouponDto GetCoupon(int couponId) {

        Coupon entity = couponLogic.couponService.selectById(couponId);

        CouponDto coupon = new CouponDto();

        coupon.setAmount(entity.getAmount());
        coupon.setCouponId(entity.getCouponId());
        coupon.setCouponName(entity.getCouponName());
        coupon.setCouponType(CouponType.valueOf(entity.getTypeId()));
        coupon.setCouponUseRange(CouponUseRange.valueOf(entity.getUserRange()));

        coupon.setValidDays(entity.getValidDays());

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
    public static WechatCouponDto GetWechatCoupon(int wechatCouponId) {

        WechatCoupon entity = couponLogic.wechatCouponService.selectById(wechatCouponId);

        WechatCouponDto wechatCouponDto = new WechatCouponDto();

        wechatCouponDto.setAmount(entity.getAmount());
        wechatCouponDto.setCouponId(entity.getCouponId());
        wechatCouponDto.setCouponType(CouponType.valueOf(entity.getTypeId()));
        wechatCouponDto.setExpiredTime(entity.getExpiredTime());
        wechatCouponDto.setOpenId(entity.getOpenId());

        wechatCouponDto.setCouponStatus(CouponStatus.valueOf(entity.getStatus()));
        wechatCouponDto.setWechatCouponId(entity.getWechatCouponId());

        return wechatCouponDto;

    }
    //endregion

    /**
     * 获得我所有可用的优惠券
     *
     * @param openId
     * @return
     */
    public static List<WechatCouponDto> GetMyAvailableCoupon(String openId) {

        if (StringUtils.isBlank(openId)) {
            throw new TipException("参数不合法，用户openId不能为空");
        }

        EntityWrapper<WechatCoupon> wrapper = new EntityWrapper<>();
        wrapper.where("delete_time is null and open_id={0}", openId);
        wrapper.where("status=0");
        wrapper.le("expired_time", DateUtils.getCurrentUnixTime());
        List<WechatCoupon> wechatCoupons = couponLogic.wechatCouponService.selectList(wrapper);

        List<WechatCouponDto> wechatCouponDtos = new ArrayList<>();

        for (WechatCoupon wechatCoupon : wechatCoupons) {

            WechatCouponDto wechatCouponDto = new WechatCouponDto();

            //获取优惠券详情
            CouponDto couponDto = CouponLogic.GetCoupon(wechatCoupon.getCouponId());
            wechatCouponDto.setCouponDto(couponDto);

            wechatCouponDto.setAmount(wechatCoupon.getAmount());
            wechatCouponDto.setCouponId(wechatCoupon.getCouponId());
            wechatCouponDto.setCouponType(CouponType.valueOf(wechatCoupon.getTypeId()));
            wechatCouponDto.setExpiredTime(wechatCoupon.getExpiredTime());
            wechatCouponDto.setOpenId(wechatCoupon.getOpenId());
            wechatCouponDto.setCouponStatus(CouponStatus.valueOf(wechatCoupon.getStatus()));
            wechatCouponDto.setWechatCouponId(wechatCoupon.getWechatCouponId());
            wechatCouponDtos.add(wechatCouponDto);
        }
        return wechatCouponDtos;

    }

    /**
     * 获取优惠券可使用的商品
     * @param couponId
     * @return
     */
    public static List<CouponProductDto> GetCouponProduct(int couponId){
        if(couponId < 0){
            throw new TipException("参数不正确！");
        }

        EntityWrapper<CouponProduct> wrapper = new EntityWrapper<>();
        wrapper.where("delete_time is null and coupon_id={0}",couponId);
        List<CouponProduct> couponProducts = couponLogic.couponProductService.selectList(wrapper);
        if(couponProducts.size() == 0){
            throw new TipException("该优惠券适用于全部商品！");
        }

        List<CouponProductDto> couponProductDtos = new ArrayList<>();
        for (CouponProduct couponProduct : couponProducts) {
            CouponProductDto couponProductDto = new CouponProductDto();

            ProductDto productDto = ProductLogic.GetProduct(couponProduct.getProductId());
            couponProductDto.setProductDto(productDto);

            couponProductDto.setCouponId(couponProduct.getCouponId());
            couponProductDto.setCouponProductId(couponProduct.getCouponProductId());
            couponProductDto.setProductId(couponProduct.getProductId());

            couponProductDtos.add(couponProductDto);

        }

        return couponProductDtos;
    }

}
