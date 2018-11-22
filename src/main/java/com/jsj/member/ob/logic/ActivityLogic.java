package com.jsj.member.ob.logic;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jsj.member.ob.dto.api.activity.ActivityDto;
import com.jsj.member.ob.dto.api.activity.ActivityProductDto;
import com.jsj.member.ob.entity.Activity;
import com.jsj.member.ob.entity.ActivityProduct;
import com.jsj.member.ob.enums.ActivityType;
import com.jsj.member.ob.exception.ActivityStockException;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.redis.ActivityKey;
import com.jsj.member.ob.redis.RedisService;
import com.jsj.member.ob.service.ActivityProductService;
import com.jsj.member.ob.service.ActivityService;
import com.jsj.member.ob.utils.DateUtils;
import com.jsj.member.ob.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class ActivityLogic extends BaseLogic {

    private static ActivityLogic activityLogic;

    @Autowired
    ActivityProductService activityProductService;

    @Autowired
    ActivityService activityService;

    @Autowired
    RedisService redisService;


    // 初始化的时候，将本类中的sysConfigManager赋值给静态的本类变量
    @PostConstruct
    public void init() {
        activityLogic = this;
    }

    /**
     * 获取商品正在参加的活动
     *
     * @param productId
     * @param productSpecId
     * @return
     */
    public static List<ActivityDto> GetProductCurrentActivityDtos(int productId, int productSpecId) {

        List<ActivityDto> activityDtos = new ArrayList<>();

        EntityWrapper<ActivityProduct> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("product_id={0} and product_spec_id = {1}", productId, productSpecId);
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
        dto.setShowTime(entity.getShowTime());

        dto.setCreateTime(entity.getCreateTime());
        dto.setDeleteTime(entity.getDeleteTime());
        dto.setIfpass(entity.getIfpass());
        dto.setOriginalPrice(entity.getOriginalPrice());
        dto.setSalePrice(entity.getSalePrice());

        dto.setTypeId(entity.getTypeId());
        dto.setStockCount(entity.getStockCount());
        dto.setUpdateTime(entity.getUpdateTime());
        dto.setNumber(entity.getNumber());

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
     * 获得当前预热展示的活动
     *
     * @return
     */
    public static List<ActivityDto> GetShowActivity() {

        int currentTime = DateUtils.getCurrentUnixTime();

        List<ActivityDto> activityDtos = new ArrayList<>();
        EntityWrapper<Activity> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("delete_time is null and ifpass = 1");
        entityWrapper.where("UNIX_TIMESTAMP() between show_time and begin_time");

        List<Activity> activities = activityLogic.activityService.selectList(entityWrapper);
        for (Activity activity : activities) {
            ActivityDto activityDto = ActivityLogic.GetActivity(activity.getActivityId());
            activityDtos.add(activityDto);
        }
        return activityDtos;
    }

    /**
     * 获取活动商品列表
     *
     * @param activityId
     * @return
     */
    public static List<ActivityProductDto> GetActivityProductDtos(int activityId) {

        EntityWrapper<ActivityProduct> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("delete_time is null");
        entityWrapper.where("activity_id={0}", activityId);

        List<ActivityProductDto> activityProductDtos = new ArrayList<>();
        List<ActivityProduct> activityProducts = activityLogic.activityProductService.selectList(entityWrapper);

        for (ActivityProduct ap : activityProducts) {

            ActivityProductDto dto = new ActivityProductDto();

            dto.setActivityId(ap.getActivityId());
            dto.setActivityProductId(ap.getActivityProductId());
            dto.setCreateTime(ap.getCreateTime());
            dto.setDeleteTime(ap.getDeleteTime());
            dto.setProductId(ap.getProductId());

            dto.setSalePrice(ap.getSalePrice());
            dto.setProductSpecId(ap.getProductSpecId());
            dto.setUpdateTime(ap.getUpdateTime());

            activityProductDtos.add(dto);
        }

        return activityProductDtos;

    }

    /**
     * 获得未审核的商品
     *
     * @return
     */
    public static Integer GetUnPassActivity() {
        EntityWrapper<Activity> activityWrapper = new EntityWrapper<>();

        activityWrapper.where("ifpass is false and delete_time is null");

        return activityLogic.activityService.selectCount(activityWrapper);
    }

    /**
     * 更新活动排序
     *
     * @param activityId
     * @param ifUp
     */
    public static void Sort(int activityId, Boolean ifUp) {

        Activity current = activityLogic.activityService.selectById(activityId);
        int typeId = current.getTypeId();

        EntityWrapper<Activity> wrapper = new EntityWrapper<>();
        wrapper.where("type_id={0} and delete_time is null", typeId);
        wrapper.orderBy("sort asc, update_time desc");
        List<Activity> activities = activityLogic.activityService.selectList(wrapper);

        //重置所有排序
        int sort = 0;
        for (Activity activity : activities) {
            activity.setSort(sort);
            activityLogic.activityService.updateById(activity);
            sort++;
        }

        if (ifUp != null) {
            //向上
            if (ifUp) {

                current = activities.stream().filter(p -> p.getActivityId() == activityId).findFirst().get();
                if (current.getSort() == 0) {
                    return;
                }
                int currentSort = current.getSort();
                Activity prev = activities.stream().filter(p -> p.getSort() == (currentSort - 1)).findFirst().get();
                prev.setSort(prev.getSort() + 1);

                current.setSort(current.getSort() - 1);

                activityLogic.activityService.updateById(current);
                activityLogic.activityService.updateById(prev);

            } else {
                //向下移动
                current = activities.stream().filter(p -> p.getActivityId() == activityId).findFirst().get();
                if (current.getSort() == activities.size() - 1) {
                    return;
                }
                int currentSort = current.getSort();
                Activity next = activities.stream().filter(p -> p.getSort() == (currentSort + 1)).findFirst().get();
                next.setSort(next.getSort() - 1);

                current.setSort(current.getSort() + 1);

                activityLogic.activityService.updateById(current);
                activityLogic.activityService.updateById(next);
            }
        }


    }


    public static void Sync2Redis() {

        Wrapper<Activity> wrapper = new EntityWrapper<>();
        wrapper.where("delete_time is null");
        wrapper.where("ifpass = 1");
        wrapper.where("UNIX_TIMESTAMP() between begin_time - 60 * 10 and begin_time - 60 *5");

        List<Activity> activities = activityLogic.activityService.selectList(wrapper);
        for (Activity at : activities) {
            ActivityLogic.Sync2Redis(at);
        }
    }

    public static void Sync2Redis(Activity activity) {

        Jedis jedis = activityLogic.jedisPool.getResource();
        String key = String.format("%d", activity.getActivityId());
        //没有库存
        if (activity.getStockCount() <= 0) {
            jedis.del(ActivityKey.SecKill.getPrefix() + key);
            return;
        }
        //已初始化
        if (jedis.exists(ActivityKey.SecKill.getPrefix() + key)) {
            return;
        }
        jedis.set(ActivityKey.SecKill.getPrefix() + key, activity.getStockCount() + "");

    }

    @Autowired
    JedisPool jedisPool;

    public static String SecKill(int activityId, String userId) {

        String uuid = StringUtils.UUID32();
        Jedis jedis = activityLogic.jedisPool.getResource();
        while (true) {
            boolean ok = RedisService.tryGetDistributedLock(jedis, "ttt", uuid, 5);
            if (!ok) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            } else {
                try {
                    String key = String.format("%d", activityId);
                    String userKey = String.format("%d_%s", activityId, userId);
                    //初始化判断
                    if (!jedis.exists(ActivityKey.SecKill.getPrefix() + key)) {
                        return "未初始化数据";
                    }
                    //判断购买
                    if (jedis.exists(ActivityKey.SecKill.getPrefix() + userKey)) {
                        return "重复购买";
                    }
                    //预减库存
                    long stock = jedis.decr(ActivityKey.SecKill.getPrefix() + key);
                    if (stock < 0) {
                        return "已被抢空";
                    }
                    jedis.set(ActivityKey.SecKill.getPrefix() + userKey, userKey);
                    //放入rabbitmq队列
                    return "抢购成功";

                } catch (Exception ex) {
                    throw ex;
                } finally {
                    System.out.println("end >>" + userId + ">>" + DateUtils.getCurrentUnixTime());
                    RedisService.releaseDistributedLock(jedis, "ttt", uuid);
                }
            }
        }
    }


}
