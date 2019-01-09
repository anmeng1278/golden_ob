package com.jsj.member.ob.service;

import com.baomidou.mybatisplus.service.IService;
import com.jsj.member.ob.dto.api.gift.UserDrawDto;
import com.jsj.member.ob.dto.api.gift.UserGiftDto;
import com.jsj.member.ob.entity.Gift;

import java.util.List;

/**
 * @author cc
 * @description : Gift 服务接口
 * ---------------------------------
 * @since 2018-12-21
 */
public interface GiftService extends IService<Gift> {


    /**
     * 获取用户领取记录
     *
     * @param openId
     * @return
     */
    List<UserDrawDto> getUserDraws(String openId);

    /**
     * 获取用户赠送记录
     *
     * @param openId
     * @return
     */
    List<UserGiftDto> getUserGifts(String openId);
}
