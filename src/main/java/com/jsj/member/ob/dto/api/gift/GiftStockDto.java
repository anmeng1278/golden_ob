package com.jsj.member.ob.dto.api.gift;

import com.jsj.member.ob.dto.api.stock.StockDto;

public class GiftStockDto {

    /**
     * 主键
     */
    private Integer giftStockId;
    /**
     * 赠送表主键
     */
    private Integer giftId;
    /**
     * 用户库存表主键
     */
    private Integer stockId;

    /**
     * 领取人库存
     */
    private StockDto receviedStockDto;


    public Integer getGiftStockId() {
        return giftStockId;
    }

    public void setGiftStockId(Integer giftStockId) {
        this.giftStockId = giftStockId;
    }

    public Integer getGiftId() {
        return giftId;
    }

    public void setGiftId(Integer giftId) {
        this.giftId = giftId;
    }

    public Integer getStockId() {
        return stockId;
    }

    public void setStockId(Integer stockId) {
        this.stockId = stockId;
    }


    public StockDto getReceviedStockDto() {
        return receviedStockDto;
    }

    public void setReceviedStockDto(StockDto receviedStockDto) {
        this.receviedStockDto = receviedStockDto;
    }
}
