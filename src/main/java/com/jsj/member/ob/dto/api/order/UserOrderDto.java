package com.jsj.member.ob.dto.api.order;

import com.jsj.member.ob.enums.OrderStatus;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

public class UserOrderDto {

    private String openId;

    private String nickName;

    private Integer orderId;

    private OrderStatus orderStatus;

    private Double payAmount;

    private Integer expiredTime;

    private String xml;

    private UserOrderProductsDto item;

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

    public OrderStatus getOrderStatus() {
        return orderStatus;
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

    public UserOrderProductsDto getItem() {

        try {
            StringReader reader = new StringReader(xml);
            JAXBContext context = JAXBContext.newInstance(UserOrderProductsDto.class);
            Unmarshaller un = context.createUnmarshaller();
            UserOrderProductsDto item = (UserOrderProductsDto) un.unmarshal(reader);
            return item;
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setItem(UserOrderProductsDto item) {
        this.item = item;
    }
}
