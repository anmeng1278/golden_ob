package com.jsj.member.ob.logic;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.dto.api.gift.*;
import com.jsj.member.ob.entity.Gift;
import com.jsj.member.ob.entity.GiftStock;
import com.jsj.member.ob.entity.Stock;
import com.jsj.member.ob.entity.StockFlow;
import com.jsj.member.ob.enums.GiftShareType;
import com.jsj.member.ob.enums.GiftStatus;
import com.jsj.member.ob.enums.StockFlowType;
import com.jsj.member.ob.enums.StockStatus;
import com.jsj.member.ob.exception.FatalException;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.service.GiftService;
import com.jsj.member.ob.service.GiftStockService;
import com.jsj.member.ob.service.StockService;
import com.jsj.member.ob.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class GiftLogic {

    public static GiftLogic giftLogic;

    @Autowired
    GiftService giftService;

    @Autowired
    GiftStockService giftStockService;

    @Autowired
    StockService stockService;

    @PostConstruct
    public void init() {
        giftLogic = this;
        giftLogic.giftService = this.giftService;
        giftLogic.giftStockService = this.giftStockService;
        giftLogic.stockService = this.stockService;
    }


    //region (public) 创建赠送记录 CreateGift

    /**
     * 创建赠送记录
     *
     * @param requ
     * @return
     */
    public static CreateGiftResp CreateGift(CreateGiftRequ requ) {

        CreateGiftResp resp = new CreateGiftResp();

        if (StringUtils.isBlank(requ.getBaseRequ().getOpenId())) {
            throw new TipException("用户编号不能为空");
        }
        if (requ.getGiftProductDtos() == null || requ.getGiftProductDtos().isEmpty()) {
            throw new TipException("请选择赠送的商品");
        }
        if (requ.getGiftShareType() == null) {
            throw new TipException("请输入分享类型");
        }
        if (StringUtils.isBlank(requ.getBlessings())) {
            throw new TipException("请输入祝福语");
        }

        //用户编号
        String openId = requ.getBaseRequ().getOpenId();

        //库存编号
        List<Stock> stocks = new ArrayList<>();

        for (GiftProductDto dto : requ.getGiftProductDtos()) {

            EntityWrapper<Stock> wrapper = new EntityWrapper<>();

            wrapper.where("open_id={0}", openId);
            wrapper.where("status={0}", StockStatus.UNUSE.getValue());
            wrapper.where("product_id={0} and product_spec_id={1}", dto.getProductId(), dto.getProductSpecId());
            wrapper.orderBy("create_time asc");

            //待赠送列表
            List<Stock> currentStocks = giftLogic.stockService.selectList(wrapper);

            int number = dto.getNumber();
            if (number > currentStocks.size()) {
                number = currentStocks.size();
            }

            for (int i = 0; i < number; i++) {
                Stock st = currentStocks.get(i);
                stocks.add(st);
            }

        }
        if (stocks.isEmpty()) {
            throw new TipException("没有可赠送的库存信息");
        }

        Gift gift = new Gift();

        gift.setBlessings(requ.getBlessings());
        gift.setOpenId(openId);
        gift.setRemarks(requ.getRemarks());
        gift.setShareType(requ.getGiftShareType().getValue());
        gift.setStatus(GiftStatus.UNSHARE.getValue());

        gift.setGiftUniqueCode(com.jsj.member.ob.utils.StringUtils.UUID32());
        gift.setExpiredTime(DateUtils.getCurrentUnixTime() + Constant.GIFT_EXPIRED_TIME);
        gift.setCreateTime(DateUtils.getCurrentUnixTime());
        gift.setUpdateTime(DateUtils.getCurrentUnixTime());

        giftLogic.giftService.insert(gift);

        for (Stock st : stocks) {

            //添加赠送记录
            GiftStock gs = new GiftStock();
            gs.setGiftId(gift.getGiftId());
            gs.setStockId(st.getStockId());
            gs.setCreateTime(DateUtils.getCurrentUnixTime());
            gs.setUpdateTime(DateUtils.getCurrentUnixTime());
            giftLogic.giftStockService.insert(gs);

            //更新库存状态
            st.setStatus(StockStatus.GIVED.getValue());
            st.setUpdateTime(DateUtils.getCurrentUnixTime());
            giftLogic.stockService.updateById(st);


            //库存日志
            StockFlow stockFlow = new StockFlow();
            stockFlow.setOpenId(st.getOpenId());
            stockFlow.setCreateTime(DateUtils.getCurrentUnixTime());
            stockFlow.setUpdateTime(DateUtils.getCurrentUnixTime());
            stockFlow.setStockId(st.getStockId());
            StockLogic.AddStockFlow(stockFlow, StockFlowType.GIVING);


        }

        resp.setGiftId(gift.getGiftId());
        resp.setGiftUniqueCode(gift.getGiftUniqueCode());

        return resp;

    }
    //endregion

    //region (public) 获取赠送信息 selectByUniqueCode

    /**
     * 获取赠送信息
     *
     * @param giftUniqueCode
     * @return
     */
    public static Gift selectByUniqueCode(String giftUniqueCode) {
        if (StringUtils.isBlank(giftUniqueCode)) {
            throw new TipException("赠送编号不能为空");
        }
        Gift gift = giftLogic.giftService.selectOne(new EntityWrapper<Gift>().where("gift_unique_code={0}", giftUniqueCode));
        if (gift == null) {
            throw new TipException("没有找到分享信息");
        }
        return gift;
    }
    //endregion

    //region (public) 领取礼物 ReceivedGift

    /**
     * 领取礼物
     *
     * @param requ
     * @return
     */
    public static ReceivedGiftResp ReceivedGift(ReceivedGiftRequ requ) {

        ReceivedGiftResp resp = new ReceivedGiftResp();

        if (StringUtils.isBlank(requ.getGiftUniqueCode())) {
            throw new TipException("赠送编号不能为空");
        }
        if (StringUtils.isBlank(requ.getBaseRequ().getOpenId())) {
            throw new TipException("用户编号不能为空");
        }

        //当前领取人
        String openId = requ.getBaseRequ().getOpenId();

        Gift gift = GiftLogic.selectByUniqueCode(requ.getGiftUniqueCode());
        int giftId = gift.getGiftId();

        if (gift.getStatus() == GiftStatus.CANCEL.getValue()) {
            throw new TipException("用户已取消分享");
        }
        if (gift.getStatus() == GiftStatus.BROUGHTOUT.getValue()) {
            throw new TipException("来晚了，礼物被领完啦");
        }

        //可领取状态
        //GiftStatus.UNSHARE
        //GiftStatus.SHARED
        //GiftStatus.DRAWING
        if (gift.getDeleteTime() != null) {
            throw new TipException("没有找到分享信息");
        }
        if (gift.getOpenId().equals(openId)) {
            throw new TipException("不能领取自己发出的礼物");
        }

        //待领取的礼物
        //没有被领取过，当前状态已赠送
        List<GiftStock> giftStocks = GiftLogic.GetUnReceivedGiftstocks(gift.getGiftId());
        if (giftStocks.isEmpty()) {
            throw new TipException("来晚了，礼物被领完啦");
        }

        //重复领取判断
        EntityWrapper<Stock> stockWrapper = new EntityWrapper<>();
        stockWrapper.where("open_id={0}", openId);
        stockWrapper.where("exists( select * from _gift_stock as gs where gs.gift_stock_id = _stock.gift_stock_id and gs.gift_id = {0} )", giftId);

        int receivedTimes = giftLogic.stockService.selectCount(stockWrapper);
        if (receivedTimes > 0) {
            throw new TipException("礼物只能领取一次");
        }

        //分享方式
        GiftShareType giftShareType = GiftShareType.valueOf(gift.getShareType());

        //允许领取库存
        List<GiftStock> allowGiftStocks = new ArrayList<>();
        switch (giftShareType) {

            //全部领取
            case FRIEND:
                allowGiftStocks.addAll(giftStocks);
                break;

            //随机取一个领取
            case GROUP:
                Random random = new Random();
                int r = random.nextInt(giftStocks.size());
                allowGiftStocks.add(giftStocks.get(r));
                break;

            default:
                throw new FatalException("未知的枚举类型");
        }

        List<Stock> getStocks = new ArrayList<>();

        for (GiftStock gs : allowGiftStocks) {

            //获取原库存记录，更新状态
            Stock stock = giftLogic.stockService.selectById(gs.getStockId());
            stock.setStatus(StockStatus.RECEIVED.getValue());
            stock.setUpdateTime(DateUtils.getCurrentUnixTime());

            giftLogic.stockService.updateById(stock);

            //库存日志
            StockFlow stockFlow = new StockFlow();
            stockFlow.setOpenId(stock.getOpenId());
            stockFlow.setCreateTime(DateUtils.getCurrentUnixTime());
            stockFlow.setUpdateTime(DateUtils.getCurrentUnixTime());
            stockFlow.setStockId(stock.getStockId());
            StockLogic.AddStockFlow(stockFlow, StockFlowType.GIVED);


            //添加领取人的库存记录
            Stock getStock = new Stock();

            getStock.setOpenId(openId);
            getStock.setProductId(stock.getProductId());
            getStock.setProductSpecId(stock.getProductSpecId());
            getStock.setOrderId(stock.getOrderId());
            getStock.setOrderProductId(stock.getOrderProductId());

            getStock.setGiftStockId(gs.getGiftStockId());
            getStock.setParentStockId(stock.getStockId());

            getStocks.add(getStock);

        }

        //添加领取人库存
        StockLogic.AddGiftStock(getStocks);
        resp.setGiftId(giftId);
        resp.setGiftUniqueCode(gift.getGiftUniqueCode());

        //未领取记录
        //更新领取状态
        giftStocks = GiftLogic.GetUnReceivedGiftstocks(giftId);
        if (giftStocks.isEmpty()) {
            gift.setStatus(GiftStatus.BROUGHTOUT.getValue());
        } else {
            gift.setStatus(GiftStatus.DRAWING.getValue());
        }
        gift.setUpdateTime(DateUtils.getCurrentUnixTime());
        giftLogic.giftService.updateById(gift);

        return resp;

    }
    //endregion

    //region (public) 获取未领取记录 GetUnReceivedGiftstocks

    /**
     * 获取未领取记录
     *
     * @return
     */
    public static List<GiftStock> GetUnReceivedGiftstocks(int giftId) {

        Wrapper<GiftStock> wrapper = new EntityWrapper<GiftStock>().where("gift_id={0}", giftId);
        wrapper.where("not exists( select * from _stock where gift_stock_id = _gift_stock.gift_stock_id )");
        wrapper.where("exists( select * from _stock where stock_id = _gift_stock.stock_id and status = {0} )", StockStatus.GIVED.getValue());

        List<GiftStock> giftStocks = giftLogic.giftStockService.selectList(wrapper);

        return giftStocks;
    }
    //endregion


    /**
     * 取消赠送
     * 已被领取的不退还
     *
     * @param requ
     * @return
     */
    public static CancelGiftResp CancelGift(CancelGiftRequ requ) {

        CancelGiftResp resp = new CancelGiftResp();

        if (StringUtils.isBlank(requ.getGiftUniqueCode())) {
            throw new TipException("赠送编号不能为空");
        }
        if (StringUtils.isBlank(requ.getBaseRequ().getOpenId())) {
            throw new TipException("用户编号不能为空");
        }

        //当前操作人
        String openId = requ.getBaseRequ().getOpenId();

        Gift gift = GiftLogic.selectByUniqueCode(requ.getGiftUniqueCode());
        int giftId = gift.getGiftId();

        if (gift == null) {
            throw new TipException("没有找到赠送信息");
        }
        if (gift.getStatus() == GiftStatus.CANCEL.getValue()) {
            throw new TipException("用户已取消分享");
        }
        if (gift.getStatus() == GiftStatus.BROUGHTOUT.getValue()) {
            throw new TipException("礼物被领完啦");
        }
        //必须本人操作取消
        if (!gift.getOpenId().equals(openId)) {
            throw new TipException("没有找到赠送信息");
        }

        List<GiftStock> giftStocks = GiftLogic.GetUnReceivedGiftstocks(giftId);
        if (giftStocks.isEmpty()) {
            throw new TipException("礼物被领完啦");
        }

        giftStocks.forEach(gf -> {
            Stock stock = giftLogic.stockService.selectById(gf.getStockId());
            stock.setStatus(StockStatus.UNUSE.getValue());
            stock.setUpdateTime(DateUtils.getCurrentUnixTime());

            giftLogic.stockService.updateById(stock);

            //库存日志
            StockFlow stockFlow = new StockFlow();
            stockFlow.setOpenId(stock.getOpenId());
            stockFlow.setCreateTime(DateUtils.getCurrentUnixTime());
            stockFlow.setUpdateTime(DateUtils.getCurrentUnixTime());
            stockFlow.setStockId(stock.getStockId());
            StockLogic.AddStockFlow(stockFlow, StockFlowType.CANCEL);

        });

        gift.setUpdateTime(DateUtils.getCurrentUnixTime());
        gift.setStatus(GiftStatus.CANCEL.getValue());

        giftLogic.giftService.updateById(gift);

        resp.setGiftId(gift.getGiftId());
        return resp;
    }

}
