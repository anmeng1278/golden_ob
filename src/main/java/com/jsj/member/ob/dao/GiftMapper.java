package com.jsj.member.ob.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jsj.member.ob.dto.api.gift.UserDrawDto;
import com.jsj.member.ob.dto.api.gift.UserGiftDto;
import com.jsj.member.ob.entity.Gift;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author cc
 * @description : Gift Mapper 接口
 * ---------------------------------
 * @since 2018-12-21
 */
@Repository
public interface GiftMapper extends BaseMapper<Gift> {


    /**
     * 获取用户领取记录
     * @param unionId
     * @return
     */
    List<UserDrawDto> getUserDraws(String unionId);


    /**
     * 获取用户赠送记录
     * @param openId
     * @return
     */
    List<UserGiftDto> getUserGifts(String openId);
}