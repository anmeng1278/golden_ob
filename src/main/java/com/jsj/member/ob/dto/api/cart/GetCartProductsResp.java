package com.jsj.member.ob.dto.api.cart;



public class GetCartProductsResp {

    private Integer carProductId;

    private Integer productId;

    private Integer number;

    public Integer getCarProductId() {
        return carProductId;
    }

    public void setCarProductId(Integer carProductId) {
        this.carProductId = carProductId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
