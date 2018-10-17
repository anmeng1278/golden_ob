package com.jsj.member.ob.dto.api.cart;

import com.jsj.member.ob.dto.api.product.Product;
import io.swagger.models.auth.In;

import java.util.ArrayList;
import java.util.List;

public class GetCartProductsResp {

    private Integer carProductId;

    private Integer productId;

    private String openId;

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

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

}
