package com.jsj.member.ob.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *   @description : Wechat 微信用户实体类
 *   ---------------------------------
 * 	 @author cc
 *   @since 2018-10-29
 */
@TableName("_wechat")
public class Wechat implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * openid
     */
    @TableId("open_id")
	private String openId;
    /**
     * 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段
     */
	@TableField("union_id")
	private String unionId;
	private Integer jsjid;
    /**
     * 微信公众号 1: 空铁管家
     */
	@TableField("official_accounts")
	private Integer officialAccounts;
    /**
     * 是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息，只有openid和UnionID（在该公众号绑定到了微信开放平台账号时才有）
     */
	private Integer subscribe;
    /**
     * 昵称
     */
	private String nickname;
    /**
     * 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
     */
	private Integer sex;
    /**
     * 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。
     */
	private String headimgurl;
    /**
     * 关注时间
     */
	@TableField("subscribe_time")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date subscribeTime;
    /**
     * 用户所在城市
     */
	private String city;
    /**
     * 用户所在省份
     */
	private String province;
    /**
     * 用户所在国家
     */
	private String country;
    /**
     * 用户的语言，简体中文为zh_CN
     */
	private String language;
    /**
     * 备注
     */
	private String remarks;
    /**
     * 来源：0：正常关注, 1:扫码关注, 2:oauth
     */
	private Integer source;
    /**
     * 取消关注时间
     */
	@TableField("unsubscribe_time")
	private Integer unsubscribeTime;
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


	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getUnionId() {
		return unionId;
	}

	public void setUnionId(String unionId) {
		this.unionId = unionId;
	}

	public Integer getJsjid() {
		return jsjid;
	}

	public void setJsjid(Integer jsjid) {
		this.jsjid = jsjid;
	}

	public Integer getOfficialAccounts() {
		return officialAccounts;
	}

	public void setOfficialAccounts(Integer officialAccounts) {
		this.officialAccounts = officialAccounts;
	}

	public Integer getSubscribe() {
		return subscribe;
	}

	public void setSubscribe(Integer subscribe) {
		this.subscribe = subscribe;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public Date getSubscribeTime() {
		return subscribeTime;
	}

	public void setSubscribeTime(Date subscribeTime) {
		this.subscribeTime = subscribeTime;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public Integer getUnsubscribeTime() {
		return unsubscribeTime;
	}

	public void setUnsubscribeTime(Integer unsubscribeTime) {
		this.unsubscribeTime = unsubscribeTime;
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
		return "Wechat{" +
			", openId=" + openId +
			", unionId=" + unionId +
			", jsjid=" + jsjid +
			", officialAccounts=" + officialAccounts +
			", subscribe=" + subscribe +
			", nickname=" + nickname +
			", sex=" + sex +
			", headimgurl=" + headimgurl +
			", subscribeTime=" + subscribeTime +
			", city=" + city +
			", province=" + province +
			", country=" + country +
			", language=" + language +
			", remarks=" + remarks +
			", source=" + source +
			", unsubscribeTime=" + unsubscribeTime +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", deleteTime=" + deleteTime +
			"}";
	}
}
