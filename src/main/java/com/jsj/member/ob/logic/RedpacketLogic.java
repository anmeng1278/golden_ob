package com.jsj.member.ob.logic;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jsj.member.ob.dto.api.coupon.CouponDto;
import com.jsj.member.ob.dto.api.redpacket.RedpacketCouponDto;
import com.jsj.member.ob.dto.api.redpacket.RedpacketDto;
import com.jsj.member.ob.entity.OrderRedpacketCoupon;
import com.jsj.member.ob.entity.Redpacket;
import com.jsj.member.ob.entity.RedpacketCoupon;
import com.jsj.member.ob.entity.WechatCoupon;
import com.jsj.member.ob.enums.CouponStatus;
import com.jsj.member.ob.enums.RedpacketType;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.service.OrderRedpacketCouponService;
import com.jsj.member.ob.service.RedpacketCouponService;
import com.jsj.member.ob.service.RedpacketService;
import com.jsj.member.ob.service.WechatCouponService;
import com.jsj.member.ob.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public class RedpacketLogic {

    public static RedpacketLogic redpacketLogic;

    // 初始化的时候，将本类中的sysConfigManager赋值给静态的本类变量
    @PostConstruct
    public void init() {
        redpacketLogic = this;
        redpacketLogic.redpacketService = this.redpacketService;
        redpacketLogic.redpacketCouponService = this.redpacketCouponService;
        redpacketLogic.orderRedpacketCouponService = this.orderRedpacketCouponService;
        redpacketLogic.wechatCouponService = this.wechatCouponService;
    }

    @Autowired
    RedpacketService redpacketService;

    @Autowired
    RedpacketCouponService redpacketCouponService;

    @Autowired
    OrderRedpacketCouponService orderRedpacketCouponService;

    @Autowired
    WechatCouponService wechatCouponService;



    /**
     * 获取礼包信息
     *
     * @param redpacketId
     * @return
     */
    public static RedpacketDto GetRedpacket(int redpacketId) {

        if (redpacketId <= 0) {
            throw new TipException("参数不合法，红包ID不能为空");
        }

        RedpacketDto dto = new RedpacketDto();

        Redpacket redpacket = redpacketLogic.redpacketService.selectById(redpacketId);

        dto.setRedpacketId(redpacket.getRedpacketId());
        dto.setRedpacketName(redpacket.getRedpacketName());
        dto.setIfpass(redpacket.getIfpass());
        dto.setBeginTime(redpacket.getBeginTime());
        dto.setEndTime(redpacket.getEndTime());

        dto.setRedpacketType(RedpacketType.valueOf(redpacket.getTypeId()));
        dto.setCreateTime(redpacket.getCreateTime());
        dto.setDeleteTime(redpacket.getDeleteTime());
        dto.setUpdateTime(redpacket.getUpdateTime());

        return dto;

    }

    /**
     * 礼包优惠券信息
     *
     * @param redpacketId
     * @return
     */
    public static List<RedpacketCouponDto> GetRedpacketCoupon(int redpacketId) {

        if (redpacketId <= 0) {
            throw new TipException("参数不合法，红包ID不能为空");
        }

        EntityWrapper<RedpacketCoupon> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("redpacket_id={0}", redpacketId);
        entityWrapper.where("delete_time is null");

        List<RedpacketCoupon> redpacketCoupons = redpacketLogic.redpacketCouponService.selectList(entityWrapper);

        List<RedpacketCouponDto> redpacketCouponDtos = new ArrayList<>();
        //如果礼包中优惠券为空直接返回
        if (redpacketCoupons.size() == 0) {
            return redpacketCouponDtos;
        }

        for (RedpacketCoupon redpacketCoupon : redpacketCoupons) {

            RedpacketCouponDto redpacketCouponDto = new RedpacketCouponDto();

            //获得优惠券信息
            CouponDto couponDto = CouponLogic.GetCoupon(redpacketCoupon.getCouponId());

            redpacketCouponDto.setCouponDto(couponDto);
            redpacketCouponDto.setCouponId(redpacketCoupon.getCouponId());
            redpacketCouponDto.setRedpacketId(redpacketCoupon.getRedpacketId());
            redpacketCouponDto.setNumber(redpacketCoupon.getNumber());
            redpacketCouponDto.setRedpacketCouponId(redpacketCoupon.getRedpacketCouponId());
            redpacketCouponDto.setCreateTime(redpacketCoupon.getCreateTime());
            redpacketCouponDto.setUpdateTime(redpacketCoupon.getUpdateTime());

            redpacketCouponDtos.add(redpacketCouponDto);

        }
        return redpacketCouponDtos;

    }




    /**
     * 给订单创建一个礼包
     *
     * @param orderId
     * @return
     */
    public static void CreateOrderRedpacket(int orderId) {

        if (orderId <= 0) {
            throw new TipException("参数不合法，订单ID不能为空");
        }

        //判断当前订单是否已经分配红包
        EntityWrapper<OrderRedpacketCoupon> entityWrapper = new EntityWrapper();
        entityWrapper.where("order_id={0}", orderId);
        List<OrderRedpacketCoupon> orderRedpacketCoupons = redpacketLogic.orderRedpacketCouponService.selectList(entityWrapper);
        if (orderRedpacketCoupons.size() != 0) {
            return;
        }

        //如没有分配红包则查询出一个排名最优，在有效期内的红包
        EntityWrapper<Redpacket> redpacketWrapper = new EntityWrapper<>();
        redpacketWrapper.where("delete_time is null");
        redpacketWrapper.where("ifpass=1");
        redpacketWrapper.where("UNIX_TIMESTAMP() between begin_time and end_time");
        redpacketWrapper.orderBy("sort asc");
        Redpacket redpacket = redpacketLogic.redpacketService.selectOne(redpacketWrapper);
        if (redpacket == null) {
            return;
        }

        //获取红包中的优惠券
        List<RedpacketCouponDto> redpacketCouponDtos = RedpacketLogic.GetRedpacketCoupon(redpacket.getRedpacketId());
        if(redpacketCouponDtos.size() == 0){
            return;
        }

        //将红包中的优惠券插入到订单红包优惠券表中
        for (RedpacketCouponDto redpacketCouponDto : redpacketCouponDtos) {

            for (int i = 0; i < redpacketCouponDto.getNumber(); i++) {

                OrderRedpacketCoupon orderRedpacketCoupon = new OrderRedpacketCoupon();

                orderRedpacketCoupon.setOrderId(orderId);
                orderRedpacketCoupon.setCouponId(redpacketCouponDto.getCouponId());
                orderRedpacketCoupon.setAmount(redpacketCouponDto.getCouponDto().getAmount());
                orderRedpacketCoupon.setRedpacketCouponId(redpacketCouponDto.getRedpacketCouponId());
                orderRedpacketCoupon.setRedpacketCouponId(redpacketCouponDto.getRedpacketCouponId());
                orderRedpacketCoupon.setTypeId(redpacketCouponDto.getCouponDto().getCouponType().getValue());
                orderRedpacketCoupon.setCreateTime(DateUtils.getCurrentUnixTime());
                orderRedpacketCoupon.setUpdateTime(DateUtils.getCurrentUnixTime());

                redpacketLogic.orderRedpacketCouponService.insert(orderRedpacketCoupon);

            }
        }
    }


    /**
     * 发送礼包
     * @param openId
     * @param orderId
     * @return
     */
    public static WechatCoupon DistributeRedpacket(String openId, int orderId) {
        if (orderId <= 0) {
            throw new TipException("参数不合法，订单ID不能为空");
        }

        if (StringUtils.isBlank(openId)) {
            throw new TipException("参数不合法，用户openId为空");
        }

        //每个人只能领取一次
        EntityWrapper<OrderRedpacketCoupon> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("open_id={0} and order_id={1}",openId,orderId);
        entityWrapper.where( "ifreceived is true and delete_time is null ");
        List<OrderRedpacketCoupon> orderRedpacketCouponList = redpacketLogic.orderRedpacketCouponService.selectList(entityWrapper);
        if(orderRedpacketCouponList.size() != 0){
            throw new TipException("您已经领过啦！");
        }

        //获得订单的礼包
        List<OrderRedpacketCoupon> orderRedpacketCoupons = RedpacketLogic.GetOrderRedpacket(orderId);
        //如果该订单下没有礼包创建一个礼包
        /*if(orderRedpacketCoupons.size() == 0){
            RedpacketLogic.CreateOrderRedpacket(orderId);
        }*/

        //去为这个用户随机分配一个未被领取的优惠券
        List<OrderRedpacketCoupon> collect = orderRedpacketCoupons.stream().filter(item -> item.getIfreceived() == false).collect(Collectors.toList());

        //产生一个小于集合大小的随机数，根据这个随机数查找第i条数据给用户
        WechatCoupon wechatCoupon = new WechatCoupon();

        Random random = new Random();
        if (collect.size() == 0) {
            throw new TipException("已被领完啦");
        }
        int i = random.nextInt(collect.size());

        OrderRedpacketCoupon orderRedpacketCoupon = collect.get(i);

        //获取对应的优惠券信息
        CouponDto couponDto = CouponLogic.GetCoupon(orderRedpacketCoupon.getCouponId());

        //把获得的信息插入到_wechat_coupon并设置_order_redpacket_coupon已领取，领取人openId，领取时间
        wechatCoupon.setOpenId(openId);
        wechatCoupon.setOrderRedpacketCouponId(orderRedpacketCoupon.getOrderRedpacketCouponId());
        wechatCoupon.setCouponId(orderRedpacketCoupon.getCouponId());
        wechatCoupon.setExpiredTime((couponDto.getValidDays())*86400 + DateUtils.getCurrentUnixTime());
        wechatCoupon.setStatus(CouponStatus.UNUSE.getValue());
        wechatCoupon.setAmount(couponDto.getAmount());
        wechatCoupon.setTypeId(couponDto.getCouponType().getValue());
        wechatCoupon.setCreateTime(DateUtils.getCurrentUnixTime());
        wechatCoupon.setUpdateTime(DateUtils.getCurrentUnixTime());

        redpacketLogic.wechatCouponService.insert(wechatCoupon);

        orderRedpacketCoupon.setIfreceived(true);
        orderRedpacketCoupon.setOpenId(Integer.valueOf(openId));
        orderRedpacketCoupon.setReceivedTime(DateUtils.getCurrentUnixTime());

        redpacketLogic.orderRedpacketCouponService.updateById(orderRedpacketCoupon);
        return wechatCoupon;
    }

    /**
     * 获得订单红包
     *
     * @param orderId
     * @return
     */
    private static List<OrderRedpacketCoupon> GetOrderRedpacket(int orderId) {
        if (orderId <= 0) {
            throw new TipException("参数不合法，订单ID不能为空");
        }
        EntityWrapper<OrderRedpacketCoupon> wrapper = new EntityWrapper();
        wrapper.where("delete_time is null and order_id = {0}", orderId);
        List<OrderRedpacketCoupon> orderRedpacketCoupons = redpacketLogic.orderRedpacketCouponService.selectList(wrapper);
        if(orderRedpacketCoupons.size() == 0){
            throw new TipException("该订单没有礼包诶！");
        }
        return orderRedpacketCoupons;
    }
}
