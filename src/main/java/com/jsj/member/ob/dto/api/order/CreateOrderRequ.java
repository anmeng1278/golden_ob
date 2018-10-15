package com.jsj.member.ob.dto.api.order;

import com.jsj.member.ob.dto.BaseRequ;
import com.jsj.member.ob.enums.OrderType;

import java.util.List;

public class CreateOrderRequ {

    public CreateOrderRequ() {
        this.baseRequ = new BaseRequ();
    }

    private BaseRequ baseRequ;

    /**
     * 购买商品
     */
    private List<OrderProduct> orderProducts;

    /**
     * 使用优惠券编号
     */
    private Integer wechatCouponId;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 团单号
     */
    private Integer teamOrderId;

    /**
     * 秒杀编号
     */
    private Integer secKillId;

    /**
     * 订单类型
     */
    private OrderType orderType;


    public BaseRequ getBaseRequ() {
        return baseRequ;
    }

    public void setBaseRequ(BaseRequ baseRequ) {
        this.baseRequ = baseRequ;
    }

    public List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(List<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }

    public Integer getWechatCouponId() {
        return wechatCouponId;
    }

    public void setWechatCouponId(Integer wechatCouponId) {
        this.wechatCouponId = wechatCouponId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getTeamOrderId() {
        return teamOrderId;
    }

    public void setTeamOrderId(Integer teamOrderId) {
        this.teamOrderId = teamOrderId;
    }

    public Integer getSecKillId() {
        return secKillId;
    }

    public void setSecKillId(Integer secKillId) {
        this.secKillId = secKillId;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }
}
