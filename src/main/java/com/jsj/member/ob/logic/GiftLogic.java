package com.jsj.member.ob.logic;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.dto.api.gift.*;
import com.jsj.member.ob.dto.api.stock.StockDto;
import com.jsj.member.ob.dto.api.wechat.WechatDto;
import com.jsj.member.ob.entity.Gift;
import com.jsj.member.ob.entity.GiftStock;
import com.jsj.member.ob.entity.Stock;
import com.jsj.member.ob.entity.StockFlow;
import com.jsj.member.ob.enums.*;
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
public class GiftLogic extends BaseLogic {

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
        //if (StringUtils.isBlank(requ.getBlessings())) {
        //    throw new TipException("请输入祝福语");
        //}

        //用户编号
        String openId = requ.getBaseRequ().getOpenId();

        //库存编号
        List<Stock> stocks = new ArrayList<>();

        for (GiftProductDto dto : requ.getGiftProductDtos()) {

            int number = dto.getNumber();
            if (number <= 0) {
                continue;
            }

            EntityWrapper<Stock> wrapper = new EntityWrapper<>();

            wrapper.where("open_id={0}", openId);
            wrapper.where("status={0}", StockStatus.UNUSE.getValue());
            wrapper.where("product_id={0} and product_spec_id={1}", dto.getProductId(), dto.getProductSpecId());
            wrapper.orderBy("create_time asc");

            //待赠送列表
            List<Stock> currentStocks = giftLogic.stockService.selectList(wrapper);
            if (number > currentStocks.size()) {
                throw new TipException("库存不足，请重新赠送。");
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
            st.setStatus(StockStatus.GIVING.getValue());
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
     * <p>
     * 礼物分享逻辑
     * 1、一个商品，只可选择赠送一个好友，送礼物者不可领取；
     * 2、两个及以上商品，可选择赠送一个好友或发群福利,赠送者可以领取，每个用户限制只可领取一份；
     * 3、赠送者可选择一种商品或多种商品赠送；
     * 4、发群福利商品，如果是多款商品，用户打开群福利，随机获得；
     * 6、礼物赠送未被领取，24小时后退回原账户商品库；
     * 7、礼物未分享成功，在库存页面给提示；
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

        GiftDto gift = GiftLogic.GetGift(requ.getGiftUniqueCode());
        int giftId = gift.getGiftId();

        if (gift.getGiftStatus().equals(GiftStatus.CANCEL)) {
            throw new TipException("用户已取消分享");
        }
        if (gift.getGiftStatus().equals(GiftStatus.BROUGHTOUT)) {
            throw new TipException("您来晚了，礼物已被抢完啦！");
        }

        //可领取状态
        //GiftStatus.UNSHARE
        //GiftStatus.SHARED
        //GiftStatus.DRAWING

        if (gift.getStockDtos().size() == 1) {
            if (gift.getOpenId().equals(openId)) {
                throw new TipException("不能领取自己发出的礼物");
            }
        }

        if (gift.getGiftShareType().equals(GiftShareType.FRIEND)) {
            if (gift.getOpenId().equals(openId)) {
                throw new TipException("不能领取自己发出的礼物");
            }
        }


        //待领取的礼物
        //没有被领取过，当前状态已赠送
        List<GiftStock> giftStocks = GiftLogic.GetUnReceivedGiftstocks(gift.getGiftId());
        if (giftStocks.isEmpty()) {
            throw new TipException("您来晚了，礼物已被抢完啦！");
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
        GiftShareType giftShareType = gift.getGiftShareType();

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

        Gift entity = giftLogic.giftService.selectById(giftId);
        if (giftStocks.isEmpty()) {
            entity.setStatus(GiftStatus.BROUGHTOUT.getValue());
        } else {
            entity.setStatus(GiftStatus.DRAWING.getValue());
        }
        gift.setUpdateTime(DateUtils.getCurrentUnixTime());
        giftLogic.giftService.updateById(entity);

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
        wrapper.where("exists( select * from _stock where stock_id = _gift_stock.stock_id and status = {0} )", StockStatus.GIVING.getValue());

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

    /**
     * 获取赠送库存列表
     *
     * @param giftId
     * @return
     */
    public static List<StockDto> GetGiftStocks(int giftId) {

        EntityWrapper<GiftStock> wrapper = new EntityWrapper<>();
        wrapper.where("gift_id={0}", giftId);
        wrapper.orderBy("create_time desc");

        List<GiftStock> giftStocks = giftLogic.giftStockService.selectList(wrapper);
        List<StockDto> stockDtos = new ArrayList<>();

        for (GiftStock giftStock : giftStocks) {
            StockDto stockDto = StockLogic.GetStock(giftStock.getStockId());
            stockDtos.add(stockDto);
        }

        return stockDtos;

    }


    /**
     * 获取赠送详情
     *
     * @param giftId
     * @return
     */
    public static GiftDto GetGift(int giftId) {

        Gift entity = giftLogic.giftService.selectById(giftId);
        return ToDto(entity);

    }

    /**
     * 实体转换
     *
     * @param entity
     * @return
     */
    public static GiftDto ToDto(Gift entity) {

        GiftDto dto = new GiftDto();

        WechatDto wechatDto = WechatLogic.GetWechat(entity.getOpenId());

        dto.setBlessings(entity.getBlessings());
        dto.setCreateTime(entity.getCreateTime());
        dto.setDeleteTime(entity.getDeleteTime());
        dto.setExpiredTime(entity.getExpiredTime());
        dto.setGiftId(entity.getGiftId());

        dto.setGiftShareType(GiftShareType.valueOf(entity.getShareType()));
        dto.setGiftStatus(GiftStatus.valueOf(entity.getStatus()));
        dto.setGiftUniqueCode(entity.getGiftUniqueCode());
        dto.setOpenId(entity.getOpenId());

        dto.setRemarks(entity.getRemarks());
        dto.setUpdateTime(entity.getUpdateTime());
        dto.setWechatDto(wechatDto);

        return dto;

    }


    /**
     * 获取赠送详情
     *
     * @param giftUniqueCode
     * @return
     */
    public static GiftDto GetGift(String giftUniqueCode) {

        EntityWrapper<Gift> wrapper = new EntityWrapper<>();
        wrapper.where("gift_unique_code = {0} and delete_time is null", giftUniqueCode);

        Gift entity = giftLogic.giftService.selectOne(wrapper);
        GiftDto dto = ToDto(entity);

        //赠送的库存信息
        List<StockDto> stockDtos = StockLogic.GetStocks(entity.getGiftId());
        dto.setStockDtos(stockDtos);

        return dto;
    }


    /**
     * 根据赠送编号获取领取列表
     *
     * @param giftId
     * @return
     */
    public static List<GiftStockDto> GetGiftStockDtos(int giftId) {

        List<GiftStockDto> giftStockDtos = new ArrayList<>();

        Wrapper<GiftStock> wrapper = new EntityWrapper<>();
        wrapper.where("gift_id = {0} and delete_time is null", giftId);

        List<GiftStock> giftStocks = giftLogic.giftStockService.selectList(wrapper);
        if (giftStocks.size() == 0) {
            return giftStockDtos;
        }

        giftStocks.forEach(gs -> {

            GiftStockDto dto = new GiftStockDto();

            dto.setGiftId(gs.getGiftId());
            dto.setGiftStockId(gs.getGiftStockId());
            dto.setStockId(gs.getStockId());

            Stock stock = giftLogic.stockService.selectOne(new EntityWrapper<Stock>().where("gift_stock_id = {0}", gs.getGiftStockId()));
            if (stock != null) {
                StockDto stockDto = StockLogic.ToDto(stock);
                dto.setReceviedStockDto(stockDto);
            }
            giftStockDtos.add(dto);
        });

        return giftStockDtos;

    }


    //region (public) 获取赠送商品数量 GetGiftStockCount

    /**
     * 获取赠送商品数量
     *
     * @param giftId
     * @return
     */
    public static Integer GetGiftStockCount(int giftId) {

        EntityWrapper<GiftStock> wrapper = new EntityWrapper<>();
        wrapper.where("gift_id={0}", giftId);
        int count = giftLogic.giftStockService.selectCount(wrapper);

        return count;

    }
    //endregion

    /**
     * 获得用户赠送列表
     *
     * @param openId
     * @return
     */
    public static List<GiftDto> GetGives(String openId) {

        EntityWrapper<Gift> wrapper = new EntityWrapper<>();
        wrapper.where("delete_time is null and open_id={0}", openId);
        List<Gift> gifts = giftLogic.giftService.selectList(wrapper);

        List<GiftDto> giftDtos = new ArrayList<>();
        for (Gift gift : gifts) {
            GiftDto giftDto = GiftLogic.GetGift(gift.getGiftId());
            giftDtos.add(giftDto);
        }

        return giftDtos;
    }

    /**
     * 获得用户领取列表
     *
     * @param openId
     * @return
     */
    public static List<StockDto> GetReceived(String openId) {

        List<StockDto> stockDtos = StockLogic.GetStocks(openId, StockType.GIFT, null);
        return stockDtos;

    }


    /**
     * 分享前更新分享数据
     *
     * @param giftUniqueCode
     * @param giftShareType
     * @param blessings
     */
    public static void GiftReadyToShare(String giftUniqueCode, GiftShareType giftShareType, String blessings) {

        Gift gift = giftLogic.giftService.selectOne(new EntityWrapper<Gift>().where("gift_unique_code = {0}", giftUniqueCode));
        GiftDto giftDto = ToDto(gift);

        if (giftDto.getGiftStatus().equals(GiftStatus.UNSHARE)) {
            gift.setBlessings(blessings);
            gift.setShareType(giftShareType.getValue());
            giftLogic.giftService.updateById(gift);
        }
    }

    /**
     * 分享成功后回调
     *
     * @param giftUniqueCode
     */
    public static void GiftShareSuccessed(String giftUniqueCode) {

        Gift gift = giftLogic.giftService.selectOne(new EntityWrapper<Gift>().where("gift_unique_code = {0}", giftUniqueCode));
        GiftDto giftDto = ToDto(gift);

        if (giftDto.getGiftStatus().equals(GiftStatus.UNSHARE)) {
            gift.setStatus(GiftStatus.SHARED.getValue());
            giftLogic.giftService.updateById(gift);
        }

    }

}
