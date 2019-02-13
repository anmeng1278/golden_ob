package com.jsj.member.ob.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

public abstract class BaseDto {

    /**
     * 创建时间
     */

    private Integer createTime;
    /**
     * 更新时间
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Integer updateTime;
    /**
     * 删除时间
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Integer deleteTime;


    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Integer deleteTime) {
        this.deleteTime = deleteTime;
    }
}
