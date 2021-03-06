package com.jsj.member.ob.logic;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jsj.member.ob.dto.api.dict.DictDto;
import com.jsj.member.ob.dto.api.dict.GetAreasRequ;
import com.jsj.member.ob.dto.api.dict.GetAreasResp;
import com.jsj.member.ob.entity.Dict;
import com.jsj.member.ob.entity.VArea;
import com.jsj.member.ob.enums.DictType;
import com.jsj.member.ob.service.DictService;
import com.jsj.member.ob.service.VAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class DictLogic extends BaseLogic {

    public static DictLogic dictLogic;

    // 初始化的时候，将本类中的sysConfigManager赋值给静态的本类变量
    @PostConstruct
    public void init() {
        dictLogic = this;
    }

    @Autowired
    DictService dictService;

    @Autowired
    VAreaService vAreaService;


    /**
     * 获取字典详情信息
     *
     * @param dictId
     * @return
     */
    public static Dict GetDict(int dictId) {
        Dict dict = dictLogic.dictService.selectById(dictId);
        if (dict == null) {
            return new Dict();
        }
        return dict;
    }

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

    public static List<Dict> GetDicts(DictType dictType,int limit){

        EntityWrapper<Dict> entityWrapper = new EntityWrapper<>();

        entityWrapper.where("parent_dict_id={0}", dictType.getValue());
        entityWrapper.where("delete_time is null");
        entityWrapper.orderBy("sort asc, update_time desc");

        Page<Dict> dictPage = dictLogic.dictService.selectPage(new Page<Dict>(1, limit), entityWrapper);

        List<Dict> dicts = new ArrayList<>();

        for (Dict dict : dictPage.getRecords()){
            dicts.add(DictLogic.GetDict(dict.getDictId()));
        }

        return  dicts;

    }

    /**
     * 获得级联列表
     *
     * @param parentAreaId
     * @return
     */
    public static GetAreasResp GetCascade(int parentAreaId) {
        GetAreasRequ requ = new GetAreasRequ();
        requ.setParentAreaId(parentAreaId);
        GetAreasResp getAreasResp = DictLogic.GetAreas(requ);
        return getAreasResp;
    }

    /**
     * 获取省市区
     *
     * @return
     */
    public static GetAreasResp GetAreas(GetAreasRequ requ) {

        //-- 一级省市
        //select * from _v_area where city_id = 0 and area_id = 0;
        //
        //-- 二级城市（方式1）
        //select * from _v_area as a left join _v_area as b on a.city_id = b.dict_id
        //where a.province_id  in (110000, 130000 ) and
        //        ( a.city_id = a.dict_id or ( a.area_id = a.dict_id and b.dict_id is null ))
        //
        //-- 二级城市（方式2）
        //select *
        //        from _v_area
        //where province_id  in (110000, 130000 ) and
        //        ( city_id = dict_id or ( area_id = dict_id and  not exists( select * from _dict where _dict.dict_id = _v_area.city_id ) ))
        //
        //        -- 三级地址
        //select * from _v_area where city_id in (130300) and area_id <> 0

        GetAreasResp resp = new GetAreasResp();
        int parentAreaId = requ.getParentAreaId();

        EntityWrapper<VArea> entityWrapper = new EntityWrapper<>();

        if (parentAreaId == 0) {
            entityWrapper.where("city_id = 0 and area_id = 0");
        } else if (parentAreaId % 10000 == 0) {
            entityWrapper.where("province_id  = {0} and ( city_id = dict_id or ( area_id = dict_id and  not exists( select * from _dict where _dict.dict_id = _v_area.city_id ) ))", parentAreaId);
        } else if (parentAreaId % 100 == 0 && parentAreaId % 10000 != 0) {
            entityWrapper.where("city_id = {0} and area_id <> 0", parentAreaId);
        } else {
            entityWrapper.where("1=0");
        }

        List<VArea> vAreas = dictLogic.vAreaService.selectList(entityWrapper);

        vAreas.forEach(va -> {
            DictDto dto = new DictDto();
            dto.setDictId(va.getDictId());
            dto.setDictName(va.getDictName());
            resp.getAreas().add(dto);
        });
        return resp;

    }

}
