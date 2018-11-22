package com.jsj.member.ob.logic;

import com.jsj.member.ob.App;
import com.jsj.member.ob.dto.api.activity.ActivityDto;
import com.jsj.member.ob.entity.Activity;
import com.jsj.member.ob.enums.SecKillStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

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
        activity.setStockCount(3);

        ActivityLogic.RedisSync(activity);
    }

    @Test
    public void secKill() {

        Activity activity = new Activity();
        activity.setActivityId(1);
        activity.setStockCount(3);

        ActivityLogic.RedisSync(activity);
        List<String> userIds = new ArrayList<>();

        userIds.add("10001");
        userIds.add("10001");
        userIds.add("10001");
        userIds.add("10001");
        userIds.add("10001");
        userIds.add("10001");
        userIds.add("10005");
        userIds.add("10006");
        userIds.add("10001");
        userIds.add("10001");
        userIds.add("10001");
        userIds.add("10001");
        userIds.add("10001");
        userIds.add("10001");

        List<Future> futureList = new ArrayList<>();
        for (int i = 0; i < userIds.size(); i++) {
            String userId = userIds.get(i);

            Future future = CommonThreadPool.submit(() -> {
                SecKillStatus secKillStatus = ActivityLogic.RedisKill(1, userId);
                return secKillStatus.getMessage() + ">>" + userId;
            });
            futureList.add(future);
        }

        try {
            for (Future future : futureList) {
                System.out.println(future.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("主线程干完了");

    }


    @Test
    public void secKillOne() {

        //Activity activity = new Activity();
        //activity.setActivityId(1);
        //activity.setStockCount(3);
        //ActivityLogic.Sync2Redis(activity);

        SecKillStatus secKillStatus = ActivityLogic.RedisKill(1, "10001");
        System.out.println(secKillStatus.getMessage());

    }
}

class CommonThreadPool {
    private static ExecutorService exec = new ThreadPoolExecutor(50, 100, 0L,
            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(10000),
            new ThreadPoolExecutor.CallerRunsPolicy());

    public static void execute(Runnable command) {
        exec.execute(command);
    }

    // 子线程执行结束future.get()返回null，若没有执行完毕，主线程将会阻塞等待
    public static Future submit(Runnable command) {
        return exec.submit(command);
    }

    // 子线程中的返回值可以从返回的future中获取：future.get();
    public static Future submit(Callable command) {
        return exec.submit(command);
    }
}
