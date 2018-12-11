package com.jsj.member.ob.controller.index;

import com.jsj.member.ob.controller.BaseController;
import com.jsj.member.ob.dto.api.activity.ActivityDto;
import com.jsj.member.ob.dto.api.activity.ActivityProductDto;
import com.jsj.member.ob.dto.api.product.ProductDto;
import com.jsj.member.ob.entity.Banner;
import com.jsj.member.ob.enums.ActivityType;
import com.jsj.member.ob.enums.BannerType;
import com.jsj.member.ob.logic.ActivityLogic;
import com.jsj.member.ob.logic.BannerLogic;
import com.jsj.member.ob.logic.CartLogic;
import com.jsj.member.ob.logic.ProductLogic;
import com.jsj.member.ob.utils.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@ApiIgnore
@Controller
@RequestMapping("/index")
public class IndexController extends BaseController {

    @GetMapping(value = {""})
    public String index(HttpServletRequest request) {

        //首页轮播图
        List<Banner> banners = BannerLogic.GetBanner(BannerType.COVER.getValue());
        request.setAttribute("banners", banners);


        //品质出行
        List<ProductDto> qualityTravels = ProductLogic.GetProductDtos(1, 4);
        request.setAttribute("qualityTravels", qualityTravels);

        //爆款单品
        List<ProductDto> hotProducts = ProductLogic.GetProductDtos(839033, 4);
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
