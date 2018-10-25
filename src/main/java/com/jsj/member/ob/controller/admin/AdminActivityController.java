package com.jsj.member.ob.controller.admin;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.dto.RestResponseBo;
import com.jsj.member.ob.dto.api.activity.ActivityDto;
import com.jsj.member.ob.entity.Activity;
import com.jsj.member.ob.entity.Dict;
import com.jsj.member.ob.entity.Product;
import com.jsj.member.ob.enums.ActivityType;
import com.jsj.member.ob.enums.DictType;
import com.jsj.member.ob.form.ActivityForm;
import com.jsj.member.ob.form.DictForm;
import com.jsj.member.ob.logic.ActivityLogic;
import com.jsj.member.ob.logic.DictLogic;
import com.jsj.member.ob.logic.ProductLogic;
import com.jsj.member.ob.service.ActivityService;
import com.jsj.member.ob.service.ProductService;
import com.jsj.member.ob.utils.CCPage;
import com.jsj.member.ob.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@ApiIgnore
@Controller
@RequestMapping("/admin/activity")
public class AdminActivityController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ProductService productService;

    //活动活动列表
    /**
     * 查询所有字典列表
     *
     * @param page  当前页
     * @param limit 每页显示条数
     * @param keys  关键字
     * @param model
     * @return
     */
    @GetMapping(value = {"", "/index"})
    public String index(@RequestParam(value = "page", defaultValue = "1") Integer page,
                        @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                        @RequestParam(value = "keys", defaultValue = "") String keys,
                        @RequestParam(value = "typeId", defaultValue = "0") Integer typeId,
                        Model model) {
        EntityWrapper<Activity> wrapper = new EntityWrapper<>();
        wrapper.where("delete_time is null");

        if (typeId > 0) {
            wrapper.where("type_id={0}", typeId);
        }
        wrapper.where(!StringUtils.isBlank(keys), "(activity_name LIKE concat(concat('%',{0}),'%') )", keys);
        wrapper.orderBy("create_time desc");

        Page<Activity> pageInfo = new Page<>(page, limit);
        Page<Activity> pp = activityService.selectPage(pageInfo, wrapper);


        //所属类型
        List<ActivityType> activityTypes =Arrays.asList(ActivityType.values());

        model.addAttribute("infos", new CCPage<Dict>(pp, limit));
        model.addAttribute("activityTypes", activityTypes);

        model.addAttribute("keys", keys);
        model.addAttribute("typeId", typeId);

        return "admin/activity/index";
    }
    /**
     * 查询信息页面
     *
     * @param activityId
     * @param model
     * @return
     */
    @GetMapping("/{activityId}")
    public String info(@PathVariable("activityId") Integer activityId, Model model) {

        ActivityDto activity = new ActivityDto();
        if (activityId > 0) {
            activity = ActivityLogic.GetActivity(activityId);
        }
        //获取活动类型
        List<ActivityType> activityTypes = Arrays.asList(ActivityType.values());

        model.addAttribute("activityTypes", activityTypes);
        model.addAttribute("info", activity);
        model.addAttribute("dictId", activityId);

        return "admin/activity/info";
    }

    /**
     * 修改或添加
     *
     * @param form
     * @param model
     * @return
     */
    @PostMapping("/{activityId}")
    @ResponseBody
    @Transactional(Constant.DBTRANSACTIONAL)
    public RestResponseBo saveOrUpdate(@PathVariable("activityId") Integer activityId, @Valid ActivityForm form, Model model) {

        Activity activity = new Activity();

        if (activityId > 0) { //修改
            //todo
        } else { //添加
            activity.setCreateTime(DateUtils.getCurrentUnixTime());
            activity.setUpdateTime(DateUtils.getCurrentUnixTime());
            BeanUtils.copyProperties(form, activity);
            activityService.insert(activity);
        }
        return RestResponseBo.ok("保存成功");
    }

}
