package com.jsj.member.ob.dto.api.stock;

import java.util.List;

public class GetMyStockResp {

    List<StockDto> stockDtoList;

    public List<StockDto> getStockDtoList() {
        return stockDtoList;
    }

    public void setStockDtoList(List<StockDto> stockDtoList) {
        this.stockDtoList = stockDtoList;
    }


}
