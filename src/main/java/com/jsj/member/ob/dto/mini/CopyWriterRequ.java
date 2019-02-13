package com.jsj.member.ob.dto.mini;

import io.swagger.annotations.ApiModelProperty;

public class CopyWriterRequ {

    @ApiModelProperty(value = "分享文案类型编号", required = true)
    private int typeId;

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }
}
