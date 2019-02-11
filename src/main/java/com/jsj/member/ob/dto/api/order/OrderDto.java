package com.jsj.member.ob.dto.api.order;

import com.jsj.member.ob.dto.api.product.ProductDto;
import com.jsj.member.ob.enums.OrderStatus;
import com.jsj.member.ob.enums.SourceType;

import java.util.List;

public class OrderDto {
    /**
     * 订单主键
     */
    private Integer orderId;
    /**
     * 公众号open_id
     */
    private String openId;

    /**
     * 公众号 union_id
     */
    private String unionId;

    /**
     * 订单金额
     */
    private Double amount;
    /**
     * 需要支付金额 = amount - coupon_price
     */
    private Double payAmount;
    /**
     * 订单状态，0：待支付 10：支付成功 20：支付失败 60：取消订单
     */
    private OrderStatus status;
    /**
     * 微信的支付订单号
     */
    private String transactionId;
    /**
     * 领取的优惠券ID
     */
    private Integer wechatCouponId;

    private String couponName;
    /**
     * 优惠券使用金额
     */
    private Double couponPrice;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 活动编号
     */
    private Integer activityId;

    /**
     * 活动订单编号(团购时不为空)
     */
    private Integer activityOrderId;
    /**
     * 0 普通订单  10团单  20秒杀单 30商品组合单
     */
    private Integer typeId;
    /**
     * 支付时间
     */
    private Integer payTime;
    /**
     * 支付有效时间
     */
    private Integer expiredTime;

    /**
     * 订单来源
     */
    private SourceType sourceType;

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

    /**
     * 订单唯一标识
     */
    private String orderUniqueCode;

    private List<ProductDto> productDtos;

    private List<OrderProductDto> orderProductDtos;

    public List<OrderProductDto> getOrderProductDtos() {
        return orderProductDtos;
    }

    public void setOrderProductDtos(List<OrderProductDto> orderProductDtos) {
        this.orderProductDtos = orderProductDtos;
    }

    public List<ProductDto> getProductDtos() {
        return productDtos;
    }

    public void setProductDtos(List<ProductDto> productDtos) {
        this.productDtos = productDtos;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Double payAmount) {
        this.payAmount = payAmount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Integer getWechatCouponId() {
        return wechatCouponId;
    }

    public void setWechatCouponId(Integer wechatCouponId) {
        this.wechatCouponId = wechatCouponId;
    }

    public Double getCouponPrice() {
        return couponPrice;
    }

    public void setCouponPrice(Double couponPrice) {
        this.couponPrice = couponPrice;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getActivityOrderId() {
        return activityOrderId;
    }

    public void setActivityOrderId(Integer activityOrderId) {
        this.activityOrderId = activityOrderId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getPayTime() {
        return payTime;
    }

    public void setPayTime(Integer payTime) {
        this.payTime = payTime;
    }

    public Integer getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Integer expiredTime) {
        this.expiredTime = expiredTime;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Integer deleteTime) {
        this.deleteTime = deleteTime;
    }

    public String getOrderUniqueCode() {
        return orderUniqueCode;
    }

    public void setOrderUniqueCode(String orderUniqueCode) {
        this.orderUniqueCode = orderUniqueCode;
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
