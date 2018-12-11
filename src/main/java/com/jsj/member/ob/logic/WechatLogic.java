package com.jsj.member.ob.logic;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jsj.member.ob.dto.api.wechat.WechatDto;
import com.jsj.member.ob.entity.Wechat;
import com.jsj.member.ob.service.WechatService;
import com.jsj.member.ob.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import weixin.popular.bean.user.User;

import javax.annotation.PostConstruct;

@Component
public class WechatLogic extends BaseLogic {

    public static WechatLogic wechatLogic;

    @Autowired
    WechatService wechatService;

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
            dto.setNickname("未知");
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


    /**
     * 初始化会员数据
     *
     * @param dto
     * @return
     */
    public static void Init(User user) {

        Wechat wechat = wechatLogic.wechatService.selectById(user.getOpenid());

        if (wechat == null) {

            wechat = new Wechat();

            wechat.setCity(user.getCity());
            wechat.setCountry(user.getCountry());
            wechat.setCreateTime(DateUtils.getCurrentUnixTime());
            wechat.setHeadimgurl(user.getHeadimgurl());
            wechat.setJsjid(0);

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

}
