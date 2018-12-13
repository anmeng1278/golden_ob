package com.jsj.member.ob.controller.index;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jsj.member.ob.config.Webconfig;
import com.jsj.member.ob.controller.BaseController;
import com.jsj.member.ob.dto.api.activity.ActivityDto;
import com.jsj.member.ob.dto.api.activity.ActivityProductDto;
import com.jsj.member.ob.dto.api.product.ProductDto;
import com.jsj.member.ob.dto.thirdParty.GetPayTradeRequ;
import com.jsj.member.ob.dto.thirdParty.GetPayTradeResp;
import com.jsj.member.ob.entity.Banner;
import com.jsj.member.ob.enums.ActivityType;
import com.jsj.member.ob.enums.BannerType;
import com.jsj.member.ob.enums.ProductType;
import com.jsj.member.ob.logic.*;
import com.jsj.member.ob.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@ApiIgnore
@Controller
@RequestMapping("${webconfig.virtualPath}")
public class IndexController extends BaseController {

    @GetMapping(value = {""})
    public String index(HttpServletRequest request) {

        //首页轮播图
        List<Banner> banners = BannerLogic.GetBanner(BannerType.COVER.getValue());
        request.setAttribute("banners", banners);


        //品质出行
        List<ProductDto> qualityTravels = ProductLogic.GetProductDtos(ProductType.QUALITYTRAVELS.getValue(), 4);
        request.setAttribute("qualityTravels", qualityTravels);

        //爆款单品
        List<ProductDto> hotProducts = ProductLogic.GetProductDtos(ProductType.HOTPRODUCT.getValue(), 4);
        request.setAttribute("hotProducts", hotProducts);


        //限时秒杀
        List<ActivityProductDto> secKills = new ArrayList<>();
        ActivityDto activityDto = ActivityLogic.GetActivity(ActivityType.SECKILL);
        int secKillTime = 0;
        if (activityDto != null) {
            secKills = ActivityLogic.GetActivityProductDtos(activityDto.getActivityId());
            secKillTime = activityDto.getBeginTime() - DateUtils.getCurrentUnixTime();
        }
        request.setAttribute("secKills", secKills);
        request.setAttribute("secKillTime", secKillTime);


        //组合优惠
        List<ActivityDto> setSales = ActivityLogic.GetActivityByType(ActivityType.COMBINATION);
        request.setAttribute("setSales", setSales);


        //用户购物车的商品数量
        String openId = this.OpenId();
        int count = CartLogic.GetCartProductCount(openId);
        request.setAttribute("count", count);

        return "index/index";
    }


    @Autowired
    Webconfig webconfig;

    @GetMapping(value = {"/pay"})
    public String pay(HttpServletRequest request) {

        String openId = this.OpenId();

        GetPayTradeRequ requ = new GetPayTradeRequ();
        requ.getRequestHead().setTimeStamp(DateUtils.getCurrentUnixTime() + "");
        requ.getRequestHead().setSourceFrom("KTGJ");

        requ.getRequestBody().setSourceWay("20");
        requ.getRequestBody().setSourceApp("600");
        requ.getRequestBody().setPayMethod("22");
        requ.getRequestBody().setOutTradeId("95346343322");
        requ.getRequestBody().setPayAmount("0.1");
        requ.getRequestBody().setOpenId(openId);
        requ.getRequestBody().setOrderTimeOut("20181220175900");

        GetPayTradeResp resp = ThirdPartyLogic.GetPayTrade(requ);
        System.out.println(JSON.toJSONString(resp));

        //{"ResponseBody":{"ResponseText":"{\"appId\":\"wx555b169abb4d62ce\",\"timeStamp\":\"1544635487\",\"nonceStr\":\"a0ilrwe275ivvp0ikol64o8lp01mwhng\",\"package\":\"prepay_id=wx12172447043119e5fc7b9f8c4028765572\",\"signType\":\"MD5\",\"paySign\":\"B1DB925035FD24DAA921C706EB34B41C\"}"},"ResponseHead":{"Code":"0000","Message":"SUCCESS"}}

        if (resp.getResponseHead().getCode().equals("0000")) {

            String responseText = resp.getResponseBody().getResponseText();
            JSONObject jsonObject = JSON.parseObject(responseText);

            String appId = jsonObject.get("appId").toString();
            String timeStamp = jsonObject.get("timeStamp").toString();
            String nonceStr = jsonObject.get("nonceStr").toString();
            String packageStr = jsonObject.get("package").toString();
            String signType = jsonObject.get("signType").toString();
            String paySign = jsonObject.get("paySign").toString();

            request.setAttribute("pappId", appId);
            request.setAttribute("ptimestamp", timeStamp);
            request.setAttribute("pnonceStr", nonceStr);
            request.setAttribute("ppackage", packageStr);
            request.setAttribute("psignType", signType);
            request.setAttribute("ppaySign", paySign);
        }

        //
        //appId: [[${appId}]],
        //timestamp: [[${timeStamp}]],
        //nonceStr: [[${nonceStr}]],
        //    package: [[${package}]],
        //signType: [[${signType}]],
        //paySign: [[${paySign}]],

        return "index/pay";

    }


}
