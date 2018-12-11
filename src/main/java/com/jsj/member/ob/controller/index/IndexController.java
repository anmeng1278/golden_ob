package com.jsj.member.ob.controller.index;

import com.jsj.member.ob.controller.BaseController;
import com.jsj.member.ob.dto.api.activity.ActivityDto;
import com.jsj.member.ob.dto.api.activity.ActivityProductDto;
import com.jsj.member.ob.dto.api.product.ProductDto;
import com.jsj.member.ob.enums.ActivityType;
import com.jsj.member.ob.logic.ActivityLogic;
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
@RequestMapping("/")
public class IndexController extends BaseController {

    @GetMapping(value = {""})
    public String index(HttpServletRequest request) {
        
        //品质出行
        List<ProductDto> qualityTravels = ProductLogic.GetProductDtos(1, 4);
        request.setAttribute("qualityTravels", qualityTravels);

        //限时秒杀
        //获取秒杀活动排序第 个数据
        List<ActivityProductDto> secKills = new ArrayList<>();
        ActivityDto activityDto = ActivityLogic.GetActivity(ActivityType.SECKILL);
        int secKillTime = 0;
        if (activityDto != null) {
            secKills = ActivityLogic.GetActivityProductDtos(activityDto.getActivityId());
            secKillTime = activityDto.getBeginTime() - DateUtils.getCurrentUnixTime();
        }
        request.setAttribute("secKills", secKills);
        request.setAttribute("secKillTime", secKillTime);

        return "index/index";
    }

}
