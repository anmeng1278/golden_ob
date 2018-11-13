package com.jsj.member.ob.dto.api.activity;

import com.jsj.member.ob.enums.ActivityOrderStatus;

public class ActivityOrderDto {


    /**
     * 主键
     */
    private Integer activityOrderId;
    /**
     * 活动编号
     */
    private Integer activityId;
    /**
     * 微信openid
     */
    private String openId;
    /**
     * 过期时间
     */
    private Integer expireTime;
    /**
     * 父编号,为null时openid为团长
     */
    private Integer parentActivityOrderId;
    /**
     * 组团状态 0 组团中  10 组团成功  20 组团失败 30 创建订单成功   60 已取消
     */
    private Integer status;

    private ActivityOrderStatus activityOrderStatus;

    private String remarks;
    /**
     * 创建时间
     */
    private Integer createTime;
    /**
     * 更新时间
     */
    private Integer updateTime;
    /**
     * 删除时间
     */
    private Integer deleteTime;

}
