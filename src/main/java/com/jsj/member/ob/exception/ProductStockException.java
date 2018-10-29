package com.jsj.member.ob.exception;

import com.jsj.member.ob.enums.ActivityType;

/**
 * 系统致命异常
 */
public class ProductStockException extends RuntimeException {

    private Integer productId;
    private Integer productSpecId;

    private ActivityType activityType;
    private Integer orderId;

    private int number;
    private int stock;

    public ProductStockException() {
    }

    public ProductStockException(String message) {
        super(message);
    }

    public ProductStockException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProductStockException(Throwable cause) {
        super(cause);
    }



    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getProductSpecId() {
        return productSpecId;
    }

    public void setProductSpecId(Integer productSpecId) {
        this.productSpecId = productSpecId;
    }


    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }


    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
}