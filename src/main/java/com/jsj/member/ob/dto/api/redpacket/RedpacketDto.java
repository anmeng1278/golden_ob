package com.jsj.member.ob.dto.api.redpacket;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.jsj.member.ob.enums.RedpacketType;

import java.util.List;

public class RedpacketDto {

    private Integer redpacketId;
    /**
     * 礼包名称
     */
    private String redpacketName;
    /**
     * 礼包类型 1.券类红包
     */
    private Integer typeId;

    private RedpacketType redpacketType;

    private Integer beginTime;

    private Integer endTime;
    /**
     * 是否审核
     */
    private Boolean ifpass;

    /**
     * 创建时间
     */
    private Integer createTime;
    /**
     * 更新时间
     */
    private Integer updateTime;
    /**
     * 删除时间
     */
    private Integer deleteTime;

    private List<RedpacketCouponDto>  redpacketCouponDtos;

    public Integer getRedpacketId() {
        return redpacketId;
    }

    public void setRedpacketId(Integer redpacketId) {
        this.redpacketId = redpacketId;
    }

    public String getRedpacketName() {
        return redpacketName;
    }

    public void setRedpacketName(String redpacketName) {
        this.redpacketName = redpacketName;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public RedpacketType getRedpacketType() {
        return redpacketType;
    }

    public void setRedpacketType(RedpacketType redpacketType) {
        this.redpacketType = redpacketType;
    }

    public Integer getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Integer beginTime) {
        this.beginTime = beginTime;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    public Boolean getIfpass() {
        return ifpass;
    }

    public void setIfpass(Boolean ifpass) {
        this.ifpass = ifpass;
    }


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
