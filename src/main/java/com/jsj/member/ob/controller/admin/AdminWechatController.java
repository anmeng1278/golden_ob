package com.jsj.member.ob.controller.admin;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jsj.member.ob.entity.Wechat;
import com.jsj.member.ob.service.WechatService;
import com.jsj.member.ob.utils.CCPage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Controller
@RequestMapping("/admin/wechat")
public class AdminWechatController {

    @Autowired
    private WechatService wechatService;

    /**
     * 查询用户列表
     * @param page
     * @param limit
     * @param keys
     * @param model
     * @return
     */
    @GetMapping(value = {"","/index"})
    public String index(@RequestParam(value = "page", defaultValue = "1") Integer page,
                        @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                        @RequestParam(value = "keys", defaultValue = "") String keys,Model model) {

        EntityWrapper<Wechat> wrapper = new EntityWrapper<>();
        wrapper.where("delete_time is null");
        wrapper.where(!StringUtils.isBlank(keys), "(nickname LIKE concat(concat('%',{0}),'%') or open_id like concat(concat('%',{0}),'%') )", keys);
        wrapper.orderBy("create_time desc");

        Page<Wechat> pageInfo = new Page<>(page, limit);
        Page<Wechat> pp = wechatService.selectPage(pageInfo,wrapper);

        model.addAttribute("keys", keys);
        model.addAttribute("infos", new CCPage<Wechat>(pp, limit));

        return "admin/wechat/index";

    }

}
