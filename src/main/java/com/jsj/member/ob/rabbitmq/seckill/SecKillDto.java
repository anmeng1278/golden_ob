package com.jsj.member.ob.rabbitmq.seckill;


import com.jsj.member.ob.rabbitmq.BaseDto;

public class SecKillDto extends BaseDto {

    /**
     * 用户编号
     */
    private String openId;

    /**
     * 活动编号
     */
    private Integer activityId;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

}
