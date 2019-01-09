package com.jsj.member.ob.service.impl;

import com.jsj.member.ob.dto.api.gift.UserDrawDto;
import com.jsj.member.ob.dto.api.gift.UserGiftDto;
import com.jsj.member.ob.entity.Gift;
import com.jsj.member.ob.dao.GiftMapper;
import com.jsj.member.ob.service.GiftService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cc
 * @description : Gift 服务实现类
 * ---------------------------------
 * @since 2018-12-21
 */
@Service
public class GiftServiceImpl extends ServiceImpl<GiftMapper, Gift> implements GiftService {

    /**
     * 获取用户领取记录
     *
     * @param openId
     * @return
     */
    @Override
    public List<UserDrawDto> getUserDraws(String openId) {
        return baseMapper.getUserDraws(openId);
    }

    /**
     * 获取用户赠送记录
     *
     * @param openId
     * @return
     */
    @Override
    public List<UserGiftDto> getUserGifts(String openId) {
        return baseMapper.getUserGifts(openId);
    }
}
