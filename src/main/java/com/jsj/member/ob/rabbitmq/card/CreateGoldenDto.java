package com.jsj.member.ob.rabbitmq.card;

import com.jsj.member.ob.rabbitmq.BaseDto;

public class CreateGoldenDto extends BaseDto {

    private int deliveryId;

    private int cardType;

    private String memberMobile;

    private String memberName;

    private String memberIdNumber;

    private String memberIDType;

    private String salePrice;

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    public String getMemberMobile() {
        return memberMobile;
    }

    public void setMemberMobile(String memberMobile) {
        this.memberMobile = memberMobile;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberIdNumber() {
        return memberIdNumber;
    }

    public void setMemberIdNumber(String memberIdNumber) {
        this.memberIdNumber = memberIdNumber;
    }

    public String getMemberIDType() {
        return memberIDType;
    }

    public void setMemberIDType(String memberIDType) {
        this.memberIDType = memberIDType;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public int getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(int deliveryId) {
        this.deliveryId = deliveryId;
    }
}
