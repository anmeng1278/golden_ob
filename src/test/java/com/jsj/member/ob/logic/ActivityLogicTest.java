package com.jsj.member.ob.logic;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jsj.member.ob.App;
import com.jsj.member.ob.dto.api.activity.ActivityDto;
import com.jsj.member.ob.entity.Activity;
import com.jsj.member.ob.enums.SecKillStatus;
import com.jsj.member.ob.enums.SourceType;
import com.jsj.member.ob.service.ActivityService;
import com.jsj.member.ob.utils.DateUtils;
import com.jsj.member.ob.utils.ThreadPoolUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
public class ActivityLogicTest {

    @Test
    public void getShowActivity() {
        List<ActivityDto> activityDtos = ActivityLogic.GetShowActivity();
        for (ActivityDto activityDto : activityDtos) {
            System.out.println(activityDto);
        }
    }

    @Test
    public void sync2Redis() {

        Activity activity = new Activity();
        activity.setActivityId(1);
        activity.setBeginTime(DateUtils.getCurrentUnixTime());

        ActivityLogic.RedisSync(activity);
    }

    @Test
    public void sync2Redis2() {

        ActivityLogic.RedisSync();
    }

    /**
     * 同步数据
     */
    @Test
    public void RedisSync() {

        Activity activity = new Activity();
        activity.setActivityId(1);

        ActivityLogic.RedisSync(activity);
    }

    @Test
    public void secKill() {

        List<String> userIds = new ArrayList<>();

        List<String> userIds1 = new ArrayList<>();
        List<String> userIds2 = new ArrayList<>();

        for (int i = 10001; i < 10501; i++) {
            for (int j = 0; j < 3; j++) {
                userIds.add(i + "");
            }
        }

        List<Future> futureList = new ArrayList<>();
        for (int i = 0; i < userIds.size(); i++) {
            String userId = userIds.get(i);

            Future future = ThreadPoolUtils.submit(() -> {
                SecKillStatus secKillStatus = ActivityLogic.RedisKill(1, 3, 4, userId, SourceType.AWKTC);

                if (secKillStatus == SecKillStatus.REPEAT) {
                    userIds1.add(userId);
                }
                if (secKillStatus == SecKillStatus.SUCCESS) {
                    userIds2.add(userId);
                }

                return secKillStatus.getMessage() + ">>" + userId;
            });
            futureList.add(future);
        }

        try {
            for (Future future : futureList) {
                System.out.println(new Date() + " " + future.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        //
        //System.out.println(JSON.toJSONString(userIds1));
        //System.out.println(JSON.toJSONString(userIds2));

        System.out.println("主线程干完了");

    }

    public List removeDuplicate(List list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).equals(list.get(i))) {
                    list.remove(j);
                }
            }
        }
        return list;
    }

    @Autowired
    JedisPool jedisPool;

    @Test
    public void TestJedis() {
        Jedis jedis = jedisPool.getResource();

        for (int i = 1; i <= 1000; i++) {
            Long decr = jedis.decr("Prod:1_3_4:INIT");

            if (decr < 0) {
                break;
            }

            System.out.println("" + i);
        }

    }

    @Test
    public void secKillOne() {

        //Activity activity = new Activity();
        //activity.setActivityId(1);
        //activity.setStockCount(3);
        //ActivityLogic.Sync2Redis(activity);

        SecKillStatus secKillStatus = ActivityLogic.RedisKill(1, 3, 4, "10001", SourceType.AWKTC);
        System.out.println(secKillStatus.getMessage());

    }

    @Autowired
    ActivityService activityService;

    @Test
    public void selCount(){

        int i = activityService.selectCount(new EntityWrapper<Activity>().where("activity_id = -1"));
        System.out.println(i);
    }
}

