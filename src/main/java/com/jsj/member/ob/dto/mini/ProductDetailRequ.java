package com.jsj.member.ob.dto.mini;

import com.jsj.member.ob.enums.ActivityType;
import io.swagger.annotations.ApiModelProperty;

public class ProductDetailRequ {

    @ApiModelProperty(value = "会员编号，必填", required = true)
    private int jsjId;

    @ApiModelProperty(value = "unionId，必填", required = true)
    private String unionId;

    @ApiModelProperty(value = "活动编号，活动商品详情必填")
    private int activityId;

    @ApiModelProperty(value = "商品编号，普通商品详情必填")
    private int productId;

    @ApiModelProperty(value = "商品规格编号，秒杀兑换商品详情必填")
    private int productSpecId;

    @ApiModelProperty(value = "活动类型，0:普通，20:秒杀，30:组合，40:会员权益兑换")
    private ActivityType activityType;

    public int getJsjId() {
        return jsjId;
    }

    public void setJsjId(int jsjId) {
        this.jsjId = jsjId;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
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


    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }
}
