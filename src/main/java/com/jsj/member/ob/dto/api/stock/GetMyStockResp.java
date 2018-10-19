package com.jsj.member.ob.dto.api.stock;

import java.util.List;

public class GetMyStockResp {

    private List<StockDto> stockDtos;

    public List<StockDto> getStockDtos() {
        return stockDtos;
    }

    public void setStockDtos(List<StockDto> stockDtos) {
        this.stockDtos = stockDtos;
    }
}
