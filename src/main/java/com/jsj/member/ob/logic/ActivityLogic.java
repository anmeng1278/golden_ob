package com.jsj.member.ob.logic;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jsj.member.ob.dto.api.activity.ActivityDto;
import com.jsj.member.ob.dto.api.activity.ActivityProductDto;
import com.jsj.member.ob.entity.Activity;
import com.jsj.member.ob.entity.ActivityProduct;
import com.jsj.member.ob.enums.ActivityType;
import com.jsj.member.ob.enums.SecKillStatus;
import com.jsj.member.ob.enums.SingletonMap;
import com.jsj.member.ob.exception.ActivityStockException;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.rabbitmq.MQSender;
import com.jsj.member.ob.rabbitmq.seckill.SecKillConfig;
import com.jsj.member.ob.rabbitmq.seckill.SecKillDto;
import com.jsj.member.ob.redis.ProductKey;
import com.jsj.member.ob.redis.RedisService;
import com.jsj.member.ob.service.ActivityProductService;
import com.jsj.member.ob.service.ActivityService;
import com.jsj.member.ob.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
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

    @Autowired
    MQSender mqSender;


    // 初始化的时候，将本类中的sysConfigManager赋值给静态的本类变量
    @PostConstruct
    public void init() {

        mqSender.setNormalQueue(SecKillConfig.SECKILL_NORMAL_QUEUE);
        mqSender.setErrorQueue(SecKillConfig.SECKILL_ERROR_QUEUE);

        activityLogic = this;
    }

    //region (public) 获取商品正在参加的活动 GetProductCurrentActivityDtos

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
    //endregion

    //region (public) 获取活动信息 GetActivity

    /**
     * 获取活动信息
     *
     * @param activityId
     * @return
     */
    public static ActivityDto GetActivity(int activityId) {

        Activity entity = activityLogic.activityService.selectById(activityId);
        return ActivityLogic.ToDto(entity);

    }
    //endregion

    //region (public) 实体转换 ToDto

    /**
     * 实体转换
     *
     * @param entity
     * @return
     */
    public static ActivityDto ToDto(Activity entity) {

        if (entity == null) {
            return null;
        }

        ActivityDto dto = new ActivityDto();

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
        dto.setImgPath(entity.getImgPath());
        dto.setIntroduce(entity.getIntroduce());

        return dto;
    }
    //endregion

    //region (public) 削减活动库存 ReductionActivityStock

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
    //endregion

    //region (public) 消减活动商品库存 ReductionActivityProductStock

    /**
     * 消减活动商品库存
     *
     * @param activityId
     * @param productId
     * @param productSepcId
     * @param number
     */
    public static void ReductionActivityProductStock(int activityId, int productId, int productSepcId, int number) {

        //削减活动商品库存
        Wrapper<ActivityProduct> activityProductWrapper = new EntityWrapper<>();
        activityProductWrapper.where("activity_id = {0}", activityId);
        activityProductWrapper.where("product_id = {0}", productId);
        activityProductWrapper.where("product_spec_id = {0}", productSepcId);

        ActivityProduct activityProduct = activityLogic.activityProductService.selectOne(activityProductWrapper);
        if (activityProduct.getStockCount() < number) {
            throw new TipException(String.format("活动商品库存不足，暂不允许下单<br />活动编号：%d 商品编号：%d", activityId, productId));
        }
        activityProduct.setStockCount(activityProduct.getStockCount() - number);

        activityLogic.activityProductService.updateById(activityProduct);

    }
    //endregion


    //region (public) 获得当前预热展示的活动 GetShowActivity

    /**
     * 获得当前预热展示的活动
     *
     * @return
     */
    public static List<ActivityDto> GetShowActivity() {

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
    //endregion

    //region (public) 获取活动商品列表 GetActivityProductDtos

    /**
     * 获取活动商品列表
     *
     * @param activityId
     * @return
     */
    public static List<ActivityProductDto> GetActivityProductDtos(int activityId) {
        return GetActivityProductDtos(activityId, 0);
    }
    //endregion


    //region (public) 获取活动商品列表 GetActivityProductDtos

    /**
     * 获取活动商品列表
     *
     * @param activityId
     * @return
     */
    public static List<ActivityProductDto> GetActivityProductDtos(int activityId, int productSpecId) {

        EntityWrapper<ActivityProduct> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("delete_time is null");
        entityWrapper.where("activity_id={0}", activityId);

        if (productSpecId > 0) {
            entityWrapper.where("product_spec_id={0}", productSpecId);
        }

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
            dto.setStockCount(ap.getStockCount());

            dto.setProductDto(ProductLogic.GetProduct(ap.getProductId()));

            activityProductDtos.add(dto);
        }

        return activityProductDtos;

    }
    //endregion

    //region (public) 获得未审核的商品 GetUnPassActivity

    /**
     * 获得未审核的商品
     *
     * @return
     */
    public static Integer GetUnPassActivity() {
        EntityWrapper<Activity> activityWrapper = new EntityWrapper<>();

        activityWrapper.where("ifpass = 0 and delete_time is null");

        return activityLogic.activityService.selectCount(activityWrapper);
    }
    //endregion

    //region (public) 更新活动排序 Sort

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
    //endregion

    //region (public) 同步活动到redis数据库 RedisSync

    /**
     * 同步活动到redis数据库
     */
    public static List<Integer> RedisSync() {

        Wrapper<Activity> wrapper = new EntityWrapper<>();
        wrapper.where("delete_time is null");
        wrapper.where("ifpass = 1");
        wrapper.where("type_id = {0}", ActivityType.SECKILL.getValue());
        //wrapper.where("UNIX_TIMESTAMP() between begin_time - 60 * 10 and begin_time - 60 *5");
        wrapper.where("UNIX_TIMESTAMP() between begin_time - 60 * 10 and end_time");

        List<Activity> activities = activityLogic.activityService.selectList(wrapper);
        List<Integer> activityIds = new ArrayList<>();
        for (Activity at : activities) {
            if (ActivityLogic.RedisSync(at)) {
                activityIds.add(at.getActivityId());
            }
        }

        return activityIds;
    }
    //endregion

    //region (public) 同步活动到redis数据库 RedisSync

    /**
     * 同步活动到redis数据库
     *
     * @param activity
     */
    public static boolean RedisSync(Activity activity) {
        Jedis jedis = null;
        try {

            jedis = activityLogic.jedisPool.getResource();
            int activityId = activity.getActivityId();

            //活动商品
            List<ActivityProductDto> products = ActivityLogic.GetActivityProductDtos(activityId);
            if (products == null || products.isEmpty()) {
                return false;
            }

            for (ActivityProductDto dto : products) {

                ProductKey productKey = new ProductKey(0, String.format("%d_%d_%d", activityId, dto.getProductId(), dto.getProductSpecId()));
                String key = productKey.INIT();
                String readyKey = productKey.READYTIME();

                //没有库存
                if (dto.getStockCount() == 0) {
                    jedis.del(key);
                    continue;
                }

                //已初始化
                if (jedis.exists(key)) {
                    continue;
                }

                jedis.set(key, dto.getStockCount() + "");
                jedis.set(readyKey, activity.getBeginTime() + "");
            }

        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

        return true;
    }
    //endregion

    //region (public) 校验活动是否允许修改 checkActivity

    /**
     * 校验活动是否允许修改
     *
     * @param activityId
     */
    public static boolean checkActivity(int activityId) {

        //活动商品
        List<ActivityProductDto> products = ActivityLogic.GetActivityProductDtos(activityId);
        if (products == null || products.isEmpty()) {
            return true;
        }
        Jedis jedis = null;
        try {

            jedis = activityLogic.jedisPool.getResource();
            for (ActivityProductDto dto : products) {

                ProductKey productKey = new ProductKey(0, String.format("%d_%d_%d", activityId, dto.getProductId(), dto.getProductSpecId()));

                //已初始化
                if (jedis.exists(productKey.RESULT())) {
                    return false;
                }

            }
            for (ActivityProductDto dto : products) {
                ProductKey productKey = new ProductKey(0, String.format("%d_%d_%d", activityId, dto.getProductId(), dto.getProductSpecId()));
                for (String k : productKey.TOTALS()) {
                    jedis.del(k);
                }
            }
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

        return true;
    }
    //endregion

    //region (public) 根据活动类型获取首个活动 GetActivity

    /**
     * 根据活动类型获取首个活动
     *
     * @param activityType
     * @return
     */
    public static ActivityDto GetActivity(ActivityType activityType) {

        Wrapper<Activity> wrapper = new EntityWrapper<>();
        wrapper.where("ifpass = 1 and delete_time is null");
        wrapper.where("type_id = {0}", activityType.getValue());
        wrapper.orderBy("sort asc, update_time desc");

        Activity entity = activityLogic.activityService.selectOne(wrapper);
        ActivityDto dto = ActivityLogic.ToDto(entity);

        return dto;
    }
    //endregion

    //region (public) 根据活动类型活动活动信息 GetActivityByType

    /**
     * 根据活动类型活动活动信息
     *
     * @param activityType
     * @return
     */
    public static List<ActivityDto> GetActivityByType(ActivityType activityType) {

        Wrapper<Activity> wrapper = new EntityWrapper<>();
        wrapper.where("ifpass = 1 and delete_time is null");
        wrapper.where("type_id = {0}", activityType.getValue());
        wrapper.where("UNIX_TIMESTAMP() between begin_time and end_time");
        wrapper.orderBy("sort asc, update_time desc");

        List<Activity> activities = activityLogic.activityService.selectList(wrapper);

        List<ActivityDto> dtos = new ArrayList<>();

        for (Activity activity : activities) {
            ActivityDto dto = new ActivityDto();
            dto.setShowTime(activity.getShowTime());
            dto.setNumber(activity.getNumber());
            dto.setActivityId(activity.getActivityId());
            dto.setActivityName(activity.getActivityName());
            dto.setBeginTime(activity.getBeginTime());
            dto.setActivityType(ActivityType.valueOf(activity.getTypeId()));
            dto.setEndTime(activity.getEndTime());
            dto.setIfpass(activity.getIfpass());
            dto.setImgPath(activity.getImgPath());
            dto.setCreateTime(activity.getCreateTime());
            dto.setStockCount(activity.getStockCount());
            dto.setUpdateTime(activity.getUpdateTime());
            dto.setOriginalPrice(activity.getOriginalPrice());
            dto.setSalePrice(activity.getSalePrice());
            dto.setIntroduce(activity.getIntroduce());
            dto.setTypeId(activity.getTypeId());
            dtos.add(dto);
        }

        return dtos;
    }
    //endregion

    //region (public) 秒杀 RedisKill


    @Autowired
    JedisPool jedisPool;


    public static HashMap<String, Boolean> soldOutMap = new HashMap<String, Boolean>();

    /**
     * 秒杀
     *
     * @param activityId
     * @param productId
     * @param productSpecId
     * @param openId
     * @return
     */
    public static SecKillStatus RedisKill(int activityId, int productId, int productSpecId, String openId) {

        ProductKey productKey = new ProductKey(0, String.format("%d_%d_%d", activityId, productId, productSpecId));
        Jedis jedis = null;

        String key = productKey.INIT();
        String userKey = String.format("%s:%s", productKey.getPrefix(), openId);
        String resultKey = productKey.RESULT();
        String readyKey = productKey.READYTIME();

        SingletonMap singletonMap = SingletonMap.INSTANCE;

        try {

            jedis = activityLogic.jedisPool.getResource();

            if (soldOutMap.containsKey(productKey.getPrefix())) {
                if (soldOutMap.get(productKey.getPrefix())) {
                    return SecKillStatus.SOLDOUT;
                }
            }

            //初始化判断
            if (!jedis.exists(key)) {
                return SecKillStatus.UNINIT;
            }

            //重复请求判断
            boolean put = singletonMap.Put(openId, openId);
            if (!put) {
                return SecKillStatus.REPEATREQUEST;
            }

            long readyTime = Long.parseLong(jedis.get(readyKey));
            if (readyTime > DateUtils.getCurrentUnixTime()) {
                return SecKillStatus.UNBEGIN;
            }


            if (soldOutMap.containsKey(productKey.getPrefix())) {
                if (soldOutMap.get(productKey.getPrefix())) {
                    return SecKillStatus.SOLDOUT;
                }
            }

            //判断购买
            if (jedis.sadd(resultKey, userKey).intValue() == 0) {
                return SecKillStatus.REPEAT;
            }
            //预减库存
            long stock = jedis.decr(key);
            if (stock < 0) {
                jedis.srem(resultKey, userKey);
                soldOutMap.put(productKey.getPrefix(), true);
                return SecKillStatus.SOLDOUT;
            }

            //放入rabbitmq队列
            SecKillDto dto = new SecKillDto();
            dto.setActivityId(activityId);
            dto.setOpenId(openId);
            dto.setProductId(productId);
            dto.setProductSpecId(productSpecId);

            activityLogic.mqSender.sendNormal(dto);

            return SecKillStatus.SUCCESS;

        } catch (Exception ex) {
            ex.printStackTrace();
            return SecKillStatus.SYSTEMERROR;
        } finally {
            singletonMap.Remove(openId);
            if (jedis != null) {
                jedis.close();
            }
            System.gc();
        }
    }

//endregion

}

