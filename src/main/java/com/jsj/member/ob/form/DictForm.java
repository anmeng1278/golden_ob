package com.jsj.member.ob.form;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

public class DictForm {
    /**
     * 主键
     */
    private Integer dictId;
    /**
     * 名称
     */
    private String dictName;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 类型
     */
    private String dictType;
    /**
     * 所属类型
     */
    private Integer parentDictId;
    /**
     * 排序
     */
    private Integer sort;

    public Integer getDictId() {
        return dictId;
    }

    public void setDictId(Integer dictId) {
        this.dictId = dictId;
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    public Integer getParentDictId() {
        return parentDictId;
    }

    public void setParentDictId(Integer parentDictId) {
        this.parentDictId = parentDictId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
