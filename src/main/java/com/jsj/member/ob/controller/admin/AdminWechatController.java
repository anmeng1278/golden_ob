package com.jsj.member.ob.controller.admin;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jsj.member.ob.dto.api.wechat.WechatDto;
import com.jsj.member.ob.entity.Dict;
import com.jsj.member.ob.entity.Wechat;
import com.jsj.member.ob.enums.DictType;
import com.jsj.member.ob.enums.WechatType;
import com.jsj.member.ob.service.WechatService;
import com.jsj.member.ob.utils.CCPage;
import org.bouncycastle.math.raw.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;
import java.util.List;

@ApiIgnore
@Controller
@RequestMapping("/admin/wechat")
public class AdminWechatController {

    @Autowired
    private WechatService wechatService;

    /**
     * 查询所有用户列表
     * @param page 当前页
     * @param limit 每页显示条数
     * @param model
     * @return
     */

    @GetMapping(value = {"","/index"})
    public String index(@RequestParam(value = "page", defaultValue = "1") Integer page,
                        @RequestParam(value = "limit", defaultValue = "10") Integer limit,Model model) {
        Page<Wechat> pageInfo = new Page<>(page, limit);
        Page<Wechat> pp = wechatService.selectPage(pageInfo, new EntityWrapper<Wechat>().where("delete_time is null"));

        //遍历枚举得出来源
        List<WechatType> wechatTypes = Arrays.asList(WechatType.values());
        model.addAttribute("infos", new CCPage<Wechat>(pp, limit));
        model.addAttribute("wechatTypes", wechatTypes);

        return "admin/wechat/index";
    }

}
