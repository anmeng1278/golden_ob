package com.jsj.member.ob.logic;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jsj.member.ob.dto.api.gift.GiftDto;
import com.jsj.member.ob.dto.api.product.ProductSpecDto;
import com.jsj.member.ob.dto.api.stock.GetMyStockRequ;
import com.jsj.member.ob.dto.api.stock.GetMyStockResp;
import com.jsj.member.ob.dto.api.stock.StockDto;
import com.jsj.member.ob.dto.api.stock.StockFlowDto;
import com.jsj.member.ob.dto.api.wechat.WechatDto;
import com.jsj.member.ob.entity.GiftStock;
import com.jsj.member.ob.entity.Stock;
import com.jsj.member.ob.entity.StockFlow;
import com.jsj.member.ob.enums.StockFlowType;
import com.jsj.member.ob.enums.StockStatus;
import com.jsj.member.ob.enums.StockType;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.service.GiftStockService;
import com.jsj.member.ob.service.StockFlowService;
import com.jsj.member.ob.service.StockService;
import com.jsj.member.ob.utils.DateUtils;
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
        stockLogic.giftStockService = this.giftStockService;

    }

    @Autowired
    StockService stockService;


    @Autowired
    GiftStockService giftStockService;

    @Autowired
    StockFlowService stockFlowService;

    //region (public) 获取我的库存 GetMyStock

    /**
     * 获取我的库存
     *
     * @param requ
     * @return
     */
    public static GetMyStockResp GetMyStock(GetMyStockRequ requ) {

        if (StringUtils.isBlank(requ.getBaseRequ().getOpenId())) {
            throw new TipException("参数不合法，用户openId为空");
        }

        GetMyStockResp resp = new GetMyStockResp();

        List<StockDto> stockDtoList = new ArrayList<>();

        EntityWrapper<Stock> productWrapper = new EntityWrapper<>();

        //查询该用户下所有库存
        EntityWrapper<Stock> stockWrapper = new EntityWrapper<>();
        stockWrapper.where("open_id={0} and status = {1} and delete_time is null", requ.getBaseRequ().getOpenId(), StockStatus.UNUSE.getValue());
        List<Stock> stockList = stockLogic.stockService.selectList(stockWrapper);
        if (stockList.size() == 0) {
            return resp;
        }
        StockDto stockDto = new StockDto();
        for (Stock stock : stockList) {
            //获得库存中每样商品总量
            productWrapper.where("product_id={0} and open_id={1}", stock.getProductId(), stock.getOpenId());
            int number = stockLogic.stockService.selectCount(productWrapper);

            ProductSpecDto dto = ProductLogic.GetProductSpec(stock.getProductSpecId());
            stockDto.setProductSpecDto(dto);
            stockDto.setOpenId(requ.getBaseRequ().getOpenId());
            stockDto.setOrderId(stock.getOrderId());
            stockDto.setProductId(stock.getProductId());
            stockDto.setStockId(stock.getStockId());
            stockDto.setNumber(number);
            stockDtoList.add(stockDto);
            resp.setStockDtos(stockDtoList);
        }
        return resp;
    }
    //endregion

    //region (public) 订单支付成功添加库存 AddOrderStock

    /**
     * 订单支付成功添加库存
     *
     * @param stocks
     */
    public static void AddOrderStock(List<Stock> stocks) {

        stocks.forEach(st -> {
            st.setTypeId(StockType.BUY.getValue());
            st.setStatus(StockStatus.UNUSE.getValue());
            st.setCreateTime(DateUtils.getCurrentUnixTime());
            st.setUpdateTime(DateUtils.getCurrentUnixTime());
        });
        stockLogic.stockService.insertBatch(stocks);


        //库存日志
        stocks.forEach(st -> {
            StockFlow stockFlow = new StockFlow();
            stockFlow.setOpenId(st.getOpenId());
            stockFlow.setCreateTime(DateUtils.getCurrentUnixTime());
            stockFlow.setUpdateTime(DateUtils.getCurrentUnixTime());
            stockFlow.setStockId(st.getStockId());
            StockLogic.AddStockFlow(stockFlow, StockFlowType.BUY);
        });

    }
    //endregion

    //region (public) 领取成功添加库存 AddGiftStock

    /**
     * 领取成功添加库存
     *
     * @param stocks
     */
    public static void AddGiftStock(List<Stock> stocks) {

        stocks.forEach(st -> {
            st.setTypeId(StockType.GIFT.getValue());
            st.setStatus(StockStatus.UNUSE.getValue());
            st.setCreateTime(DateUtils.getCurrentUnixTime());
            st.setUpdateTime(DateUtils.getCurrentUnixTime());
        });
        stockLogic.stockService.insertBatch(stocks);

        //库存日志
        stocks.forEach(st -> {
            StockFlow stockFlow = new StockFlow();
            stockFlow.setOpenId(st.getOpenId());
            stockFlow.setParentStockId(st.getParentStockId());
            stockFlow.setCreateTime(DateUtils.getCurrentUnixTime());
            stockFlow.setUpdateTime(DateUtils.getCurrentUnixTime());
            stockFlow.setStockId(st.getStockId());
            StockLogic.AddStockFlow(stockFlow, StockFlowType.RECEVIED);
        });

    }
    //endregion

    //region (public) 添加库存流转 AddStockFlow

    /**
     * 添加库存流转
     *
     * @param stockFlow
     * @param stockFlowType
     */
    public static void AddStockFlow(StockFlow stockFlow, StockFlowType stockFlowType) {

        stockFlow.setFlowName(stockFlowType.getMessage());
        stockFlow.setCreateTime(DateUtils.getCurrentUnixTime());
        stockFlow.setUpdateTime(DateUtils.getCurrentUnixTime());

        stockLogic.stockFlowService.insert(stockFlow);
    }
    //endregion

    //region (public) 获取父编号 GetParentStockIds

    /**
     * 获取父编号
     *
     * @param stockId
     * @return
     */
    public static List<Integer> GetParentStockIds(int stockId) {

        List<Integer> stockIds = new ArrayList<>();
        Stock stock = stockLogic.stockService.selectById(stockId);
        if (stock.getParentStockId() != null) {
            stockIds.add(stock.getParentStockId());
            stockIds.addAll(StockLogic.GetParentStockIds(stock.getParentStockId()));
        }
        return stockIds;

    }
    //endregion

    //region (public) 获取库存子编号 GetChildStockIds

    /**
     * 获取库存子编号
     *
     * @param stockId
     * @return
     */
    public static List<Integer> GetChildStockIds(int stockId) {

        List<Integer> stockIds = new ArrayList<>();
        List<Stock> stocks = stockLogic.stockService.selectList(new EntityWrapper<Stock>().where("parent_stock_id={0}", stockId));

        stocks.forEach(st -> {
            stockIds.add(st.getStockId());
            stockIds.addAll(StockLogic.GetChildStockIds(st.getStockId()));
        });

        return stockIds;

    }
    //endregion

    /**
     * 获库存取领取信
     * @param stockId
     * @return
     */
    public static StockDto GetChild(int stockId) {
        Stock stock = stockLogic.stockService.selectOne(new EntityWrapper<Stock>().where("parent_stock_id={0}", stockId));
        if (stock != null) {
            return StockLogic.GetStock(stock.getStockId());
        }
        return null;
    }

    //region (public) 获取库存流转 GetStockFlows

    /**
     * 获取库存流转
     *
     * @param stockId
     * @param getAll
     * @return
     */
    public static List<StockFlowDto> GetStockFlows(int stockId, Boolean getAll) {

        List<StockFlowDto> stockFlowDtos = new ArrayList<>();

        List<Integer> stockIds = new ArrayList<>();
        if (getAll) {
            stockIds.addAll(StockLogic.GetParentStockIds(stockId));
        }
        stockIds.addAll(StockLogic.GetChildStockIds(stockId));
        stockIds.add(stockId);

        Wrapper<StockFlow> wrapper = new EntityWrapper<StockFlow>().in("stock_id", stockIds);
        wrapper.orderBy("create_time asc");

        List<StockFlow> stockFlows = stockLogic.stockFlowService.selectList(wrapper);
        stockFlows.forEach(sf -> {

            WechatDto wechatDto = WechatLogic.GetWechat(sf.getOpenId());
            StockFlowDto dto = new StockFlowDto();

            dto.setStockId(sf.getStockId());
            dto.setParentStockId(sf.getParentStockId());
            dto.setOpenId(sf.getOpenId());
            dto.setCreateTime(sf.getCreateTime());
            dto.setNickName(wechatDto.getNickname());
            dto.setFlowName(sf.getFlowName());
            dto.setRemark(sf.getRemark());

            stockFlowDtos.add(dto);

        });

        return stockFlowDtos;

    }
    //endregion


    /**
     * 获取库存详情
     *
     * @param stockId
     * @return
     */
    public static StockDto GetStock(int stockId) {

        StockDto stockDto = new StockDto();
        Stock stock = stockLogic.stockService.selectById(stockId);

        ProductSpecDto productSpecDto = ProductLogic.GetProductSpec(stock.getProductSpecId());

        EntityWrapper<GiftStock> wrapper = new EntityWrapper<>();
        wrapper.where("stock_id={0}", stockId);
        wrapper.orderBy("create_time desc");

        GiftStock giftStock = stockLogic.giftStockService.selectOne(wrapper);

        stockDto.setProductSpecDto(productSpecDto);
        stockDto.setNumber(1);
        stockDto.setOpenId(stock.getOpenId());
        stockDto.setOrderId(stock.getOrderId());
        stockDto.setProductId(stock.getProductId());

        stockDto.setStockId(stock.getStockId());
        stockDto.setProductSpecId(stock.getProductSpecId());
        stockDto.setStockStatus(StockStatus.valueOf(stock.getStatus()));
        stockDto.setStockType(StockType.valueOf(stock.getTypeId()));
        stockDto.setCreateTime(stock.getCreateTime());

        if (giftStock != null) {
            GiftDto giftDto = GiftLogic.GetGift(giftStock.getGiftId());
            stockDto.setGiftDto(giftDto);
        }
        return stockDto;

    }

}
