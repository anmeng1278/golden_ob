package com.jsj.member.ob.logic;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jsj.member.ob.dto.api.wechat.WechatDto;
import com.jsj.member.ob.entity.Wechat;
import com.jsj.member.ob.entity.WechatRelation;
import com.jsj.member.ob.enums.SourceType;
import com.jsj.member.ob.service.WechatRelationService;
import com.jsj.member.ob.service.WechatService;
import com.jsj.member.ob.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import weixin.popular.bean.user.User;

import javax.annotation.PostConstruct;

@Component
public class WechatLogic extends BaseLogic {

    public static WechatLogic wechatLogic;

    @Autowired
    WechatService wechatService;

    @Autowired
    WechatRelationService wechatRelationService;

    @PostConstruct
    public void init() {
        wechatLogic = this;
    }


    //region (public) 获取微信用户信息 GetWechant

    /**
     * 获取微信用户信息
     *
     * @param openId
     * @return
     */
    public static WechatDto GetWechat(String openId) {

        WechatDto dto = new WechatDto();
        dto.setOpenId(openId);

        Wechat wechat = wechatLogic.wechatService.selectById(openId);
        if (wechat == null) {
            dto.setNickname("匿名");
            return dto;
        }


        dto.setCity(wechat.getCity());
        dto.setCountry(wechat.getCountry());
        dto.setCreateTime(wechat.getCreateTime());
        dto.setDeleteTime(wechat.getDeleteTime());
        dto.setHeadimgurl(wechat.getHeadimgurl());

        dto.setJsjid(wechat.getJsjid());
        dto.setLanguage(wechat.getLanguage());
        dto.setNickname(wechat.getNickname());
        dto.setOfficialAccounts(wechat.getOfficialAccounts());
        dto.setOpenId(wechat.getOpenId());
        dto.setProvince(wechat.getProvince());

        dto.setRemarks(wechat.getRemarks());
        dto.setSex(wechat.getSex());
        dto.setSource(wechat.getSource());
        dto.setSubscribe(wechat.getSubscribe());
        dto.setSubscribeTime(wechat.getSubscribeTime());

        dto.setUnionId(wechat.getUnionId());
        dto.setUnsubscribeTime(wechat.getUnsubscribeTime());
        dto.setUpdateTime(wechat.getUpdateTime());

        return dto;

    }
    //endregion

    //region (public) 获取新增用户 GetNewUsers

    /**
     * 获取新增用户
     *
     * @return
     */
    public static Integer GetNewUsers() {
        EntityWrapper<Wechat> wechatWrapper = new EntityWrapper<>();

        wechatWrapper.where("delete_time is null and unsubscribe_time is null and create_time={0}", DateUtils.getCurrentUnixTime());

        return wechatLogic.wechatService.selectCount(wechatWrapper);
    }
    //endregion

    //region (public) 初始化会员数据 Init

    /**
     * 初始化会员数据
     *
     * @param user
     * @param jsjId
     * @return
     */
    public static void Init(User user, int jsjId) {

        Wechat wechat = wechatLogic.wechatService.selectById(user.getOpenid());

        if (wechat == null) {

            wechat = new Wechat();

            wechat.setCity(user.getCity());
            wechat.setCountry(user.getCountry());
            wechat.setCreateTime(DateUtils.getCurrentUnixTime());
            wechat.setHeadimgurl(user.getHeadimgurl());
            wechat.setJsjid(jsjId);

            wechat.setLanguage(user.getLanguage());
            wechat.setNickname(user.getNickname());
            wechat.setOfficialAccounts(1);
            wechat.setOpenId(user.getOpenid());
            wechat.setProvince(user.getProvince());

            wechat.setRemarks(user.getRemark());
            wechat.setSex(user.getSex());
            wechat.setSource(0);
            wechat.setSubscribe(user.getSubscribe());
            wechat.setSubscribeTime(user.getSubscribe_time());

            wechat.setUnionId(user.getUnionid());
            wechat.setUnsubscribeTime(0);
            wechat.setUpdateTime(DateUtils.getCurrentUnixTime());

            wechatLogic.wechatService.insert(wechat);
        } else {

            wechat.setCity(user.getCity());
            wechat.setCountry(user.getCountry());
            wechat.setHeadimgurl(user.getHeadimgurl());
            wechat.setJsjid(jsjId);

            wechat.setLanguage(user.getLanguage());
            wechat.setNickname(user.getNickname());
            wechat.setOpenId(user.getOpenid());
            wechat.setProvince(user.getProvince());

            wechat.setRemarks(user.getRemark());
            wechat.setSex(user.getSex());
            wechat.setSubscribe(user.getSubscribe());
            wechat.setSubscribeTime(user.getSubscribe_time());
            wechat.setUpdateTime(DateUtils.getCurrentUnixTime());

            wechatLogic.wechatService.updateById(wechat);

        }
    }

    //endregion

    //region (public) 绑定会员关系 BindRelation

    /**
     * 绑定会员关系
     *
     * @param openId
     * @param relationOpenId
     * @param sourceType
     */
    public static void BindRelation(String openId, String relationOpenId, SourceType sourceType) {

        if (StringUtils.isEmpty(openId)) {
            return;
        }
        if (StringUtils.isEmpty(relationOpenId)) {
            return;
        }

        Wrapper<WechatRelation> wrapper = new EntityWrapper<>();
        wrapper.where("open_id = {0}", openId);
        wrapper.where("relation_open_id = {0}", relationOpenId);
        wrapper.where("type_id = {0}", sourceType.getValue());

        WechatRelation wechatRelation = wechatLogic.wechatRelationService.selectOne(wrapper);
        //已绑定
        if (wechatRelation != null) {
            return;
        }

        wechatRelation = new WechatRelation();
        wechatRelation.setOpenId(openId);
        wechatRelation.setRelationOpenId(relationOpenId);
        wechatRelation.setTypeId(sourceType.getValue());
        wechatRelation.setCreateTime(DateUtils.getCurrentUnixTime());
        wechatRelation.setUpdateTime(DateUtils.getCurrentUnixTime());

        wechatLogic.wechatRelationService.insert(wechatRelation);

    }
    //endregion

    //region (public) 绑定会员编号 BindJSJId

    /**
     * 绑定会员编号
     *
     * @param openId
     * @param jsjId
     */
    public static Integer BindJSJId(String openId, int jsjId) {

        Wechat wechat = wechatLogic.wechatService.selectById(openId);
        if (wechat.getJsjid() == null || wechat.getJsjid() == 0) {
            wechat.setJsjid(jsjId);
            wechatLogic.wechatService.updateById(wechat);
        }

        return wechat.getJsjid();
    }
    //endregion

    //region (public) 根据openId获取绑定关系 GetWechatRelation

    /**
     * 根据openId获取绑定关系
     *
     * @param openId
     * @param sourceType
     * @return
     */
    public static WechatRelation GetWechatRelation(String openId, SourceType sourceType) {

        Wrapper<WechatRelation> wrapper = new EntityWrapper<>();
        wrapper.where("open_id = {0}", openId);
        wrapper.where("type_id = {0}", sourceType.getValue());

        WechatRelation wechatRelation = wechatLogic.wechatRelationService.selectOne(wrapper);
        return wechatRelation;

    }
    //endregion

}
