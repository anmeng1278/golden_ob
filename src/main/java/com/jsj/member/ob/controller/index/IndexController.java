package com.jsj.member.ob.controller.index;

import com.jsj.member.ob.controller.BaseController;
import com.jsj.member.ob.dto.api.activity.ActivityDto;
import com.jsj.member.ob.dto.api.activity.ActivityProductDto;
import com.jsj.member.ob.dto.api.product.ProductDto;
import com.jsj.member.ob.entity.Banner;
import com.jsj.member.ob.enums.ActivityType;
import com.jsj.member.ob.enums.BannerType;
import com.jsj.member.ob.enums.OrderFlag;
import com.jsj.member.ob.enums.ProductType;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.*;
import com.jsj.member.ob.redis.AccessKey;
import com.jsj.member.ob.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ApiIgnore
@Controller
@RequestMapping("${webconfig.virtualPath}")
public class IndexController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(IndexController.class);

    /**
     * 测试页面
     *
     * @param request
     * @return
     */
    @GetMapping(value = {"/test"})
    public String test(HttpServletRequest request) {

        try {
            throw new TipException("出错了");
        } catch (Exception ex) {
            this.Redirect("/");
        }

        return "index/index";
    }
    //region (public) 首页 index

    /**
     * 首页
     *
     * @param request
     * @return
     */
    @GetMapping(value = {""})
    @ResponseBody
    public String index(HttpServletRequest request, HttpServletResponse response) throws IOException {


        String html = this.GetAccessCache(AccessKey.pageIndex);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }

        //首页轮播图
        List<Banner> banners = BannerLogic.GetBanner(BannerType.COVER.getValue());
        request.setAttribute("banners", banners);


        //品质出行
        List<ProductDto> qualityTravels = ProductLogic.GetProductDtos(ProductType.QUALITYTRAVELS.getValue(), 4);
        request.setAttribute("qualityTravels", qualityTravels);

        //爆款单品
        List<ProductDto> hotProducts = ProductLogic.GetProductDtos(ProductType.HOTPRODUCT.getValue(), 12);
        request.setAttribute("hotProducts", hotProducts);


        //限时秒杀
        ActivityDto secKills = ActivityLogic.GetActivity(ActivityType.SECKILL);
        List<ActivityProductDto> secKillProducts = new ArrayList<>();

        String skillBegin = "";
        if (secKills != null) {
            secKillProducts = ActivityLogic.GetActivityProductDtos(secKills.getActivityId());
            skillBegin = DateUtils.formatDateByUnixTime(Long.parseLong(secKills.getBeginTime() + ""), "yyyy/MM/dd HH:mm:ss");
        }
        request.setAttribute("secKills", secKills);
        request.setAttribute("secKillProducts", secKillProducts);

        //组合优惠
        List<ActivityDto> setSales = ActivityLogic.GetActivityByType(ActivityType.COMBINATION);
        request.setAttribute("setSales", setSales);


        //用户购物车的商品数量
        String openId = this.OpenId();
        int count = CartLogic.GetCartProductCount(openId);
        request.setAttribute("count", count);
        request.setAttribute("skillBegin", skillBegin);

        //用户未支付订单数
        int size = OrderLogic.GetOrders(openId, OrderFlag.UNPAIDORDERS).size();
        request.setAttribute("size", size);

        return this.SetAccessCache(request, response, AccessKey.pageIndex, "index/index");

        //return "index/index";
    }
    //endregion

    /**
     * 兑换专区
     *
     * @param request
     * @return
     */
    @GetMapping(value = {"/exchange"})
    public String exchange(HttpServletRequest request) {

        ActivityDto exchange = ActivityLogic.GetActivity(ActivityType.EXCHANGE);
        List<ActivityProductDto> exchangeProducts = new ArrayList<>();

        if (exchange != null) {
            exchangeProducts = ActivityLogic.GetActivityProductDtos(exchange.getActivityId());
        }
        request.setAttribute("exchangeProducts", exchangeProducts);
        request.setAttribute("exchange", exchange);

        double balance = MemberLogic.StrictChoiceSearch(this.User().getJsjId());
        request.setAttribute("balance", balance);

        return "index/exchange";
    }
    //endregion

}
