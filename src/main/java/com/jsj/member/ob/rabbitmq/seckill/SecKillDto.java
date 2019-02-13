package com.jsj.member.ob.rabbitmq.seckill;


import com.jsj.member.ob.enums.SourceType;
import com.jsj.member.ob.rabbitmq.BaseDto;

public class SecKillDto extends BaseDto {

    /**
     * 用户编号
     */
    private String openId;

    /**
     * 用户unionId
     */
    private String unionId;

    /**
     * 活动编号
     */
    private Integer activityId;

    /**
     * 商品编号
     */
    private Integer productId;

    /**
     * 型号编号
     */
    private Integer productSpecId;

    /**
     * 订单来源
     */
    private SourceType sourceType;

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

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getProductSpecId() {
        return productSpecId;
    }

    public void setProductSpecId(Integer productSpecId) {
        this.productSpecId = productSpecId;
    }

    public SourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(SourceType sourceType) {
        this.sourceType = sourceType;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }
}
