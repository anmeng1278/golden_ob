package com.jsj.member.ob.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *   @description : CartProduct 实体类
 *   ---------------------------------
 * 	 @author cc
 *   @since 2018-10-19
 */
@TableName("_cart_product")
public class CartProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId(value="cart_product_id", type= IdType.AUTO)
	private Integer cartProductId;
    /**
     * 购物车编号
     */
	@TableField("cart_id")
	private Integer cartId;
    /**
     * 商品编号
     */
	@TableField("product_id")
	private Integer productId;
    /**
     * 规格编号
     */
	@TableField("product_spec_id")
	private Integer productSpecId;
    /**
     * 数量
     */
	private Integer number;
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


	public Integer getCartProductId() {
		return cartProductId;
	}

	public void setCartProductId(Integer cartProductId) {
		this.cartProductId = cartProductId;
	}

	public Integer getCartId() {
		return cartId;
	}

	public void setCartId(Integer cartId) {
		this.cartId = cartId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getProductSpecId() {
		return productSpecId;
	}

	public void setProductSpecId(Integer productSpecId) {
		this.productSpecId = productSpecId;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
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
		return "CartProduct{" +
			", cartProductId=" + cartProductId +
			", cartId=" + cartId +
			", productId=" + productId +
			", productSpecId=" + productSpecId +
			", number=" + number +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", deleteTime=" + deleteTime +
			"}";
	}
}
