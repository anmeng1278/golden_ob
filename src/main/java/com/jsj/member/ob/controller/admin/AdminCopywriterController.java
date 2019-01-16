package com.jsj.member.ob.controller.admin;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.dto.RestResponseBo;
import com.jsj.member.ob.entity.Copywriter;
import com.jsj.member.ob.entity.Dict;
import com.jsj.member.ob.enums.DictType;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.DictLogic;
import com.jsj.member.ob.service.CopywriterService;
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
import java.util.List;

@ApiIgnore
@Controller
@RequestMapping("/admin/copywriter")
public class AdminCopywriterController {

    @Autowired
    CopywriterService copywriterService;

    /**
     * 文案列表
     * @param page
     * @param limit
     * @param keys
     * @param typeId
     * @param model
     * @return
     */
    @GetMapping(value = {"", "/index"})
    public String index(@RequestParam(value = "page", defaultValue = "1") Integer page,
                        @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                        @RequestParam(value = "keys", defaultValue = "") String keys,
                        @RequestParam(value = "typeId", defaultValue = "0") Integer typeId,Model model) {
        EntityWrapper<Copywriter> wrapper = new EntityWrapper<>();
        wrapper.where("delete_time is null");

        if(typeId > 0){
            wrapper.where("type_id={0}",typeId);
        }

        wrapper.where(!StringUtils.isBlank(keys),"remark like contact(concat('%',{0}),'%') )",keys);

        wrapper.orderBy("create_time desc");

        Page<Copywriter> pageInfo = new Page<>(page,limit);
        Page<Copywriter> pp = copywriterService.selectPage(pageInfo, wrapper);

        List<Dict> copywriterTypes = DictLogic.GetDicts(DictType.COPYWRITER);

        model.addAttribute("infos",new CCPage<Copywriter>(pp,limit));
        model.addAttribute("typeId",typeId);
        model.addAttribute("keys",keys);
        model.addAttribute("copywriterTypes",copywriterTypes);

        return "admin/copywriter/index";

    }


    /**
     * 查询信息页面
     *
     * @param copywriterId
     * @param model
     * @return
     */
    @GetMapping("/{copywriterId}")
    public String info(@PathVariable("copywriterId") Integer copywriterId, Model model) {

        Copywriter copywriter = new Copywriter();
        if (copywriterId > 0) {
            copywriter = copywriterService.selectById(copywriterId);
        }

        List<Dict> copywriterTypes = DictLogic.GetDicts(DictType.COPYWRITER);

        model.addAttribute("copywriterTypes", copywriterTypes);
        model.addAttribute("info", copywriter);
        model.addAttribute("copywriterId", copywriterId);

        return "admin/copywriter/info";
    }

    /**
     * 修改或添加
     * @param copywriterId
     * @param request
     * @return
     */
    @RequestMapping(value = "/{copywriterId}", method = RequestMethod.POST)
    @ResponseBody
    @Transactional(Constant.DBTRANSACTIONAL)
    public RestResponseBo saveOrUpdate(@PathVariable("copywriterId") Integer copywriterId, HttpServletRequest request){
        Copywriter copywriter = new Copywriter();

        String remark = request.getParameter("remark");
        boolean ifpass = !StringUtils.isBlank(request.getParameter("ifpass"));
        String beginTime = request.getParameter("beginTime");
        String endTime = request.getParameter("endTime");
        int typeId = Integer.parseInt(request.getParameter("typeId"));

        int beginTimeUnix = DateUtils.getUnixTimeByDate(DateUtils.dateFormat(beginTime, "yyyy-MM-dd HH:mm:ss"));
        int endTimeUnix = DateUtils.getUnixTimeByDate(DateUtils.dateFormat(endTime, "yyyy-MM-dd HH:mm:ss"));

        if(remark.length() > 255){
            throw new TipException("文案内容不能超过255个字符");
        }

        if(copywriterId > 0){
            //修改
            copywriter = copywriterService.selectById(copywriterId);
            copywriter.setRemark(remark);
            copywriter.setIfpass(ifpass);
            copywriter.setBeginTime(beginTimeUnix);
            copywriter.setEndTime(endTimeUnix);
            copywriter.setUpdateTime(DateUtils.getCurrentUnixTime());

            copywriterService.updateById(copywriter);
        }else {
            //添加
            copywriter.setRemark(remark);
            copywriter.setIfpass(ifpass);
            copywriter.setTypeId(typeId);
            copywriter.setBeginTime(beginTimeUnix);
            copywriter.setEndTime(endTimeUnix);
            copywriter.setUpdateTime(DateUtils.getCurrentUnixTime());
            copywriter.setCreateTime(DateUtils.getCurrentUnixTime());

            copywriterService.insert(copywriter);
        }

        return RestResponseBo.ok("操作成功");
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

        Copywriter copywriter = copywriterService.selectById(id);

        if (method.equals("ifpass")) {
            copywriter.setIfpass(!copywriter.getIfpass());
            copywriterService.updateById(copywriter);
        }

        if (method.equals("delete")) {
            copywriter.setDeleteTime(DateUtils.getCurrentUnixTime());
            copywriterService.updateById(copywriter);
        }
        return RestResponseBo.ok("操作成功");

    }


}
