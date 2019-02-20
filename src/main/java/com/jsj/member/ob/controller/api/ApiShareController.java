package com.jsj.member.ob.controller.api;

import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.controller.BaseController;
import com.jsj.member.ob.dto.api.Request;
import com.jsj.member.ob.dto.api.Response;
import com.jsj.member.ob.dto.api.gift.GiftDto;
import com.jsj.member.ob.dto.api.gift.GiftStockDto;
import com.jsj.member.ob.dto.api.gift.ReceivedGiftRequ;
import com.jsj.member.ob.dto.api.gift.ReceivedGiftResp;
import com.jsj.member.ob.dto.api.order.OrderDto;
import com.jsj.member.ob.dto.api.redpacket.OrderRedpacketCouponDto;
import com.jsj.member.ob.dto.mini.*;
import com.jsj.member.ob.entity.Copywriter;
import com.jsj.member.ob.enums.GiftStatus;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.CopywriterLogic;
import com.jsj.member.ob.logic.GiftLogic;
import com.jsj.member.ob.logic.OrderLogic;
import com.jsj.member.ob.logic.RedpacketLogic;
import com.jsj.member.ob.tuple.TwoTuple;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${webconfig.virtualPath}/mini/share")
public class ApiShareController extends BaseController {

    //region (public) 领取红包 redPacket

    /**
     * 领取红包
     *
     * @param requ
     */
    @Transactional(Constant.DBTRANSACTIONAL)
    @ApiOperation(value = "领取红包")
    @RequestMapping(value = "/redPacket", method = RequestMethod.POST)
    public Response<RedPacketResp> redPacket(@ApiParam(value = "请求实体", required = true)
                                             @RequestBody
                                             @Validated Request<RedPacketRequ> requ) {

        RedPacketResp resp = new RedPacketResp();

        OrderDto orderDto = OrderLogic.GetOrder(requ.getRequestBody().getOrderUniqueCode());
        if (orderDto == null) {
            throw new TipException("没有找到订单信息");
        }

        String openId = requ.getRequestBody().getOpenId();
        String unionId = requ.getRequestBody().getUnionId();

        //领取
        TwoTuple<OrderRedpacketCouponDto, Boolean> twoTuple = RedpacketLogic.DistributeRedpacket(openId, unionId, orderDto.getOrderId());

        OrderRedpacketCouponDto couponDto = null;
        boolean isRepeatDraw = false;
        if (twoTuple != null) {
            couponDto = twoTuple.first;
            isRepeatDraw = twoTuple.second;
        }

        //查看领取记录
        List<OrderRedpacketCouponDto> redpacketCoupons = RedpacketLogic.GetOrderRedpacketDtos(orderDto.getOrderId(), true);

        resp.setCouponDto(couponDto);
        resp.setRedpacketCoupons(redpacketCoupons);
        resp.setRepeatDraw(isRepeatDraw);

        return Response.ok(resp);

    }
    //endregion

    //region (public) 本人查看分享 getGiftConfirm

    /**
     * 本人查看分享
     *
     * @param requ
     * @return
     */
    @ApiOperation(value = "本人查看分享")
    @RequestMapping(value = "/getGiftConfirm", method = RequestMethod.POST)
    public Response<GetGiftConfirmResp> getGiftConfirm(@ApiParam(value = "请求实体", required = true)
                                                       @RequestBody
                                                       @Validated Request<GetGiftConfirmRequ> requ) {

        GetGiftConfirmResp resp = new GetGiftConfirmResp();
        String giftUniqueCode = requ.getRequestBody().getGiftUniqueCode();

        GiftDto giftDto = GiftLogic.GetGift(giftUniqueCode);
        if (giftDto == null) {
            throw new TipException("没有找到分享信息");
        }

        if (giftDto.getStockDtos().size() == 0) {
            throw new TipException("没有找到分享信息");
        }

        String openId = requ.getRequestBody().getOpenId();
        if (!giftDto.getOpenId().equals(openId)) {
            //不是本人操作的分享，不允查看
            throw new TipException("没有找到分享信息");
        }

        resp.setGiftDto(giftDto);

        return Response.ok(resp);

    }

    //endregion

    //region (public) 更新分享 updateGiftConfirm

    /**
     * 更新分享
     * 1.待分享页面点击分享群或好友时
     * 2.分享成功回调时
     *
     * @param requ
     * @return
     */

    @Transactional(Constant.DBTRANSACTIONAL)
    @ApiOperation(value = "更新分享")
    @RequestMapping(value = "/updateGiftConfirm", method = RequestMethod.POST)
    public Response<UpdateGiftConfirmResp> updateGiftConfirm(@ApiParam(value = "请求实体", required = true)
                                                             @RequestBody
                                                             @Validated Request<UpdateGiftConfirmRequ> requ) {

        UpdateGiftConfirmResp resp = new UpdateGiftConfirmResp();

        String giftUniqueCode = requ.getRequestBody().getGiftUniqueCode();

        GiftDto giftDto = GiftLogic.GetGift(giftUniqueCode);
        if (giftDto == null) {
            throw new TipException("没有找到分享信息");
        }
        if (giftDto.getStockDtos().size() == 0) {
            throw new TipException("没有找到分享信息");
        }
        String openId = requ.getRequestBody().getOpenId();
        if (!giftDto.getOpenId().equals(openId)) {
            //不是本人操作的分享，不允查看
            throw new TipException("没有找到分享信息");
        }

        String op = requ.getRequestBody().getOp();

        if (op.equals("ready")) {
            String blessings = requ.getRequestBody().getBlessings();
            GiftLogic.GiftReadyToShare(giftUniqueCode, requ.getRequestBody().getGiftShareType(), blessings);
        }

        if (op.equals("success")) {
            GiftLogic.GiftShareSuccessed(giftUniqueCode);
        }

        return Response.ok(resp);

    }
    //endregion

    //region (public) 获取分享文案 copyWriter

    /**
     * 获取分享文案
     * 待分享页面点击“换一个”，随机获取一个文案
     *
     * @param requ
     * @return
     */
    @ApiOperation(value = "获取分享文案")
    @RequestMapping(value = "/copyWriter", method = RequestMethod.POST)
    public Response<CopyWriterResp> copyWriter(@ApiParam(value = "请求实体", required = true)
                                               @RequestBody
                                               @Validated Request<CopyWriterRequ> requ) {

        int typeId = requ.getRequestBody().getTypeId();
        Copywriter copywriter = CopywriterLogic.GetOneCopywriter(typeId);
        return Response.ok(copywriter);
    }
    //endregion

    //region (public) 获取分享领取信息 getGiftDraw

    /**
     * 获取分享领取信息
     * 领取页面初始化时请求
     *
     * @param requ
     * @return
     */
    @ApiOperation(value = "获取分享领取信息")
    @RequestMapping(value = "/getGiftDraw", method = RequestMethod.POST)
    public Response<GetGiftDrawResp> getGiftDraw(@ApiParam(value = "请求实体", required = true)
                                                 @RequestBody
                                                 @Validated Request<GetGiftDrawRequ> requ) {

        GetGiftDrawResp resp = new GetGiftDrawResp();
        String giftUniqueCode = requ.getRequestBody().getGiftUniqueCode();

        GiftDto giftDto = GiftLogic.GetGift(giftUniqueCode);
        if (giftDto == null) {
            throw new TipException("没有找到分享信息", 1404);
        }

        if (giftDto.getStockDtos().size() == 0) {
            throw new TipException("没有找到分享信息", 1404);
        }

        //已领完或取消分享，跳转到领取详情页面
        if (giftDto.getGiftStatus().equals(GiftStatus.BROUGHTOUT) || giftDto.getGiftStatus().equals(GiftStatus.CANCEL)) {
            throw new TipException(String.format("当前分享\"%s\"", giftDto.getGiftStatus().getMessage()), 1302);
        }

        //判断会员本人是否允许领取
        String openId = requ.getRequestBody().getOpenId();
        String unionId = requ.getRequestBody().getUnionId();

        if (!GiftLogic.userSelfCanDraw(giftDto, openId)) {
            throw new TipException("本人不能领取", 1500);
        }

        //判断当前操作人是否已领取
        if (GiftLogic.userIsDraw(giftDto.getGiftId(), unionId)) {
            throw new TipException("您已领取过啦", 1302);
        }

        resp.setGiftDto(giftDto);

        return Response.ok(resp);

    }
    //endregion

    //region (public) 用户操作领取 receivedGift

    /**
     * 用户操作领取
     *
     * @param requ
     * @return
     */
    @Transactional(Constant.DBTRANSACTIONAL)
    @ApiOperation(value = "领取分享")
    @RequestMapping(value = "/receivedGift", method = RequestMethod.POST)
    public Response<ReceivedGiftResp> receivedGift(@ApiParam(value = "请求实体", required = true)
                                                   @RequestBody
                                                   @Validated Request<ReceivedGiftRequ> requ) {

        ReceivedGiftResp resp = GiftLogic.ReceivedGift(requ.getRequestBody());
        return Response.ok(resp);

    }
    //endregion

    //region (public) 获取分享领取完成信息 getGiftDrawComplete

    /**
     * 获取分享领取完成信息
     * 查看领取信息页面
     *
     * @param requ
     * @return
     */
    @ApiOperation(value = "获取分享领取完成信息")
    @RequestMapping(value = "/getGiftDrawComplete", method = RequestMethod.POST)
    public Response<GetGiftDrawResp> getGiftDrawComplete(@ApiParam(value = "请求实体", required = true)
                                                         @RequestBody
                                                         @Validated Request<GetGiftDrawRequ> requ) {

        GetGiftDrawResp resp = new GetGiftDrawResp();

        GiftDto giftDto = GiftLogic.GetGift(requ.getRequestBody().getGiftUniqueCode());
        if (giftDto == null) {
            throw new TipException("没有找到分享信息", 1404);
        }
        if (giftDto.getStockDtos().size() == 0) {
            throw new TipException("没有找到分享信息", 1404);
        }

        //赠送的库存
        List<GiftStockDto> giftStockDtos = GiftLogic.GetGiftStockDtos(giftDto.getGiftId());

        //是否本人领取数
        String openId = requ.getRequestBody().getOpenId();
        long myDrawCount = giftStockDtos.stream().filter(st -> st.getReceviedStockDto() != null &&
                st.getReceviedStockDto().getOpenId().equals(openId)).count();

        //待领取列表
        List<GiftStockDto> unReceived = giftStockDtos.stream().filter(st -> st.getReceviedStockDto() == null).collect(Collectors.toList());

        //已领取列表
        List<GiftStockDto> received = giftStockDtos.stream().filter(st -> st.getReceviedStockDto() != null).collect(Collectors.toList());

        resp.setGiftDto(giftDto);
        resp.setGiftStockDtos(giftStockDtos);
        resp.setMyDrawCount(myDrawCount);
        resp.setReceiveds(received);
        resp.setUnReceiveds(unReceived);

        //只有一个商品、本人发放 显示"礼物等待被领取中"
        //只有一个商品、非本人 跳转到领取页面
        //多个商品，本人发放 显示"礼物等待被领取中"

        return Response.ok(resp);

    }
    //endregion

}
