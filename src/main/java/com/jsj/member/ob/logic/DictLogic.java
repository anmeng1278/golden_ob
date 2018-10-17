package com.jsj.member.ob.logic;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jsj.member.ob.entity.Dict;
import com.jsj.member.ob.enums.DictType;
import com.jsj.member.ob.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class DictLogic {

    public static DictLogic dictLogic;

    // 初始化的时候，将本类中的sysConfigManager赋值给静态的本类变量
    @PostConstruct
    public void init() {
        dictLogic = this;
        dictLogic.dictService = this.dictService;
    }

    @Autowired
    DictService dictService;

    /**
     * 权益父编号获取分类列表
     *
     * @param dictType
     * @return
     */
    public static List<Dict> GetDicts(DictType dictType) {

        EntityWrapper<Dict> entityWrapper = new EntityWrapper<>();

        entityWrapper.where("parent_dict_id={0}", dictType.getValue());
        entityWrapper.where("delete_time is null");
        entityWrapper.orderBy("sort asc, update_time desc");

        return dictLogic.dictService.selectList(entityWrapper);

    }
}
