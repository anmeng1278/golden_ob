package com.jsj.member.ob.logic;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jsj.member.ob.dto.api.dict.DictDto;
import com.jsj.member.ob.dto.api.dict.DictResp;
import com.jsj.member.ob.entity.Dict;
import com.jsj.member.ob.enums.DictType;
import com.jsj.member.ob.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
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
     * 获取字典详情信息
     *
     * @param dictId
     * @return
     */
    public static Dict GetDict(int dictId) {
        return dictLogic.dictService.selectById(dictId);
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

    /**
     * 获取省份和直辖市列表
     *
     * @return
     */
    public static DictResp GetProvince() {
        DictResp dictResp = new DictResp();
        List<DictDto> dictDtoList = new ArrayList<>();
        EntityWrapper<Dict> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("delete_time is null and parent_dict_id=50000");
        List<Dict> dictList = dictLogic.dictService.selectList(entityWrapper);
        for (Dict dict : dictList) {

            DictDto dictDto = new DictDto();
            if (dict.getDictId() % 10000 == 0) {
                dictDto.setDictId(dict.getDictId());
                dictDto.setDictName(dict.getDictName());
                dictDtoList.add(dictDto);
            }
            dictResp.setDictDtoList(dictDtoList);
        }
        return dictResp;
    }

    /**
     * 获取下一级列表
     *
     * @param dictId
     * @return
     */
    public static DictResp GetArea(int dictId) {
        DictResp dictResp = new DictResp();
        List<DictDto> dictDtoList = new ArrayList<>();

        EntityWrapper<Dict> entityWrapper = new EntityWrapper<>();
        //int startflag=dictId;
        //int endflag=dictId;
        //int flag=0;
        //if (dictId % 10000==0)//省或直辖市 查市
        //{
        //    endflag = dictId+9999;
        //    flag=100;
        //}
        //else if (dictId%100==0)//市 查区
        //{
        //    endflag = dictId+99;
        //    flag=0;
        //}
        //entityWrapper.where("delete_time is null and parent_dict_id=50000 and dict_id>"
        //        +startflag+" and dict_id<="
        //        +endflag +(flag==0?"":" and dict_id%"+flag+"=0"));

        entityWrapper.where("delete_time is null and parent_dict_id=50000 ");
        //获取所有数据
        List<Dict> dictList = dictLogic.dictService.selectList(entityWrapper);
        int type=0;//0 默认 1 省下市 2 直辖市下区 3 省辖市下区
        if(dictId % 10000 == 0){//省查市，没有市==》直辖市
            type=1;
            if(!dictList.stream().anyMatch(t->t.getDictId()%100==0)){
                type=2;
            }
        }else{
            type=3;
        }

        for (Dict dict : dictList) {
            DictDto dictDto = new DictDto();

            if(type==1){
                if (dictId / 10000 == dict.getDictId() / 10000 && dict.getDictId() % 100 == 0 ) {
                     //省份下的市
                    dictDto.setDictId(dict.getDictId());
                    dictDto.setDictName(dict.getDictName());
                    dictDtoList.add(dictDto);
                    dictResp.setDictDtoList(dictDtoList);
                    }

            }else if(type==2){
                if (dict.getDictId() % 100 != 0) { //直辖市的区
                    dictDto.setDictId(dict.getDictId());
                    dictDto.setDictName(dict.getDictName());
                    dictDtoList.add(dictDto);
                    dictResp.setDictDtoList(dictDtoList);
                }

            }else if(type==3){
                if (dict.getDictId() / 100 == dictId / 100 && dictId % 100 == 0 && dict.getDictId() % 100 != 0) {
                    dictDto.setDictId(dict.getDictId());
                    dictDto.setDictName(dict.getDictName());
                    dictDtoList.add(dictDto);
                    dictResp.setDictDtoList(dictDtoList);
                }

            }
            /**
            //得到省份的下一级
            if (dictId / 10000 == dict.getDictId() / 10000 && dict.getDictId() % 10000 != 0 && dictId % 10000 == 0) {
                if (dict.getDictId() % 100 == 0) { //省份下的市
                    dictDto.setDictId(dict.getDictId());
                    dictDto.setDictName(dict.getDictName());
                    dictDtoList.add(dictDto);

                } else if (dict.getDictId() % 100 != 0) { //直辖市的区
                    dictDto.setDictId(dict.getDictId());
                    dictDto.setDictName(dict.getDictName());
                    dictDtoList.add(dictDto);
                }
            }

            //得到市下边的区
            if (dict.getDictId() / 100 == dictId / 100 && dictId % 100 == 0 && dict.getDictId() % 100 != 0) {
                dictDto.setDictId(dict.getDictId());
                dictDto.setDictName(dict.getDictName());
                dictDtoList.add(dictDto);
            }
            */
        }
        return dictResp;
    }




}
