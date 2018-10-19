package com.jsj.member.ob.logic;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jsj.member.ob.dto.api.activity.ActivityDto;
import com.jsj.member.ob.dto.api.activity.ActivityProductDto;
import com.jsj.member.ob.entity.Activity;
import com.jsj.member.ob.entity.ActivityProduct;
import com.jsj.member.ob.enums.ActivityType;
import com.jsj.member.ob.exception.ActivityStockException;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.service.ActivityProductService;
import com.jsj.member.ob.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class ActivityLogic {

    public static ActivityLogic activityLogic;

    @Autowired
    ActivityProductService activityProductService;

    @Autowired
    ActivityService activityService;

    // 初始化的时候，将本类中的sysConfigManager赋值给静态的本类变量
    @PostConstruct
    public void init() {
        activityLogic = this;
        activityLogic.activityProductService = this.activityProductService;
        activityLogic.activityService = this.activityService;
    }

    /**
     * 获取商品正在参加的活动
     *
     * @param productId
     * @return
     */
    public static List<ActivityDto> GetProductCurrentActivityDtos(int productId) {

        List<ActivityDto> activityDtos = new ArrayList<>();

        EntityWrapper<ActivityProduct> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("delete_time is null");
        entityWrapper.where("exists( select * from _activity where _activity.activity_id = _activity_product.activity_id and  _activity.delete_time is null and _activity.ifpass = 1 and ( UNIX_TIMESTAMP() between _activity.begin_time and _activity.end_time )  )");

        List<ActivityProduct> activityProducts = activityLogic.activityProductService.selectList(entityWrapper);

        for (ActivityProduct ap : activityProducts) {
            ActivityDto dto = ActivityLogic.GetActivity(ap.getActivityId());
            activityDtos.add(dto);
        }

        return activityDtos;

    }

    /**
     * 获取活动信息
     *
     * @param activityId
     * @return
     */
    public static ActivityDto GetActivity(int activityId) {

        ActivityDto dto = new ActivityDto();

        Activity entity = activityLogic.activityService.selectById(activityId);

        dto.setActivityId(entity.getActivityId());
        dto.setActivityName(entity.getActivityName());
        dto.setActivityType(ActivityType.valueOf(entity.getTypeId()));
        dto.setBeginTime(entity.getBeginTime());
        dto.setEndTime(entity.getEndTime());

        dto.setCreateTime(entity.getCreateTime());
        dto.setDeleteTime(entity.getDeleteTime());
        dto.setIfpass(entity.getIfpass());
        dto.setOriginalPrice(entity.getOriginalPrice());
        dto.setSalePrice(entity.getSalePrice());

        dto.setStockCount(entity.getStockCount());
        dto.setUpdateTime(entity.getUpdateTime());

        return dto;

    }

    /**
     * 削减活动库存
     *
     * @param activityId
     * @param number
     */
    public static void ReductionActivityStock(int activityId, int number, Integer activityOrderId) {

        Activity activity = activityLogic.activityService.selectById(activityId);

        if (activity.getStockCount() < number) {
            switch ((ActivityType.valueOf(activity.getTypeId()))) {
                case GROUPON:
                    ActivityStockException ae = new ActivityStockException();
                    ae.setActivityId(activityId);
                    ae.setActivityType(ActivityType.valueOf(activity.getTypeId()));
                    ae.setNumber(number);
                    ae.setStock(activity.getStockCount());
                    ae.setActivytyOrderId(activityOrderId);
                    throw ae;
                default:
                    throw new TipException(String.format("活动商品售罄啦，活动编号：%d", activity.getActivityId()));
            }
        }

        activity.setStockCount(activity.getStockCount() - number);
        activityLogic.activityService.updateById(activity);
    }

    /**
     * 获取活动商品列表
     * @param activityId
     * @return
     */
    public static List<ActivityProductDto> GetActivityProductDtos(int activityId) {

        EntityWrapper<ActivityProduct> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("delete_time is null");

        List<ActivityProductDto> activityProductDtos = new ArrayList<>();
        List<ActivityProduct> activityProducts = activityLogic.activityProductService.selectList(entityWrapper);

        for (ActivityProduct ap : activityProducts) {

            ActivityProductDto dto = new ActivityProductDto();

            dto.setActivityId(ap.getActivityId());
            dto.setActivityProductId(ap.getActivityProductId());
            dto.setCreateTime(ap.getCreateTime());
            dto.setDeleteTime(ap.getDeleteTime());
            dto.setProductId(ap.getProductId());

            dto.setProductSpecId(ap.getProductSpecId());
            dto.setUpdateTime(ap.getUpdateTime());

            activityProductDtos.add(dto);
        }

        return activityProductDtos;

    }

}
