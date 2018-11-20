package com.jsj.member.ob.logic;

import com.jsj.member.ob.enums.*;
import org.springframework.stereotype.Component;

@Component
public class EnumLogic extends BaseLogic {

    /**
     * 获取所属类型
     *
     * @param value
     * @return
     */
    public static DictType GetDictType(Integer value) {
        return DictType.valueOf(value);
    }

    /**
     * 获取用户注册来源
     * @param value
     * @return
     */
    public static WechatType GetWechatType(Integer value) {
        return WechatType.valueOf(value);
    }

    /**
     * 获取用户性别
     * @param value
     * @return
     */
    public static SexType GetSexType(Integer value) {
        return SexType.valueOf(value);
    }

    /**
     * 获取活动类型
     * @param value
     * @return
     */
    public static ActivityType GetActivityType(Integer value) {
        return ActivityType.valueOf(value);
    }

    /**
     * 订单状态
     * @param value
     * @return
     */
    public static OrderStatus GetOrderStatus(Integer value) {
        return OrderStatus.valueOf(value);
    }

    /**
     * 配送状态
     * @param value
     * @return
     */
    public static DeliveryStatus GetDeliveryStatus(Integer value) {
        return DeliveryStatus.valueOf(value);
    }

    /**
     * 配送方式
     * @param value
     * @return
     */
    public static DeliveryType GetDeliveryType(Integer value) {
        return DeliveryType.valueOf(value);
    }


    /**
     * 库存获取方式
     * @param value
     * @return
     */
    public static StockType GetStockType(Integer value) {
        return StockType.valueOf(value);
    }

    /**
     * 库存状态
     * @param value
     * @return
     */
    public static StockStatus GetStockStatus(Integer value) {
        return StockStatus.valueOf(value);
    }

    /**
     * 赠送状态
     * @param value
     * @return
     */
    public static GiftStatus GetGiftStatus(Integer value) {
        return GiftStatus.valueOf(value);
    }


    /**
     * 获取分享方式
     * @param value
     * @return
     */
    public static GiftShareType GetGiftShareType(Integer value) {
        return GiftShareType.valueOf(value);
    }

    /**
     * 优惠券类型
     * @param value
     * @return
     */
    public static CouponType GetCouponType(Integer value) {
        return CouponType.valueOf(value);
    }


    /**
     * 优惠券使用范围
     * @param value
     * @return
     */
    public static CouponUseRange GetCouponUseRange(Integer value) {
        return CouponUseRange.valueOf(value);
    }

    /**
     * 礼包类型
     * @param value
     * @return
     */
    public static RedpacketType GetRedpacketType(Integer value) {
        return RedpacketType.valueOf(value);
    }


    /**
     * 商品图片类型
     * @param value
     * @return
     */
    public static ProductImgType GetProductImgType(Integer value) {
        return ProductImgType.valueOf(value);
    }

    /**
     * 赠送文案类型
     * @param value
     * @return
     */
    public static CopywriterType GetCopywriterType(Integer value) {
        return CopywriterType.valueOf(value);
    }

    /**
     * 赠送文案类型
     * @param value
     * @return
     */
    public static BannerType GetBannerType(Integer value) {
        return BannerType.valueOf(value);
    }

}
