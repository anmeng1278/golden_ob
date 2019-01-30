package com.jsj.member.ob.dto.api.order;

import com.jsj.member.ob.dto.BaseRequ;
import com.jsj.member.ob.enums.ActivityType;
import com.jsj.member.ob.enums.SourceType;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

public class CreateOrderRequ {

    public CreateOrderRequ() {
        this.baseRequ = new BaseRequ();
        this.orderProductDtos = new ArrayList<>();
    }

    private BaseRequ baseRequ;

    /**
     * 购买商品
     */
    @NotBlank(message = "购买商品")
    @ApiModelProperty(value = "购买商品", required = true)
    private List<OrderProductDto> orderProductDtos;

    /**
     * 使用优惠券编号
     */
    private int wechatCouponId;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 活动订单号
     */
    private int activityOrderId;

    /**
     * 活动编号
     */
    private int activityId;

    /**
     * 购买份数
     */
    @NotBlank(message = "购买商品")
    @ApiModelProperty(value = "购买商品", required = true)
    private int number;

    /**
     * 订单来源
     */
    private SourceType sourceType;

    /**
     * 订单类型
     */
    private ActivityType activityType;

    public BaseRequ getBaseRequ() {
        return baseRequ;
    }

    public void setBaseRequ(BaseRequ baseRequ) {
        this.baseRequ = baseRequ;
    }

    public List<OrderProductDto> getOrderProductDtos() {
        return orderProductDtos;
    }

    public void setOrderProductDtos(List<OrderProductDto> orderProductDtos) {
        this.orderProductDtos = orderProductDtos;
    }

    public int getWechatCouponId() {
        return wechatCouponId;
    }

    public void setWechatCouponId(int wechatCouponId) {
        this.wechatCouponId = wechatCouponId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getActivityOrderId() {
        return activityOrderId;
    }

    public void setActivityOrderId(int activityOrderId) {
        this.activityOrderId = activityOrderId;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public SourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(SourceType sourceType) {
        this.sourceType = sourceType;
    }

}
