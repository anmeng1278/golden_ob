package com.jsj.member.ob.logic;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jsj.member.ob.dto.api.gift.GiftDto;
import com.jsj.member.ob.dto.api.product.ProductDto;
import com.jsj.member.ob.dto.api.product.ProductSpecDto;
import com.jsj.member.ob.dto.api.stock.StockDto;
import com.jsj.member.ob.dto.api.stock.StockFlowDto;
import com.jsj.member.ob.dto.api.stock.UseProductDto;
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
import java.util.stream.Collectors;

/**
 * 获取我的库存
 */
@Component
public class StockLogic extends BaseLogic {

    public static StockLogic stockLogic;


    @PostConstruct
    public void init() {
        stockLogic = this;
    }

    @Autowired
    StockService stockService;


    @Autowired
    GiftStockService giftStockService;

    @Autowired
    StockFlowService stockFlowService;

    //region (public) 获取我的库存 GetMyStock

    /**
     * 获取用户库存
     *
     * @param openId
     * @return
     */
    public static List<StockDto> GetStocks(String openId) {

        List<StockDto> stockDtos = StockLogic.GetStocks(openId, null, StockStatus.UNUSE);
        return stockDtos;

    }
    //endregion


    public static List<StockDto> GetStocks(int giftId) {

        List<StockDto> stockDtos = new ArrayList<>();
        Wrapper<GiftStock> wrapper = new EntityWrapper<>();
        wrapper.where("gift_id = {0} and delete_time is null", giftId);

        List<GiftStock> giftStocks = stockLogic.giftStockService.selectList(wrapper);
        if (giftStocks.size() == 0) {
            return stockDtos;
        }

        List<Integer> stockIds = giftStocks.stream().map(gf -> gf.getStockId()).collect(Collectors.toList());

        Wrapper<Stock> stockWrapper = new EntityWrapper<>();
        stockWrapper.in("stock_id", stockIds);
        stockWrapper.where("delete_time is null");

        List<Stock> stocks = stockLogic.stockService.selectList(stockWrapper);

        stocks.forEach(entity -> {
            StockDto stockDto = ToDto(entity);
            stockDtos.add(stockDto);
        });

        return stockDtos;

    }

    /**
     * 通过所选使用的编号获取库存信息
     *
     * @param openId
     * @param useProductDtos
     * @return
     */
    public static List<StockDto> GetStocks(String openId, List<UseProductDto> useProductDtos, boolean isShowGroup) {

        if (StringUtils.isBlank(openId)) {
            throw new TipException("参数不合法，用户openId为空");
        }

        List<StockDto> stockDtos = new ArrayList<>();
        if (useProductDtos.size() == 0) {
            return stockDtos;
        }

        //所选商品规格
        List<Integer> productSpecIds = useProductDtos.stream().map(st -> st.getsId()).collect(Collectors.toList());

        EntityWrapper<Stock> wrapper = new EntityWrapper<>();
        wrapper.where("open_id={0} and delete_time is null", openId);
        wrapper.where("status={0}", StockStatus.UNUSE.getValue());
        wrapper.in("product_spec_id", productSpecIds);
        wrapper.orderBy("create_time desc");

        //查询符合规格所有库存
        List<Stock> stocks = stockLogic.stockService.selectList(wrapper);
        List<StockDto> totalStockDtos = new ArrayList<>();

        //实体转换
        stocks.forEach(st -> {
            totalStockDtos.add(ToDto(st));
        });

        //在所选规格中筛选库存
        useProductDtos.forEach(up -> {

            //符合的库存
            List<StockDto> collect = totalStockDtos.stream().filter(exist -> exist.getProductId().equals(up.getpId()) &&
                    exist.getProductSpecId().equals(up.getsId())).collect(Collectors.toList());

            if (collect.size() < up.getNum()) {
                throw new TipException("所有商品库存不足，请重新选择");
            }


            if (isShowGroup) {
                //分组展示
                //选中其中一个库存，修改商品数
                StockDto current = collect.get(0);
                current.setNumber(up.getNum());

                stockDtos.add(current);
            } else {
                //单行展示
                for (int i = 0; i < up.getNum(); i++) {
                    StockDto current = collect.get(i);
                    stockDtos.add(current);
                }
            }

        });

        return stockDtos;

    }

    /**
     * 获取库存
     *
     * @param openId
     * @param stockType
     * @param stockStatus
     * @return
     */
    public static List<StockDto> GetStocks(String openId, StockType stockType, StockStatus stockStatus) {

        if (StringUtils.isBlank(openId)) {
            throw new TipException("参数不合法，用户openId为空");
        }

        List<StockDto> stockDtos = new ArrayList<>();

        EntityWrapper<Stock> stockWrapper = new EntityWrapper<>();
        stockWrapper.where("open_id={0} and delete_time is null", openId);

        if (stockStatus != null) {
            stockWrapper.where("status={0}", stockStatus.getValue());
        }

        if (stockType != null) {
            stockWrapper.where("type_id={0}", stockType.getValue());
        }
        stockWrapper.orderBy("create_time desc");

        List<Stock> stockList = stockLogic.stockService.selectList(stockWrapper);

        for (Stock stock : stockList) {

            StockDto stockDto = new StockDto();
            stockDto.setProductId(stock.getProductId());
            stockDto.setProductSpecId(stock.getProductSpecId());

            if (stockDtos.contains(stockDto)) {
                continue;
            }

            //获得库存中每样商品总量
            Long number = stockList.stream().filter(x -> x.getProductId().equals(stock.getProductId()) &&
                    x.getProductSpecId().equals(stock.getProductSpecId())).count();
            stockDto.setNumber(number.intValue());

            ProductSpecDto dto = ProductLogic.GetProductSpec(stock.getProductSpecId());
            stockDto.setProductSpecDto(dto);
            stockDto.setOpenId(openId);
            stockDto.setOrderId(stock.getOrderId());
            stockDto.setProductId(stock.getProductId());
            stockDto.setStockId(stock.getStockId());
            stockDto.setProductSpecId(stock.getProductSpecId());
            stockDto.setStockType(StockType.valueOf(stock.getTypeId()));

            WechatDto wechatDto = WechatLogic.GetWechat(openId);
            stockDto.setWechatDto(wechatDto);
            stockDto.setCreateTime(stock.getCreateTime());
            stockDto.setParentStockId(stock.getParentStockId());

            ProductDto productDto = ProductLogic.GetProduct(stock.getProductId());
            stockDto.setProductDto(productDto);

            EntityWrapper<GiftStock> wrapper = new EntityWrapper<>();
            wrapper.where("stock_id={0}", stock.getStockId());
            wrapper.orderBy("create_time desc");

            GiftStock giftStock = stockLogic.giftStockService.selectOne(wrapper);
            if (giftStock != null) {
                GiftDto giftDto = GiftLogic.GetGift(giftStock.getGiftId());
                stockDto.setGiftDto(giftDto);
            }
            stockDtos.add(stockDto);
        }
        return stockDtos;
    }


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
     *
     * @param stockId
     * @return
     */
    public static StockDto GetChild(int stockId) {

        Stock stock = stockLogic.stockService.selectOne(new EntityWrapper<Stock>().where("parent_stock_id={0}", stockId));

        if (stock != null) {
            return StockLogic.ToDto(stock);
        }
        return null;
    }

    //region (public) 获取库存流转 GetStockFlows


    /**
     * 获得库存取赠送信息
     *
     * @param parentStockId
     * @return
     */
    public static StockDto GetParent(int parentStockId) {

        Stock stock = stockLogic.stockService.selectOne(new EntityWrapper<Stock>().where("stock_id={0}", parentStockId));
        if (stock != null) {
            return StockLogic.GetStock(stock.getStockId());
        }
        return null;
    }

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

        Stock stock = stockLogic.stockService.selectById(stockId);

        StockDto stockDto = StockLogic.ToDto(stock);

        return stockDto;

    }

    /**
     * 获取库存数
     *
     * @param openId
     * @return
     */
    public static Integer GetStockCount(String openId) {

        if (StringUtils.isBlank(openId)) {
            return 0;
        }

        EntityWrapper<Stock> wrapper = new EntityWrapper<>();
        wrapper.where("open_id={0}", openId);

        return stockLogic.stockService.selectCount(wrapper);

    }

    /**
     * @param stock
     * @return
     */
    public static StockDto GetStockDto(StockDto stock, List<Integer> stockIds) {

        StockDto stockDto = new StockDto();

        ProductSpecDto productSpecDto = ProductLogic.GetProductSpec(stock.getProductSpecId());
        WechatDto wechatDto = WechatLogic.GetWechat(stock.getOpenId());
        stockDto.setWechatDto(wechatDto);

        EntityWrapper<GiftStock> wrapper = new EntityWrapper<>();
        wrapper.where("stock_id={0}", stock.getStockId());
        wrapper.orderBy("create_time desc");

        GiftStock giftStock = stockLogic.giftStockService.selectOne(wrapper);

        stockDto.setProductSpecDto(productSpecDto);

        if (stockIds.size() != 0 && stockIds != null) {
            //获得库存中每样商品总量
            EntityWrapper<Stock> productWrapper = new EntityWrapper<>();
            productWrapper.where("product_id={0}", stock.getProductId());
            productWrapper.in("stock_id", stockIds);
            int number = stockLogic.stockService.selectCount(productWrapper);
            stockDto.setNumber(number);
        } else {
            stockDto.setNumber(1);
        }
        stockDto.setOpenId(stock.getOpenId());
        stockDto.setOrderId(stock.getOrderId());
        stockDto.setProductId(stock.getProductId());

        stockDto.setStockId(stock.getStockId());
        stockDto.setProductSpecId(stock.getProductSpecId());
        stockDto.setStockStatus(stock.getStockStatus());
        stockDto.setStockType(stock.getStockType());
        stockDto.setCreateTime(stock.getCreateTime());

        if (stock.getParentStockId() > 0) {
            stockDto.setParentStockId(stock.getParentStockId());
        }

        if (giftStock != null) {
            GiftDto giftDto = GiftLogic.GetGift(giftStock.getGiftId());
            stockDto.setGiftDto(giftDto);
        }
        return stockDto;

    }


    /**
     * 实体转换
     *
     * @param stock
     * @return
     */
    public static StockDto ToDto(Stock stock) {

        StockDto stockDto = new StockDto();

        stockDto.setNumber(1);
        stockDto.setOpenId(stock.getOpenId());
        stockDto.setOrderId(stock.getOrderId());
        stockDto.setProductId(stock.getProductId());

        stockDto.setStockId(stock.getStockId());
        stockDto.setProductSpecId(stock.getProductSpecId());
        stockDto.setStockStatus(StockStatus.valueOf(stock.getStatus()));
        stockDto.setStockType(StockType.valueOf(stock.getTypeId()));
        stockDto.setCreateTime(stock.getCreateTime());

        if (stock.getParentStockId() != null) {
            stockDto.setParentStockId(stock.getParentStockId());
        }

        //商品信息
        ProductDto productDto = ProductLogic.GetProduct(stock.getProductId());
        stockDto.setProductDto(productDto);

        //商品规格
        ProductSpecDto productSpecDto = ProductLogic.GetProductSpec(stock.getProductSpecId());
        stockDto.setProductSpecDto(productSpecDto);

        //库存人
        WechatDto wechatDto = WechatLogic.GetWechat(stock.getOpenId());
        stockDto.setWechatDto(wechatDto);

        EntityWrapper<GiftStock> wrapper = new EntityWrapper<>();
        wrapper.where("stock_id = {0}", stock.getStockId());
        wrapper.orderBy("create_time desc");
        GiftStock giftStock = stockLogic.giftStockService.selectOne(wrapper);
        //赠送人
        if (giftStock != null) {
            GiftDto giftDto = GiftLogic.GetGift(giftStock.getGiftId());
            stockDto.setGiftDto(giftDto);
        }

        return stockDto;
    }

}
