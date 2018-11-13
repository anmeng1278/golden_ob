package com.jsj.member.ob.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *   @description : ProductImg 商品图片表实体类
 *   ---------------------------------
 * 	 @author cc
 *   @since 2018-11-13
 */
@TableName("_product_img")
public class ProductImg implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId(value="product_img_id", type= IdType.AUTO)
	private Integer productImgId;
    /**
     * 商品编号
     */
	@TableField("product_id")
	private Integer productId;
    /**
     * 图片地址
     */
	@TableField("img_path")
	private String imgPath;
    /**
     * 类型 0 封面  1 商品图片
     */
	@TableField("type_id")
	private Integer typeId;
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


	public Integer getProductImgId() {
		return productImgId;
	}

	public void setProductImgId(Integer productImgId) {
		this.productImgId = productImgId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
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
		return "ProductImg{" +
			", productImgId=" + productImgId +
			", productId=" + productId +
			", imgPath=" + imgPath +
			", typeId=" + typeId +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", deleteTime=" + deleteTime +
			"}";
	}
}
