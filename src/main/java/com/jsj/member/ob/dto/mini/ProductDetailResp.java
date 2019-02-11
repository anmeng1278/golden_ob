package com.jsj.member.ob.dto.mini;

import com.jsj.member.ob.dto.api.activity.ActivityDto;
import com.jsj.member.ob.dto.api.activity.ActivityProductDto;
import com.jsj.member.ob.dto.api.coupon.WechatCouponDto;
import com.jsj.member.ob.dto.api.product.ProductDto;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class ProductDetailResp {

    @ApiModelProperty(value = "商品信息")
    private ProductDto productDto;

    @ApiModelProperty(value = "商品可用优惠券")
    private List<WechatCouponDto> wechatCouponDtos;

    @ApiModelProperty(value = "剩余库存")
    private Integer stockCount;

    @ApiModelProperty(value = "活动信息")
    private ActivityDto activityDto;

    @ApiModelProperty(value = "活动中的商品")
    private List<ActivityProductDto> activityProductDtos;

    @ApiModelProperty(value = "售价")
    private double salePrice;

    @ApiModelProperty(value = "图片地址")
    private String imgUrl;

    @ApiModelProperty(value = "图片地址")
    private int flag;

    @ApiModelProperty(value = "活动编号")
    private int activityId;

    @ApiModelProperty(value = "商品编号")
    private int productId;

    @ApiModelProperty(value = "规格编号")
    private int productSpecId;

    @ApiModelProperty(value = "账户余额")
    private double balance;


    public ProductDto getProductDto() {
        return productDto;
    }

    public void setProductDto(ProductDto productDto) {
        this.productDto = productDto;
    }

    public List<WechatCouponDto> getWechatCouponDtos() {
        return wechatCouponDtos;
    }

    public void setWechatCouponDtos(List<WechatCouponDto> wechatCouponDtos) {
        this.wechatCouponDtos = wechatCouponDtos;
    }

    public Integer getStockCount() {
        return stockCount;
    }

    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }

    public ActivityDto getActivityDto() {
        return activityDto;
    }

    public void setActivityDto(ActivityDto activityDto) {
        this.activityDto = activityDto;
    }

    public List<ActivityProductDto> getActivityProductDtos() {
        return activityProductDtos;
    }

    public void setActivityProductDtos(List<ActivityProductDto> activityProductDtos) {
        this.activityProductDtos = activityProductDtos;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
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

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
