package com.jsj.member.ob.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jsj.member.ob.entity.ActivityProduct;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *   @description : ActivityProduct Mapper 接口
 *   ---------------------------------
 * 	 @author cc
 *   @since 2019-02-11
 */
@Repository
public interface ActivityProductMapper extends BaseMapper<ActivityProduct> {

    /**
     * 获取活动商品信息
     * @param activityId
     * @param productSpecId
     * @return
     */
    List<ActivityProduct> getActivityProducts(@Param("activityId")int activityId, @Param("productSpecId")int productSpecId);

}