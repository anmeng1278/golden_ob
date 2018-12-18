package com.jsj.member.ob.controller.index;

import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.controller.BaseController;
import com.jsj.member.ob.dto.RestResponseBo;
import com.jsj.member.ob.dto.api.gift.GiftDto;
import com.jsj.member.ob.dto.api.gift.GiftStockDto;
import com.jsj.member.ob.dto.api.gift.ReceivedGiftRequ;
import com.jsj.member.ob.dto.api.gift.ReceivedGiftResp;
import com.jsj.member.ob.dto.api.redpacket.OrderRedpacketCouponDto;
import com.jsj.member.ob.enums.GiftShareType;
import com.jsj.member.ob.enums.GiftStatus;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.GiftLogic;
import com.jsj.member.ob.logic.RedpacketLogic;
import com.jsj.member.ob.utils.EncryptUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@ApiIgnore
@Controller
@RequestMapping("${webconfig.virtualPath}/share")
public class ShareController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(ShareController.class);

    /**
     * 红包领取页面
     *
     * @param request
     * @return 示例链接：
     * http://localhost/share/redPacket?ok=2rpaS3MKnYw%3d
     */
    @GetMapping(value = {"/redPacket/{obs}"})
    @Transactional(Constant.DBTRANSACTIONAL)
    public String redPacket(
            @PathVariable(name = "obs", required = true) String obs,
            HttpServletRequest request) {

        int orderId = 0;
        try {
            orderId = Integer.parseInt(EncryptUtils.decrypt(obs));
        } catch (Exception e) {
            return this.Redirect("/");
        }

        String openId = this.OpenId();

        //领取
        OrderRedpacketCouponDto couponDto = RedpacketLogic.DistributeRedpacket(openId, orderId);
        request.setAttribute("couponDto", couponDto);

        //查看领取记录
        List<OrderRedpacketCouponDto> redpacketCoupons = RedpacketLogic.GetOrderRedpacketDtos(orderId, true);
        request.setAttribute("redpacketCoupons", redpacketCoupons);

        return "index/redPacket";

    }


    @PostMapping(value = {"/gift/{obs}/confirm"})
    @ResponseBody
    @Transactional(Constant.DBTRANSACTIONAL)
    public RestResponseBo saveGiftConfirm(
            @PathVariable(name = "obs", required = true) String obs,
            HttpServletRequest request) {

        GiftDto giftDto = GiftLogic.GetGift(obs);
        if (giftDto == null) {
            return RestResponseBo.fail("没有找到分享信息");
        }
        if (giftDto.getStockDtos().size() == 0) {
            return RestResponseBo.fail("没有找到分享信息");
        }
        String openId = this.OpenId();
        if (!giftDto.getOpenId().equals(openId)) {
            //不是本人操作的分享，不允查看
            return RestResponseBo.fail("没有找到分享信息");
        }

        String op = request.getParameter("op");

        if (op.equals("ready")) {
            int shareType = Integer.parseInt(request.getParameter("shareType"));
            String blessings = request.getParameter("blessings");
            GiftLogic.GiftReadyToShare(obs, GiftShareType.valueOf(shareType), blessings);
        }

        if (op.equals("success")) {
            GiftLogic.GiftShareSuccessed(obs);
        }

        return RestResponseBo.ok("操作成功");

    }

    @GetMapping(value = {"/gift/{obs}/confirm"})
    public String giftConfirm(
            @PathVariable(name = "obs", required = true) String obs,
            HttpServletRequest request) {

        GiftDto giftDto = GiftLogic.GetGift(obs);
        if (giftDto == null) {
            return this.Redirect("/");
        }

        if (giftDto.getStockDtos().size() == 0) {
            return this.Redirect("/");
        }

        String openId = this.OpenId();
        if (!giftDto.getOpenId().equals(openId)) {
            //不是本人操作的分享，不允查看
            return this.Redirect("/");
        }

        String shareUrl = this.Url(String.format("/share/gift/%s/draw", obs), false);
        request.setAttribute("shareUrl", shareUrl);
        request.setAttribute("giftDto", giftDto);
        return "index/giftConfirm";
    }


    @GetMapping(value = {"/gift/{obs}/draw"})
    public String drawGift(
            @PathVariable(name = "obs", required = true) String obs,
            HttpServletRequest request) {

        GiftDto giftDto = GiftLogic.GetGift(obs);
        if (giftDto == null) {
            return this.Redirect("/");
        }

        if (giftDto.getStockDtos().size() == 0) {
            return this.Redirect("/");
        }

        //已领完或取消分享，跳转到领取详情页面
        if (giftDto.getGiftStatus().equals(GiftStatus.BROUGHTOUT) || giftDto.getGiftStatus().equals(GiftStatus.CANCEL)) {
            return this.Redirect(String.format("/share/gift/%s", obs));
        }

        String shareUrl = this.Url(String.format("/share/gift/%s/draw", obs), false);
        request.setAttribute("shareUrl", shareUrl);

        request.setAttribute("giftDto", giftDto);
        return "index/giftDraw";

    }

    /**
     * 用户操作领取页面
     *
     * @param obs
     * @param request
     * @return
     */
    @PostMapping(value = {"/gift/{obs}/draw"})
    @Transactional(Constant.DBTRANSACTIONAL)
    @ResponseBody
    public RestResponseBo saveDrawGift(
            @PathVariable(name = "obs", required = true) String obs,
            HttpServletRequest request) {

        String openId = this.OpenId();
        String shareUrl = this.Url(String.format("/share/gift/%s", obs), false);

        try {
            ReceivedGiftRequ requ = new ReceivedGiftRequ();
            requ.setGiftUniqueCode(obs);
            requ.getBaseRequ().setOpenId(openId);

            ReceivedGiftResp resp = GiftLogic.ReceivedGift(requ);
            return RestResponseBo.ok("领取成功", shareUrl, resp);

        } catch (TipException ex) {
            return RestResponseBo.fail(ex.getMessage(), null, shareUrl);
        }

    }


    /**
     * 用户操作领取页面
     *
     * @param obs
     * @param request
     * @return
     */
    @GetMapping(value = {"/gift/{obs}"})
    public String giftInfo(
            @PathVariable(name = "obs", required = true) String obs,
            HttpServletRequest request) {

        GiftDto giftDto = GiftLogic.GetGift(obs);
        if (giftDto == null) {
            return this.Redirect("/");
        }
        if (giftDto.getStockDtos().size() == 0) {
            return this.Redirect("/");
        }

        String openId = this.OpenId();
        List<GiftStockDto> giftStockDtos = GiftLogic.GetGiftStockDtos(giftDto.getGiftId());


        //是否本人已领取
        long myDrawCount = giftStockDtos.stream().filter(st -> st.getReceviedStockDto() != null &&
                st.getReceviedStockDto().getOpenId().equals(openId)).count();

        //待领取列表
        List<GiftStockDto> unReceived = giftStockDtos.stream().filter(st -> st.getReceviedStockDto() == null).collect(Collectors.toList());

        //已领取列表
        List<GiftStockDto> received = giftStockDtos.stream().filter(st -> st.getReceviedStockDto() != null).collect(Collectors.toList());

        //是否我已领取
        request.setAttribute("myDrawCount", myDrawCount);

        request.setAttribute("unReceived", unReceived);
        request.setAttribute("received", received);
        request.setAttribute("giftDto", giftDto);
        request.setAttribute("receivedCount", received.size());
        request.setAttribute("unReceivedCount", unReceived.size());

        request.setAttribute("totalCount", giftStockDtos.size());


        String shareUrl = this.Url(String.format("/share/gift/%s/draw", obs), false);
        request.setAttribute("shareUrl", shareUrl);

        //只有一个商品、本人发放 显示"礼物等待被领取中"
        //只有一个商品、非本人 跳转到领取页面
        //多个商品，本人发放 显示"礼物等待被领取中"

        return "index/giftDrawComplete";

    }

}