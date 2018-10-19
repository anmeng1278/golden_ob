package com.jsj.member.ob.exception;

import com.jsj.member.ob.enums.ActivityType;

/**
 * 活动库存不足异常
 */
public class ActivityStockException extends RuntimeException {

    private Integer activityId;
    private Integer activytyOrderId;

    private ActivityType activityType;

    private int number;
    private int stock;

    public ActivityStockException() {
    }

    public ActivityStockException(String message) {
        super(message);
    }

    public ActivityStockException(String message, Throwable cause) {
        super(message, cause);
    }

    public ActivityStockException(Throwable cause) {
        super(cause);
    }


    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getActivytyOrderId() {
        return activytyOrderId;
    }

    public void setActivytyOrderId(Integer activytyOrderId) {
        this.activytyOrderId = activytyOrderId;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
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
}