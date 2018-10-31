package com.jsj.member.ob.logic;

import com.jsj.member.ob.dto.api.wechat.WechatDto;
import com.jsj.member.ob.entity.Wechat;
import com.jsj.member.ob.service.WechatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class WechatLogic {

    public static WechatLogic wechatLogic;

    @Autowired
    WechatService wechatService;

    @PostConstruct
    public void init() {
        wechatLogic = this;
        wechatLogic.wechatService = this.wechatService;
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
}
