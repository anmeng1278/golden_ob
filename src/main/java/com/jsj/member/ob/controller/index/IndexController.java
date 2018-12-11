package com.jsj.member.ob.controller.index;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jsj.member.ob.entity.Activity;
import com.jsj.member.ob.entity.Banner;
import com.jsj.member.ob.entity.Product;
import com.jsj.member.ob.enums.BannerType;
import com.jsj.member.ob.service.ActivityService;
import com.jsj.member.ob.service.BannerService;
import com.jsj.member.ob.service.DictService;
import com.jsj.member.ob.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
    DictService dictService;

    @Autowired
    BannerService bannerService;

    @Autowired
    ActivityService activityService;

    @Autowired
    ProductService productService;


    @GetMapping("")
    public String index(Model model,@RequestParam(value = "openId", defaultValue = "111") String openId) {
        //活动信息
        EntityWrapper<Activity> wrapper = new EntityWrapper<>();
        wrapper.where("delete_time is null");
        wrapper.orderBy("sort asc, update_time desc");
        List<Activity> activities = activityService.selectList(wrapper);

        //商品
        EntityWrapper<Product> productWrapper = new EntityWrapper<Product>();
        wrapper.where("delete_time is null and ifpass is true");
        wrapper.where("( select sum(_product_spec.stock_count) from _product_spec where _product_spec.product_id = _product.product_id) > 0");
        wrapper.orderBy("sort asc, update_time desc");
        List<Product> products = productService.selectList(productWrapper);


        //首页轮播图
        EntityWrapper<Banner> bannerWrapper = new EntityWrapper<>();
        bannerWrapper.where("delete_time is null and ifpass is true and type_id={0}", BannerType.COVER.getValue());
        List<Banner> banners = bannerService.selectList(bannerWrapper);

        model.addAttribute("banners",banners);
        model.addAttribute("activities",activities);
        model.addAttribute("products",products);
        model.addAttribute("openId",openId);

        return "index/index/index";

    }
    @GetMapping("/getSystemTime")
    @ResponseBody
    public String getSystemTime(Model model){
        //获取当前系统的毫秒数
        Long systemTime=System.currentTimeMillis();
        return String.valueOf(systemTime);
    }


}
