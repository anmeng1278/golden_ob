package com.jsj.member.ob.dto.api.order;

public class OrderProduct {

    /**
     * 商品编号
     */
    private int productId;

    /**
     * 购买数据
     */
    private int number;

    /**
     * 尺寸型号
     */
    private int productSizeId;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getProductSizeId() {
        return productSizeId;
    }

    public void setProductSizeId(int productSizeId) {
        this.productSizeId = productSizeId;
    }
}
