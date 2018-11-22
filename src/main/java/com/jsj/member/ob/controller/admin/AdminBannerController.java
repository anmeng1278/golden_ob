package com.jsj.member.ob.controller.admin;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.dto.RestResponseBo;
import com.jsj.member.ob.entity.Banner;
import com.jsj.member.ob.enums.BannerType;
import com.jsj.member.ob.logic.BannerLogic;
import com.jsj.member.ob.service.BannerService;
import com.jsj.member.ob.utils.CCPage;
import com.jsj.member.ob.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@ApiIgnore
@Controller
@RequestMapping("/admin/banner")
public class AdminBannerController {

    @Autowired
    BannerService bannerService;

    /**
     * 轮播图列表展示
     * @param page
     * @param limit
     * @param typeId
     * @param keys
     * @param model
     * @return
     */
    @GetMapping(value={"","/index"})
    public String index(@RequestParam(value = "page",defaultValue = "1") Integer page,
                        @RequestParam(value = "limit",defaultValue = "10") Integer limit,
                        @RequestParam(value = "typeId",defaultValue = "0") Integer typeId,
                        @RequestParam(value = "keys",defaultValue = "") String keys, Model model){

        EntityWrapper<Banner> wrapper = new EntityWrapper<>();
        wrapper.where("delete_time is null");
        if(typeId > 0){
            wrapper.where("type_id={0}",typeId);
        }
        wrapper.where(!(StringUtils.isBlank(keys)),"(title LIKE concat(concat('%',{0}),'%') )", keys);
        wrapper.orderBy("sort asc, update_time desc");

        Page<Banner> pageInfo = new Page();
        Page<Banner> pp = bannerService.selectPage(pageInfo, wrapper);

        //轮播图类型
        List<BannerType> bannerTypes = Arrays.asList(BannerType.values());

        model.addAttribute("infos",new CCPage<Banner>(pp,limit));
        model.addAttribute("bannerTypes",bannerTypes);
        model.addAttribute("typeId",typeId);
        model.addAttribute("keys",keys);

        return "admin/banner/index";

    }

    /**
     * 轮播图信息页面
     * @param bannerId
     * @param model
     * @return
     */
    @GetMapping("/{bannerId}")
    public String info(@PathVariable("bannerId") Integer bannerId,Model model){

        Banner banner = new Banner();

        if(bannerId>0){
             banner = bannerService.selectById(bannerId);
        }

       List<BannerType> bannerTypes = Arrays.asList(BannerType.values());

        model.addAttribute("info",banner);
        model.addAttribute("bannerTypes",bannerTypes);
        model.addAttribute("bannerId",bannerId);

        return "admin/banner/info";
    }


    /**
     * 修改或添加轮播图
     * @param bannerId
     * @param request
     * @return
     */
    @PostMapping("/{bannerId}")
    @ResponseBody
    @Transactional(Constant.DBTRANSACTIONAL)
    public RestResponseBo saveOrUpdate(@PathVariable("bannerId") Integer bannerId,HttpServletRequest request){


        String title = request.getParameter("title");
        int typeId = Integer.parseInt(request.getParameter("typeId"));
        boolean ifpass = !StringUtils.isBlank(request.getParameter("ifpass"));
        String bannerPath = request.getParameter("bannerPath");
        String navigateUrl = request.getParameter("navigateUrl");
        if (StringUtils.isBlank(navigateUrl)) {
            navigateUrl = " ";
        }

        Banner banner = new Banner();

        if(bannerId > 0){
            //修改
            banner = bannerService.selectById(bannerId);

            banner.setTitle(title);
            banner.setTypeId(typeId);
            banner.setIfpass(ifpass);
            banner.setBannerPath(bannerPath);
            banner.setNavigateUrl(navigateUrl);
            banner.setUpdateTime(DateUtils.getCurrentUnixTime());

            bannerService.updateById(banner);

        }else {
            //添加
            banner.setTitle(title);
            banner.setTypeId(typeId);
            banner.setIfpass(ifpass);
            banner.setBannerPath(bannerPath);
            banner.setNavigateUrl(navigateUrl);
            banner.setUpdateTime(DateUtils.getCurrentUnixTime());
            banner.setCreateTime(DateUtils.getCurrentUnixTime());
            banner.setSort(0);

            bannerService.insert(banner);

            BannerLogic.Sort(banner.getBannerId(),true);
        }

        return  RestResponseBo.ok("操作成功");
    }

    /**
     * 修改状态
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/status", method = RequestMethod.POST)
    @ResponseBody
    public RestResponseBo status(HttpServletRequest request) {

        int id = Integer.parseInt(request.getParameter("id"));
        String method = request.getParameter("method");

        Banner banner = bannerService.selectById(id);

        if (method.equals("ifpass")) {
            banner.setIfpass(!banner.getIfpass());
            bannerService.updateById(banner);
        }

        if (method.equals("delete")) {
            banner.setDeleteTime(DateUtils.getCurrentUnixTime());
            bannerService.updateById(banner);
        }
        if (method.equals("up") || method.equals("down")) {
            BannerLogic.Sort(id, method.equals("up"));
        }

        return RestResponseBo.ok("操作成功");

    }



}
