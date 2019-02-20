package com.jsj.member.ob.service;

import com.baomidou.mybatisplus.service.IService;
import com.jsj.member.ob.entity.ActivityProduct;

import java.util.List;

/**
 *   @description : ActivityProduct 服务接口
 *   ---------------------------------
 * 	 @author cc
 *   @since 2019-02-11
 */
public interface ActivityProductService extends IService<ActivityProduct> {

    /**
     * 获取活动商品信息
     * @param activityId
     * @param productSpecId
     * @return
     */
    List<ActivityProduct> getActivityProducts(int activityId, int productSpecId);


}
