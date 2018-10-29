package com.jsj.member.ob.logic;

import com.jsj.member.ob.enums.*;
import org.springframework.stereotype.Component;

@Component
public class EnumLogic {

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




}
