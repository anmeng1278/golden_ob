package com.jsj.member.ob.dto.mini;

import com.jsj.member.ob.dto.api.stock.StockDto;
import com.jsj.member.ob.entity.Banner;
import com.jsj.member.ob.entity.DeliveryStock;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class StocksResp {

    //库存列表
    @ApiModelProperty(value = "库存列表", required = true)
    private List<StockDto> stockDtos;

    //分享失败的礼包
    @ApiModelProperty(value = "分享失败的礼包", required = true)
    private int unShareCount;

    //未使用的活动码
    @ApiModelProperty(value = "未使用的活动码", required = true)
    private List<DeliveryStock> unUsedActivityCodes;

    //库存轮播图
    private List<Banner> banners;

    public List<StockDto> getStockDtos() {
        return stockDtos;
    }

    public void setStockDtos(List<StockDto> stockDtos) {
        this.stockDtos = stockDtos;
    }

    public int getUnShareCount() {
        return unShareCount;
    }

    public void setUnShareCount(int unShareCount) {
        this.unShareCount = unShareCount;
    }

    public List<DeliveryStock> getUnUsedActivityCodes() {
        return unUsedActivityCodes;
    }

    public void setUnUsedActivityCodes(List<DeliveryStock> unUsedActivityCodes) {
        this.unUsedActivityCodes = unUsedActivityCodes;
    }

    public List<Banner> getBanners() {
        return banners;
    }

    public void setBanners(List<Banner> banners) {
        this.banners = banners;
    }
}
