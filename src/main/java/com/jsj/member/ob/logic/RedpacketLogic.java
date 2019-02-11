package com.jsj.member.ob.logic;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jsj.member.ob.dto.api.coupon.CouponDto;
import com.jsj.member.ob.dto.api.redpacket.OrderRedpacketCouponDto;
import com.jsj.member.ob.dto.api.redpacket.RedpacketCouponDto;
import com.jsj.member.ob.dto.api.redpacket.RedpacketDto;
import com.jsj.member.ob.entity.OrderRedpacketCoupon;
import com.jsj.member.ob.entity.Redpacket;
import com.jsj.member.ob.entity.RedpacketCoupon;
import com.jsj.member.ob.entity.WechatCoupon;
import com.jsj.member.ob.enums.CouponStatus;
import com.jsj.member.ob.enums.CouponType;
import com.jsj.member.ob.enums.RedpacketType;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.service.OrderRedpacketCouponService;
import com.jsj.member.ob.service.RedpacketCouponService;
import com.jsj.member.ob.service.RedpacketService;
import com.jsj.member.ob.service.WechatCouponService;
import com.jsj.member.ob.tuple.TwoTuple;
import com.jsj.member.ob.utils.DateUtils;
import com.jsj.member.ob.utils.TupleUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class RedpacketLogic extends BaseLogic {

    public static RedpacketLogic redpacketLogic;

    // 初始化的时候，将本类中的sysConfigManager赋值给静态的本类变量
    @PostConstruct
    public void init() {
        redpacketLogic = this;
    }

    @Autowired
    RedpacketService redpacketService;

    @Autowired
    RedpacketCouponService redpacketCouponService;

    @Autowired
    OrderRedpacketCouponService orderRedpacketCouponService;

    @Autowired
    WechatCouponService wechatCouponService;


    //region (public) 获取礼包信息 GetRedpacket

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
    //endregion

    //region (public) 礼包优惠券信息 GetRedpacketCoupon

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
    //endregion

    //region (public) 给订单创建一个礼包 CreateOrderRedpacket

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
        if (redpacketCouponDtos.size() == 0) {
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
    //endregion

    //region (public) 发送礼包 DistributeRedpacket

    /**
     * 发送礼包
     *
     * @param openId
     * @param unionId
     * @param orderId
     * @return
     */
    public static TwoTuple<OrderRedpacketCouponDto, Boolean> DistributeRedpacket(String openId, String unionId, int orderId) {

        if (orderId <= 0) {
            throw new TipException("参数不合法，订单ID不能为空");
        }

        if (StringUtils.isBlank(openId)) {
            throw new TipException("参数不合法，用户openId为空");
        }

        if (StringUtils.isBlank(unionId)) {
            throw new TipException("参数不合法，用户unionId为空");
        }

        //每个人只能领取一次
        EntityWrapper<OrderRedpacketCoupon> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("union_id={0} and order_id={1}", unionId, orderId);
        entityWrapper.where("ifreceived = 1 and delete_time is null ");

        OrderRedpacketCoupon distributeRedpacketed = redpacketLogic.orderRedpacketCouponService.selectOne(entityWrapper);

        //已领取
        if (distributeRedpacketed != null) {
            Integer orderRedpacketCouponId = distributeRedpacketed.getOrderRedpacketCouponId();
            return TupleUtils.tuple(RedpacketLogic.GetOrderRedpacketCouponDto(orderRedpacketCouponId), true);
        }

        //获得待领取记录
        List<OrderRedpacketCoupon> orderRedpacketCoupons = RedpacketLogic.GetOrderRedpackets(orderId, false);

        if (orderRedpacketCoupons.size() == 0) {
            return null;
        }
        Random random = new Random();
        int i = random.nextInt(orderRedpacketCoupons.size());

        OrderRedpacketCoupon orderRedpacketCoupon = orderRedpacketCoupons.get(i);

        //获取对应的优惠券信息
        CouponDto couponDto = CouponLogic.GetCoupon(orderRedpacketCoupon.getCouponId());

        //产生一个小于集合大小的随机数，根据这个随机数查找第i条数据给用户
        WechatCoupon wechatCoupon = new WechatCoupon();

        //把获得的信息插入到_wechat_coupon并设置_order_redpacket_coupon已领取，领取人openId，领取时间
        wechatCoupon.setOpenId(openId);
        wechatCoupon.setUnionId(unionId);
        wechatCoupon.setOrderRedpacketCouponId(orderRedpacketCoupon.getOrderRedpacketCouponId());
        wechatCoupon.setCouponId(orderRedpacketCoupon.getCouponId());
        wechatCoupon.setExpiredTime((couponDto.getValidDays()) * 60 * 60 * 24 + DateUtils.getCurrentUnixTime());
        wechatCoupon.setStatus(CouponStatus.UNUSE.getValue());
        wechatCoupon.setAmount(couponDto.getAmount());
        wechatCoupon.setTypeId(couponDto.getCouponType().getValue());
        wechatCoupon.setCreateTime(DateUtils.getCurrentUnixTime());
        wechatCoupon.setUpdateTime(DateUtils.getCurrentUnixTime());

        redpacketLogic.wechatCouponService.insert(wechatCoupon);

        orderRedpacketCoupon.setIfreceived(true);
        orderRedpacketCoupon.setOpenId(openId);
        orderRedpacketCoupon.setUnionId(unionId);
        orderRedpacketCoupon.setReceivedTime(DateUtils.getCurrentUnixTime());

        redpacketLogic.orderRedpacketCouponService.updateById(orderRedpacketCoupon);

        return TupleUtils.tuple(RedpacketLogic.GetOrderRedpacketCouponDto(orderRedpacketCoupon.getOrderRedpacketCouponId()), false);

    }
    //endregion

    //region (public) 获取领取详情 GetOrderRedpacketCouponDto

    /**
     * 获取领取详情
     *
     * @param orderRedpacketCouponId
     * @return
     */
    public static OrderRedpacketCouponDto GetOrderRedpacketCouponDto(int orderRedpacketCouponId) {

        OrderRedpacketCouponDto dto = new OrderRedpacketCouponDto();
        OrderRedpacketCoupon entity = redpacketLogic.orderRedpacketCouponService.selectById(orderRedpacketCouponId);

        dto.setAmount(entity.getAmount());
        dto.setCouponId(entity.getCouponId());
        dto.setCouponDto(CouponLogic.GetCoupon(entity.getCouponId()));
        dto.setCouponType(CouponType.valueOf(entity.getTypeId()));
        dto.setIfreceived(entity.getIfreceived());

        dto.setOpenId(entity.getOpenId());
        dto.setOrderId(entity.getOrderId());
        dto.setOrderRedpacketCouponId(entity.getOrderRedpacketCouponId());
        dto.setReceivedTime(entity.getReceivedTime());
        dto.setRedpacketCouponId(entity.getRedpacketCouponId());

        if (!StringUtils.isEmpty(entity.getOpenId())) {
            dto.setWechatDto(WechatLogic.GetWechat(entity.getOpenId()));
        }

        dto.setWechatCouponDto(CouponLogic.GetWechatCouponByOrderRedpacketCouponId(entity.getOrderRedpacketCouponId()));

        return dto;

    }
    //endregion

    //region (public) 获得订单红包 GetOrderRedpackets

    /**
     * 获得订单红包
     *
     * @param orderId    订单编号
     * @param ifreceived 领取状态 null 查全部
     * @return
     */
    public static List<OrderRedpacketCoupon> GetOrderRedpackets(int orderId, Boolean ifreceived) {

        if (orderId <= 0) {
            throw new TipException("参数不合法，订单ID不能为空");
        }

        EntityWrapper<OrderRedpacketCoupon> wrapper = new EntityWrapper();
        wrapper.where("delete_time is null and order_id = {0}", orderId);

        if (ifreceived != null) {
            if (ifreceived.booleanValue()) {
                wrapper.where("ifreceived = 1");
            } else {
                wrapper.where("ifreceived = 0");
            }
        }
        wrapper.orderBy("received_time desc, create_time desc");

        List<OrderRedpacketCoupon> orderRedpacketCoupons = redpacketLogic.orderRedpacketCouponService.selectList(wrapper);
        return orderRedpacketCoupons;

    }
    //endregion

    //region (public) 获得订单红包 GetOrderRedpacketDtos

    /**
     * 获得订单红包
     *
     * @param orderId    订单编号
     * @param ifreceived 领取状态 null 查全部
     * @return
     */
    public static List<OrderRedpacketCouponDto> GetOrderRedpacketDtos(int orderId, Boolean ifreceived) {

        List<OrderRedpacketCoupon> orderRedpacketCoupons = GetOrderRedpackets(orderId, ifreceived);
        List<OrderRedpacketCouponDto> dtos = new ArrayList<>();

        orderRedpacketCoupons.forEach(entity -> {
            OrderRedpacketCouponDto dto = GetOrderRedpacketCouponDto(entity.getOrderRedpacketCouponId());
            dtos.add(dto);
        });

        return dtos;
    }
    //endregion

    //region (public) 更新红包排序 Sort

    /**
     * 更新红包排序
     *
     * @param redpacketId
     * @param ifUp
     */
    public static void Sort(int redpacketId, Boolean ifUp) {

        Redpacket current = redpacketLogic.redpacketService.selectById(redpacketId);

        EntityWrapper<Redpacket> wrapper = new EntityWrapper<>();
        wrapper.where("delete_time is null");
        wrapper.orderBy("sort asc, ifpass desc");
        List<Redpacket> redpackets = redpacketLogic.redpacketService.selectList(wrapper);

        //重置所有排序
        int sort = 0;
        for (Redpacket redpacket : redpackets) {
            redpacket.setSort(sort);
            redpacketLogic.redpacketService.updateById(redpacket);
            sort++;
        }

        if (ifUp != null) {
            //向上
            if (ifUp) {

                current = redpackets.stream().filter(p -> p.getRedpacketId() == redpacketId).findFirst().get();
                if (current.getSort() == 0) {
                    return;
                }
                int currentSort = current.getSort();
                Redpacket prev = redpackets.stream().filter(p -> p.getSort() == (currentSort - 1)).findFirst().get();
                prev.setSort(prev.getSort() + 1);

                current.setSort(current.getSort() - 1);

                redpacketLogic.redpacketService.updateById(current);
                redpacketLogic.redpacketService.updateById(prev);

            } else {
                //向下移动
                current = redpackets.stream().filter(p -> p.getRedpacketId() == redpacketId).findFirst().get();
                if (current.getSort() == redpackets.size() - 1) {
                    return;
                }
                int currentSort = current.getSort();
                Redpacket next = redpackets.stream().filter(p -> p.getSort() == (currentSort + 1)).findFirst().get();
                next.setSort(next.getSort() - 1);

                current.setSort(current.getSort() + 1);

                redpacketLogic.redpacketService.updateById(current);
                redpacketLogic.redpacketService.updateById(next);
            }
        }


    }
    //endregion
}
