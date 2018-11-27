package com.jsj.member.ob.controller.index;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jsj.member.ob.entity.Dict;
import com.jsj.member.ob.enums.SecKillStatus;
import com.jsj.member.ob.logic.ActivityLogic;
import com.jsj.member.ob.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
    DictService dictService;

    @GetMapping(value = {""})
    @ResponseBody
    public String index(HttpServletRequest request) {

        List<Dict> dicts = dictService.selectList(new EntityWrapper<>());
        return JSON.toJSONString(dicts);

    }

    /*
    测试秒杀
     */
    @GetMapping(value = {"/seckill"})
    @ResponseBody
    public String seckill(HttpServletRequest request) {

        String openId = new Random().nextDouble() + "";
        SecKillStatus secKillStatus = ActivityLogic.RedisKill(1, 3, 4, openId);

        return secKillStatus.getMessage();

    }

    /*
        测试秒杀
         */
    @GetMapping(value = {"/seckill/reset"})
    @ResponseBody
    public String seckillReset(HttpServletRequest request) {

        ActivityLogic.soldOutMap.clear();
        return "重置成功";

    }

}
