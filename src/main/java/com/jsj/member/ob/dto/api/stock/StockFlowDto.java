package com.jsj.member.ob.dto.api.stock;

public class StockFlowDto {

    private String openId;
    private Integer createTime;
    private String nickName;
    private Integer stockId;
    private Integer parentStockId;
    private String flowName;
    private String remark;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getStockId() {
        return stockId;
    }

    public void setStockId(Integer stockId) {
        this.stockId = stockId;
    }

    public Integer getParentStockId() {
        return parentStockId;
    }

    public void setParentStockId(Integer parentStockId) {
        this.parentStockId = parentStockId;
    }

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
