package com.jsj.member.ob.controller.api;

import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.controller.BaseController;
import com.jsj.member.ob.dto.api.Request;
import com.jsj.member.ob.dto.api.Response;
import com.jsj.member.ob.dto.api.gift.*;
import com.jsj.member.ob.dto.api.stock.StockDto;
import com.jsj.member.ob.dto.mini.*;
import com.jsj.member.ob.enums.GiftStatus;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.GiftLogic;
import com.jsj.member.ob.logic.StockLogic;
import com.jsj.member.ob.service.GiftStockService;
import com.jsj.member.ob.service.StockService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mini")
public class ApiGiftController extends BaseController {

    @Autowired
    StockService stockService;

    @Autowired
    GiftStockService giftStockService;

    //region (public) 赠送领取列表 gifts

    /**
     * 赠送领取列表
     *
     * @param requ
     * @return
     */
    @ApiOperation(value = "赠送领取列表")
    @RequestMapping(value = "/gifts", method = RequestMethod.POST)
    public Response<GiftsResp> gifts(@ApiParam(value = "请求实体", required = true)
                                     @RequestBody
                                     @Validated Request<GiftsRequ> requ) {

        GiftsResp resp = new GiftsResp();
        String unionId = requ.getRequestBody().getUnionId();

        //用户所有赠送的
        List<UserGiftDto> giftDtos = GiftLogic.GetGives(unionId);

        //用户所有领取的
        List<UserDrawDto> drawDtos = GiftLogic.GetReceived(unionId);

        resp.setDrawDtos(drawDtos);
        resp.setGiftDtos(giftDtos);

        return Response.ok(resp);
    }
    //endregion

    //region (public) 赠送详情 giftDetail

    /**
     * 赠送详情
     *
     * @param requ
     * @return
     */
    @ApiOperation(value = "赠送详情")
    @RequestMapping(value = "/giftDetail", method = RequestMethod.POST)
    public Response<GiftDetailResp> giftDetail(@ApiParam(value = "请求实体", required = true)
                                               @RequestBody
                                               @Validated Request<GiftDetailRequ> requ) {

        GiftDetailResp resp = new GiftDetailResp();

        String giftUniqueCode = requ.getRequestBody().getGiftUniqueCode();

        //礼包信息
        GiftDto giftDto = GiftLogic.GetGift(giftUniqueCode);

        //赠送的库存
        List<StockDto> giveStock = GiftLogic.GetGiftStocks(giftDto.getGiftId());
        //去重计算数量
        List<StockDto> giveStocks = StockLogic.FilterData(giveStock);


        //领取的库存
        List<StockDto> receiveStock = GiftLogic.GetGiftRecevied(null, giftDto.getGiftId());
        //去重计算数量
        List<StockDto> receiveStocks = StockLogic.FilterData(receiveStock);

        //未分享
        if (giftDto.getGiftStatus() == GiftStatus.UNSHARE) {
            //分享图片
            if (giftDto.getStockDtos().get(0).getProductDto().getProductImgDtos() != null) {
                String imgUrl = giftDto.getStockDtos().get(0).getProductDto().getProductImgDtos().get(0).getImgPath();
                resp.setImgUrl(imgUrl);

            }
        }

        resp.setReceiveStocks(receiveStocks);
        resp.setGiveStocks(giveStocks);
        resp.setGiftDto(giftDto);

        return Response.ok(resp);
    }
    //endregion

    //region (public) 领取详情 receiveDetail

    /**
     * 领取详情
     *
     * @param requ
     * @return
     */
    @ApiOperation(value = "领取详情")
    @RequestMapping(value = "/receiveDetail", method = RequestMethod.POST)
    public Response<ReceiveDetailResp> receiveDetail(@ApiParam(value = "请求实体", required = true)
                                                     @RequestBody
                                                     @Validated Request<ReceiveDetailRequ> requ) {

        ReceiveDetailResp resp = new ReceiveDetailResp();

        String giftUniqueCode = requ.getRequestBody().getGiftUniqueCode();

        //礼包信息
        GiftDto giftDto = GiftLogic.GetGift(giftUniqueCode);

        //赠送的库存
        List<StockDto> giveStock = GiftLogic.GetGiftStocks(giftDto.getGiftId());
        //去重计算数量
        List<StockDto> giveStocks = StockLogic.FilterData(giveStock);


        //领取的库存
        List<StockDto> receiveStock = GiftLogic.GetGiftRecevied(null, giftDto.getGiftId());
        //去重计算数量
        List<StockDto> receiveStocks = StockLogic.FilterData(receiveStock);

        resp.setReceiveStocks(receiveStocks);
        resp.setGiveStocks(giveStocks);
        resp.setGiftDto(giftDto);

        return Response.ok(resp);

    }
    //endregion

    //region (public) 取消赠送 cancelGift

    /**
     * 取消赠送
     *
     * @param requ
     * @return
     */
    @Transactional(Constant.DBTRANSACTIONAL)
    @ApiOperation(value = "取消赠送")
    @RequestMapping(value = "/cancelGift", method = RequestMethod.POST)
    public Response<CancelGiftResp> cancelGift(@ApiParam(value = "请求实体", required = true)
                                               @RequestBody
                                               @Validated Request<CancelGiftRequ> requ) {

        CancelGiftResp resp = new CancelGiftResp();

        String giftUniqueCode = requ.getRequestBody().getGiftUniqueCode();
        String openId = requ.getRequestBody().getBaseRequ().getOpenId();

        //判断是否本人取消
        GiftDto giftDto = GiftLogic.GetGift(giftUniqueCode);
        if (!giftDto.getOpenId().equals(openId)) {
            throw new TipException("非法操作");
        }

        GiftLogic.CancelGift(requ.getRequestBody());

        return Response.ok(resp);

    }
    //endregion


    //region (public) 微信送好友 createGift

    /**
     * 微信送好友
     *
     * @param requ
     * @return
     */
    @Transactional(Constant.DBTRANSACTIONAL)
    @ApiOperation(value = "微信送好友")
    @RequestMapping(value = "/createGift", method = RequestMethod.POST)
    public Response<CreateGiftResp> createGift(@ApiParam(value = "请求实体", required = true)
                                               @RequestBody
                                               @Validated Request<CreateGiftRequ> requ) {

        CreateGiftResp resp = GiftLogic.CreateGift(requ.getRequestBody());
        return Response.ok(resp);

    }
    //endregion


}