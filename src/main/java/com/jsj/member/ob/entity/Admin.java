package com.jsj.member.ob.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

/**
 *   @description : Admin 实体类
 *   ---------------------------------
 * 	 @author cc
 *   @since 2018-07-21
 */
@TableName("_admin")
public class Admin implements Serializable {

    private static final long serialVersionUID = 1L;

	@TableId(value="admin_id", type= IdType.AUTO)
	private Integer adminId;
	@TableField("login_name")
	private String loginName;
	private String password;
	@TableField("last_login_ip")
	private String lastLoginIp;
	@TableField("last_login_time")
	private Integer lastLoginTime;
	@TableField("real_name")
	private String realName;
    /**
     * 是否审核
     */
	private Boolean ifpass;
	@TableField("create_time")
	private Integer createTime;
	@TableField("update_time")
	private Integer updateTime;
	@TableField("delete_time")
	private Integer deleteTime;


	public Integer getAdminId() {
		return adminId;
	}

	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	public Integer getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Integer lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
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


	@Override
	public String toString() {
		return "Admin{" +
			", adminId=" + adminId +
			", loginName=" + loginName +
			", password=" + password +
			", lastLoginIp=" + lastLoginIp +
			", lastLoginTime=" + lastLoginTime +
			", realName=" + realName +
			", ifpass=" + ifpass +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", deleteTime=" + deleteTime +
			"}";
	}
}
