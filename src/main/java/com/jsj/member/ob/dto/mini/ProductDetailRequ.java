package com.jsj.member.ob.dto.mini;

import io.swagger.annotations.ApiModelProperty;

public class ProductDetailRequ {

    @ApiModelProperty(value = "会员编号，可为空")
    private int jsjId;

    @ApiModelProperty(value = "openId，必填", required = true)
    private String openId;

    @ApiModelProperty(value = "活动编号，活动商品详情必填")
    private int activityId;

    @ApiModelProperty(value = "商品编号，普通商品详情必填")
    private int productId;

    @ApiModelProperty(value = "商品规格编号，秒杀兑换商品详情必填")
    private int productSpecId;

    @ApiModelProperty(value = "活动类型，0:普通，20:秒杀，30:组合，40:会员权益兑换")
    private int activityType;

    public int getJsjId() {
        return jsjId;
    }

    public void setJsjId(int jsjId) {
        this.jsjId = jsjId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getProductSpecId() {
        return productSpecId;
    }

    public void setProductSpecId(int productSpecId) {
        this.productSpecId = productSpecId;
    }

    public int getActivityType() {
        return activityType;
    }

    public void setActivityType(int activityType) {
        this.activityType = activityType;
    }
}
