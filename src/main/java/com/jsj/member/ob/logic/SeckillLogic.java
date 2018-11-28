package com.jsj.member.ob.logic;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jsj.member.ob.dto.api.activity.ActivityProductDto;
import com.jsj.member.ob.entity.Activity;
import com.jsj.member.ob.enums.ActivityType;
import com.jsj.member.ob.enums.SecKillStatus;
import com.jsj.member.ob.rabbitmq.MQSender;
import com.jsj.member.ob.rabbitmq.seckill.SecKillConfig;
import com.jsj.member.ob.rabbitmq.seckill.SecKillDto;
import com.jsj.member.ob.redis.ActivityKey;
import com.jsj.member.ob.redis.RedisService;
import com.jsj.member.ob.service.ActivityProductService;
import com.jsj.member.ob.service.ActivityService;
import com.rabbitmq.client.Channel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Component
public class SeckillLogic extends BaseLogic{

    @Autowired
    private static SeckillLogic seckillLogic;

    @PostConstruct
    public void init() {
        seckillLogic = this;
    }


    @Autowired
    JedisPool jedisPool;

    @Autowired
    ActivityService activityService;

    @Autowired
    RedisService redisService;

    @Autowired
    MQSender mqSender;

    @Autowired
    ActivityProductService activityProductService;


    private static HashMap<Integer, Boolean> stockMap = new HashMap<Integer, Boolean>();

    /**
     * 把秒杀活动中的商品放入缓存中
     */
    public static void SetActivityProduct(){

        Jedis jedis = seckillLogic.jedisPool.getResource();

        Wrapper<Activity> wrapper = new EntityWrapper<>();
        wrapper.where("delete_time is null");
        wrapper.where("ifpass = 1 and type_id={0}", ActivityType.SECKILL.getValue());
        wrapper.where("UNIX_TIMESTAMP() between begin_time - 60 * 10 and begin_time - 60 *5");

        List<Activity> activities = seckillLogic.activityService.selectList(wrapper);
        activities.stream().forEach(s->{

            List<ActivityProductDto> activityProductDtos = ActivityLogic.GetActivityProductDtos(s.getActivityId());

            for (ActivityProductDto activityProductDto : activityProductDtos) {
                ActivityKey activityKey = new ActivityKey(0, activityProductDto.getActivityId()+"");

                String key = String.format("%s:%s", activityKey.getPrefix(),activityProductDto.getProductId(), "INIT");

                //若库存小于0，删除库存
                if(activityProductDto.getStockCount() <= 0){
                    jedis.del(activityKey.toString());
                }
                jedis.set(key,activityProductDto.getStockCount()+"");
                stockMap.put(activityProductDto.getProductId(),false);
            }
        });

    }

    /**
     * 秒杀
     * @param activityId
     * @param productId
     * @param openId
     * @return
     */
    public static SecKillStatus Seckill(int activityId,int productId,String openId){

        Jedis jedis = seckillLogic.jedisPool.getResource();

        ActivityKey activityKey = new ActivityKey(0, activityId + "");

        String key = String.format("%s:%s:%s", activityKey.getPrefix(),productId, "INIT");
        String userKey = String.format("%s:%s", activityKey.getPrefix(), openId);

        //判断库存
        Boolean aBoolean = stockMap.get(productId);
        if(aBoolean){
            return SecKillStatus.SOLDOUT;
        }

        //判断是否重复秒杀
        if(jedis.exists(userKey)){
            return SecKillStatus.REPEAT;
        }

        //加锁
        while (true){
            boolean lock = RedisService.tryGetDistributedLock(jedis, "lock", UUID.randomUUID().toString(), 500);
            if(lock){
                //预减库存
                long stock = jedis.decr(key);
                if (stock < 0) {
                    stockMap.put(productId, true);
                    return SecKillStatus.SOLDOUT;
                }

                //请求入队
                SecKillDto secKillDto = new SecKillDto();
                secKillDto.setActivityId(Integer.valueOf(key));
                secKillDto.setOpenId(userKey);
                seckillLogic.mqSender.sendNormal(secKillDto);
                return SecKillStatus.SUCCESS;
            }else {
                //未拿到锁：
                //线程sleep再次尝试拿锁 .
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    //释放锁
                    RedisService.releaseDistributedLock(jedis,"lock",UUID.randomUUID().toString());
                }
            }
        }

    }


    @RabbitListener(queues = SecKillConfig.SECKILL_NORMAL_QUEUE)
    public static SecKillStatus CreateOrder(SecKillDto dto, Channel channel, Message message){

        return SecKillStatus.SUCCESS;
    }

}
