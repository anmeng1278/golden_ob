package com.jsj.member.ob.controller.index;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jsj.member.ob.config.Webconfig;
import com.jsj.member.ob.controller.BaseController;
import com.jsj.member.ob.dto.RestResponseBo;
import com.jsj.member.ob.dto.api.activity.ActivityDto;
import com.jsj.member.ob.dto.api.activity.ActivityProductDto;
import com.jsj.member.ob.dto.api.order.OrderDto;
import com.jsj.member.ob.dto.api.product.ProductDto;
import com.jsj.member.ob.dto.thirdParty.GetPayTradeRequ;
import com.jsj.member.ob.dto.thirdParty.GetPayTradeResp;
import com.jsj.member.ob.entity.Banner;
import com.jsj.member.ob.enums.ActivityType;
import com.jsj.member.ob.enums.BannerType;
import com.jsj.member.ob.enums.OrderStatus;
import com.jsj.member.ob.enums.ProductType;
import com.jsj.member.ob.logic.*;
import com.jsj.member.ob.utils.DateUtils;
import com.jsj.member.ob.utils.EncryptUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@ApiIgnore
@Controller
@RequestMapping("${webconfig.virtualPath}")
public class IndexController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(IndexController.class);

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


}
