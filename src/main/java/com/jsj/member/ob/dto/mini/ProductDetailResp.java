package com.jsj.member.ob.dto.mini;

import com.jsj.member.ob.dto.api.activity.ActivityDto;
import com.jsj.member.ob.dto.api.activity.ActivityProductDto;
import com.jsj.member.ob.dto.api.coupon.WechatCouponDto;
import com.jsj.member.ob.dto.api.product.ProductDto;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class ProductDetailResp {

    @ApiModelProperty(value = "商品信息")
    private ProductDto product;

    @ApiModelProperty(value = "商品可用优惠券")
    private  List<WechatCouponDto> coupons;

    @ApiModelProperty(value = "剩余库存")
    private Integer stockCount;


    @ApiModelProperty(value = "活动信息")
    private ActivityDto activity;

    @ApiModelProperty(value = "活动中的商品")
    private List<ActivityProductDto> activityProducts;


    public List<WechatCouponDto> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<WechatCouponDto> coupons) {
        this.coupons = coupons;
    }

    public Integer getStockCount() {
        return stockCount;
    }

    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }


    public ActivityDto getActivity() {
        return activity;
    }

    public void setActivity(ActivityDto activity) {
        this.activity = activity;
    }

    public List<ActivityProductDto> getActivityProducts() {
        return activityProducts;
    }

    public void setActivityProducts(List<ActivityProductDto> activityProducts) {
        this.activityProducts = activityProducts;
    }

    public ProductDto getProduct() {
        return product;
    }

    public void setProduct(ProductDto product) {
        this.product = product;
    }
}
