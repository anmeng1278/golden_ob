package com.jsj.member.ob.dto.thirdParty;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateGoldenCardRequ {

    public CreateGoldenCardRequ() {
        this.requestHead = new RequestHead();
        this.requestBody = new RequestBody();
    }

    @JsonProperty
    private RequestHead requestHead;

    @JsonProperty
    @JSONField(name = "RequestHead")
    public RequestHead getRequestHead() {
        return requestHead;
    }

    @JsonProperty
    public void setRequestHead(RequestHead requestHead) {
        this.requestHead = requestHead;
    }

    @JsonProperty
    private CreateGoldenCardRequ.RequestBody requestBody;

    @JsonProperty
    @JSONField(name = "RequestBody")
    public RequestBody getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
    }


    public class RequestBody{

        @JSONField(name = "CardType")
        private int CardType;

        @JSONField(name = "MemberMobile")
        private String MemberMobile;

        @JSONField(name = "MemberName")
        private String  MemberName;

        @JSONField(name = "MemberIdNumber")
        private String MemberIdNumber;

        @JSONField(name = "MemberIDType")
        private String MemberIDType;

        @JSONField(name = "SalePrice")
        private String SalePrice;

        public int getCardType() {
            return CardType;
        }

        public void setCardType(int cardType) {
            CardType = cardType;
        }

        public String getMemberMobile() {
            return MemberMobile;
        }

        public void setMemberMobile(String memberMobile) {
            MemberMobile = memberMobile;
        }

        public String getMemberName() {
            return MemberName;
        }

        public void setMemberName(String memberName) {
            MemberName = memberName;
        }

        public String getMemberIdNumber() {
            return MemberIdNumber;
        }

        public void setMemberIdNumber(String memberIdNumber) {
            MemberIdNumber = memberIdNumber;
        }

        public String getMemberIDType() {
            return MemberIDType;
        }

        public void setMemberIDType(String memberIDType) {
            MemberIDType = memberIDType;
        }

        public String getSalePrice() {
            return SalePrice;
        }

        public void setSalePrice(String salePrice) {
            SalePrice = salePrice;
        }
    }

}
