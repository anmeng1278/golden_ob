package com.jsj.member.ob.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *   @description : StockFlow 库存流转表实体类
 *   ---------------------------------
 * 	 @author cc
 *   @since 2018-10-30
 */
@TableName("_stock_flow")
public class StockFlow implements Serializable {

    private static final long serialVersionUID = 1L;

	@TableId(value="stock_flow_id", type= IdType.AUTO)
	private Integer stockFlowId;
    /**
     * 库存编号
     */
	@TableField("stock_id")
	private Integer stockId;
    /**
     * 用户openid
     */
	@TableField("open_id")
	private String openId;
    /**
     * 流转名称
     */
	@TableField("flow_name")
	private String flowName;
    /**
     * 父库存编号
     */
	@TableField("parent_stock_id")
	private Integer parentStockId;
    /**
     * 备注
     */
	private String remark;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Integer createTime;
    /**
     * 更新时间
     */
	@TableField("update_time")
	private Integer updateTime;
    /**
     * 删除时间
     */
	@TableField("delete_time")
	private Integer deleteTime;


	public Integer getStockFlowId() {
		return stockFlowId;
	}

	public void setStockFlowId(Integer stockFlowId) {
		this.stockFlowId = stockFlowId;
	}

	public Integer getStockId() {
		return stockId;
	}

	public void setStockId(Integer stockId) {
		this.stockId = stockId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getFlowName() {
		return flowName;
	}

	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

	public Integer getParentStockId() {
		return parentStockId;
	}

	public void setParentStockId(Integer parentStockId) {
		this.parentStockId = parentStockId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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


	@Override
	public String toString() {
		return "StockFlow{" +
			", stockFlowId=" + stockFlowId +
			", stockId=" + stockId +
			", openId=" + openId +
			", flowName=" + flowName +
			", parentStockId=" + parentStockId +
			", remark=" + remark +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", deleteTime=" + deleteTime +
			"}";
	}
}
