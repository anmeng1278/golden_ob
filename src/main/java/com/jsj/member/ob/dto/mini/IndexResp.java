package com.jsj.member.ob.dto.mini;

import com.jsj.member.ob.dto.api.activity.ActivityDto;
import com.jsj.member.ob.dto.api.activity.ActivityProductDto;
import com.jsj.member.ob.dto.api.product.ProductDto;
import com.jsj.member.ob.entity.Banner;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class IndexResp {


    @ApiModelProperty(value = "轮播图")
    private List<Banner> banners;

    @ApiModelProperty(value = "品质出行")
    List<ProductDto> qualityTravels;

    @ApiModelProperty(value = "爆款单品")
    List<ProductDto> hotProducts;

    @ApiModelProperty(value = "限时秒杀活动")
    ActivityDto secKills;

    @ApiModelProperty(value = "限时秒杀商品")
    List<ActivityProductDto> secKillProducts;


    @ApiModelProperty(value = "组合优惠")
    List<ActivityDto> setSales;


    public List<Banner> getBanners() {
        return banners;
    }

    public void setBanners(List<Banner> banners) {
        this.banners = banners;
    }

    public List<ProductDto> getQualityTravels() {
        return qualityTravels;
    }

    public void setQualityTravels(List<ProductDto> qualityTravels) {
        this.qualityTravels = qualityTravels;
    }

    public List<ProductDto> getHotProducts() {
        return hotProducts;
    }

    public void setHotProducts(List<ProductDto> hotProducts) {
        this.hotProducts = hotProducts;
    }

    public ActivityDto getSecKills() {
        return secKills;
    }

    public void setSecKills(ActivityDto secKills) {
        this.secKills = secKills;
    }

    public List<ActivityProductDto> getSecKillProducts() {
        return secKillProducts;
    }

    public void setSecKillProducts(List<ActivityProductDto> secKillProducts) {
        this.secKillProducts = secKillProducts;
    }

    public List<ActivityDto> getSetSales() {
        return setSales;
    }

    public void setSetSales(List<ActivityDto> setSales) {
        this.setSales = setSales;
    }
}
