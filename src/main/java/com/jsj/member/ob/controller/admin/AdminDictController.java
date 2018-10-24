package com.jsj.member.ob.controller.admin;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.base.Supplier;
import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.dto.RestResponseBo;
import com.jsj.member.ob.entity.Dict;
import com.jsj.member.ob.entity.Product;
import com.jsj.member.ob.enums.DictType;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.form.DictForm;
import com.jsj.member.ob.logic.DictLogic;
import com.jsj.member.ob.service.DictService;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@ApiIgnore
@Controller
@RequestMapping("/admin/dict")
public class AdminDictController {

    @Autowired
    private DictService dictService;

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
                        @RequestParam(value = "keys", defaultValue = " ") String keys,
                        Model model) {
        Page<Dict> pageInfo = new Page<>(page, limit);
        Page<Dict> pp = dictService.selectPage(pageInfo, new EntityWrapper<Dict>().where("parent_dict_id is not null and delete_time is null"));

        //所属类型
        List<DictType> dictTypes = Arrays.asList(DictType.values());
        model.addAttribute("infos", new CCPage<Dict>(pp, limit));
        model.addAttribute("keys", keys);
        model.addAttribute("dictTypes", dictTypes);

        return "admin/dict/index";
    }

    /**
     * 查询信息页面
     *
     * @param dictId
     * @param model
     * @return
     */
    @GetMapping("/{dictId}")
    public String info(@PathVariable("dictId") Integer dictId, Model model) {

        Dict dict = new Dict();
        if (dictId > 0) {
            dict = DictLogic.GetDict(dictId);
        }

        List<DictType> dictTypes = Arrays.asList(DictType.values());

        model.addAttribute("dictTypes", dictTypes);
        model.addAttribute("info", dict);
        model.addAttribute("dictId", dictId);

        return "admin/dict/info";
    }

    /**
     * 根据id删除一条记录
     *
     * @param dictId
     * @param model
     * @return
     */
    @RequestMapping("/delete/{dictId}")
    public String delete(@PathVariable("dictId") Integer dictId, Model model) {
        Dict dict = DictLogic.GetDict(dictId);

        dict.setDeleteTime(DateUtils.getCurrentUnixTime());
        dictService.updateById(dict);

        return "redirect:/admin/dict/index";
    }

    /**
     * 修改或添加
     *
     * @param form
     * @param model
     * @return
     */
    @PostMapping("/{dictId}")
    @ResponseBody
    @Transactional(Constant.DBTRANSACTIONAL)
    public RestResponseBo saveOrUpdate(@PathVariable("dictId") Integer dictId, @Valid DictForm form, Model model) {

        Dict dict = new Dict();

        if (dictId > 0) { //修改
            dict = DictLogic.GetDict(dictId);
            dict.setUpdateTime(DateUtils.getCurrentUnixTime());
            form.setDictType(dict.getDictType());
            BeanUtils.copyProperties(form, dict);
            dictService.updateById(dict);
        } else { //添加
            form.setDictType("0");
            dict.setCreateTime(DateUtils.getCurrentUnixTime());
            dict.setUpdateTime(DateUtils.getCurrentUnixTime());
            BeanUtils.copyProperties(form, dict);
            dictService.insert(dict);
        }
        return RestResponseBo.ok("保存成功");
    }

    /**
     * 根据下拉框所属类型和关键字搜索
     *
     * @param keys
     * @param dictTypeId
     * @param model
     * @return
     */
    @GetMapping("/getByCondition")
    public String getByCondition(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                 @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                                 @RequestParam("keys") String keys, @RequestParam("dictTypeId") Integer dictTypeId, Model model) {

        EntityWrapper<Dict> wrapper = new EntityWrapper<>();
        //根据所属类型进行搜索
        if (!StringUtils.isBlank(dictTypeId + "") && StringUtils.isBlank(keys)) {
            wrapper.where("parent_dict_id={0} and delete_time is null", dictTypeId);
        } else if (dictTypeId == null && !StringUtils.isBlank(keys)) {  //根据关键字进行搜索
            wrapper.where("delete_time is null").like("dict_name", "keys");
        } else {
            wrapper.where("parent_dict_id={0} and delete_time is null", dictTypeId).like("dict_name", "keys");
        }

        Page<Dict> pageInfo = new Page<>(page, limit);
        Page<Dict> pp = dictService.selectPage(pageInfo, wrapper);

        /*List<Dict> dicts = dictService.selectList(wrapper);*/

        //所属类型
        List<DictType> dictTypes = Arrays.asList(DictType.values());

        model.addAttribute("infos", new CCPage<Dict>(pp,limit));
        model.addAttribute("dictTypes", dictTypes);
        return "admin/dict/index";
    }

    /**
     * 修改状态
     *
     * @param dictId
     * @param request
     * @return
     */
    @RequestMapping(value = "/status", method = RequestMethod.POST)
    @ResponseBody
    public RestResponseBo modifyStatus(@RequestParam(value = "dictId", defaultValue = "0") Integer dictId,
                                       HttpServletRequest request) {

        String method = request.getParameter("method");
        if (StringUtils.isBlank(method)) {
            throw new TipException("方法名不能为空");
        }
        Dict dict = dictService.selectById(dictId);
        dict.setUpdateTime(DateUtils.getCurrentUnixTime());
        return RestResponseBo.ok("修改成功");

    }

}
