package com.jsj.member.ob.controller.index;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jsj.member.ob.entity.Dict;
import com.jsj.member.ob.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
}
