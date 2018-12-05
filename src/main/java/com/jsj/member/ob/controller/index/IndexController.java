package com.jsj.member.ob.controller.index;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jsj.member.ob.dto.RestResponseBo;
import com.jsj.member.ob.dto.api.activity.ActivityDto;
import com.jsj.member.ob.dto.api.activity.ActivityProductDto;
import com.jsj.member.ob.entity.Activity;
import com.jsj.member.ob.entity.Banner;
import com.jsj.member.ob.entity.Dict;
import com.jsj.member.ob.enums.DictType;
import com.jsj.member.ob.enums.SecKillStatus;
import com.jsj.member.ob.logic.ActivityLogic;
import com.jsj.member.ob.logic.BannerLogic;
import com.jsj.member.ob.logic.DictLogic;
import com.jsj.member.ob.service.ActivityService;
import com.jsj.member.ob.service.BannerService;
import com.jsj.member.ob.service.DictService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("/index")
public class IndexController {

    @Autowired
    DictService dictService;

    @Autowired
    BannerService bannerService;

    @Autowired
    ActivityService activityService;


    @GetMapping("/index")
    public String index(Model model) {

        //活动信息
        EntityWrapper<Activity> wrapper = new EntityWrapper<>();
        wrapper.where("delete_time is null");
        wrapper.orderBy("sort asc, update_time desc");
        List<Activity> activities = activityService.selectList(wrapper);

        //首页轮播图
        EntityWrapper<Banner> bannerWrapper = new EntityWrapper<>();
        bannerWrapper.where("delete_time is null");
        List<Banner> banners = bannerService.selectList(bannerWrapper);

        model.addAttribute("banners",banners);


        model.addAttribute("activities",activities);

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
