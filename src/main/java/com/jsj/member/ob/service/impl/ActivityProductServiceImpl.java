package com.jsj.member.ob.service.impl;

import com.jsj.member.ob.entity.ActivityProduct;
import com.jsj.member.ob.dao.ActivityProductMapper;
import com.jsj.member.ob.service.ActivityProductService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cc
 * @description : ActivityProduct 服务实现类
 * ---------------------------------
 * @since 2019-02-11
 */
@Service
public class ActivityProductServiceImpl extends ServiceImpl<ActivityProductMapper, ActivityProduct> implements ActivityProductService {

    @Override
    public List<ActivityProduct> getActivityProducts(int activityId, int productSpecId) {
        return baseMapper.getActivityProducts(activityId, productSpecId);
    }
}
