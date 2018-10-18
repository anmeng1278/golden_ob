package com.jsj.member.ob.logic;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jsj.member.ob.dto.api.product.ProductDto;
import com.jsj.member.ob.dto.api.stock.GetMyStockRequ;
import com.jsj.member.ob.dto.api.stock.GetMyStockResp;
import com.jsj.member.ob.dto.api.stock.StockDto;
import com.jsj.member.ob.entity.Stock;
import com.jsj.member.ob.enums.StockStatus;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.service.StockService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取我的库存
 */
@Component
public class StockLogic {

    public static StockLogic stockLogic;

    @PostConstruct
    public void init() {
        stockLogic = this;
        stockLogic.stockService = this.stockService;

    }

    @Autowired
    StockService stockService;


    /**
     * 获取我的库存
     * @param requ
     * @return
     */
    public static GetMyStockResp GetMyStock(GetMyStockRequ requ){

        if(StringUtils.isBlank(requ.getBaseRequ().getOpenId())){
            throw new TipException("参数不合法，用户openId为空");
        }

        GetMyStockResp resp = new GetMyStockResp();

        List<StockDto> stockDtoList = new ArrayList<>();

        EntityWrapper<Stock> productWrapper = new EntityWrapper<>();

        //查询该用户下所有库存
        EntityWrapper<Stock> stockWrapper = new EntityWrapper<>();
        stockWrapper.where("open_id={0} and status = {0} and delete_time is null",requ.getBaseRequ().getOpenId(), StockStatus.UNUSE.getValue());
        List<Stock> stockList = stockLogic.stockService.selectList(stockWrapper);
       if(stockList.size() == 0){
            return resp;
        }
        StockDto stockDto = new StockDto();
        for (Stock stock : stockList) {
            //获得库存中每样商品总量
            productWrapper.where("product_id={0}",stock.getProductId());
            int number = stockLogic.stockService.selectCount(productWrapper);
            ProductDto productDto = ProductLogic.GetProduct(stock.getProductId());
            stockDto.setProductDto(productDto);
            stockDto.setOpenId(requ.getBaseRequ().getOpenId());
            stockDto.setOrderId(stock.getOrderId());
            stockDto.setProductId(stock.getProductId());
            stockDto.setStockId(stock.getStockId());
            stockDto.setNumber(number);
            stockDtoList.add(stockDto);
            resp.setStockDtoList(stockDtoList);
        }
        return resp;
    }

}
