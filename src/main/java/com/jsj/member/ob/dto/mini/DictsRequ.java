package com.jsj.member.ob.dto.mini;

import com.jsj.member.ob.enums.DictType;
import io.swagger.annotations.ApiModelProperty;

public class DictsRequ {

    @ApiModelProperty(value = "字典类型", required = true)
    private DictType dictType;

    @ApiModelProperty(value = "获取数量，默认3个", required = true)
    private int count;

    public DictType getDictType() {
        return dictType;
    }

    public void setDictType(DictType dictType) {
        this.dictType = dictType;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
