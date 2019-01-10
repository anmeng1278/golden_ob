package com.jsj.member.ob.dto.api.order;

import com.jsj.member.ob.enums.OrderStatus;
import com.jsj.member.ob.utils.XmlToEntityUtils;

public class UserOrderDto {

    private String openId;

    private String nickName;

    private Integer orderId;

    private Integer status;

    private OrderStatus orderStatus;

    private Double payAmount;

    private Integer expiredTime;

    private String xml;

    private Item item;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public OrderStatus getOrderStatus() {
        return OrderStatus.valueOf(status);
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Integer expiredTime) {
        this.expiredTime = expiredTime;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public Double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Double payAmount) {
        this.payAmount = payAmount;
    }

    public Item getItem() {
        Item item = (Item) XmlToEntityUtils.ConvertXmlToObject(Item.class, xml);
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
