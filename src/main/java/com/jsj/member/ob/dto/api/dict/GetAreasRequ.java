package com.jsj.member.ob.dto.api.dict;

/**
 * 获取省市区列表
 */
public class GetAreasRequ {

    /**
     * 父编号，当传0时获取省市
     */
    private int parentAreaId;

    public int getParentAreaId() {
        return parentAreaId;
    }

    public void setParentAreaId(int parentAreaId) {
        this.parentAreaId = parentAreaId;
    }
}
