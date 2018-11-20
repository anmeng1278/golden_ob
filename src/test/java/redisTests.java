import com.jsj.member.ob.App;
import com.jsj.member.ob.redis.AccessKey;
import com.jsj.member.ob.redis.RedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
public class redisTests {

    @Autowired
    RedisService redisService;

    @Test
    public void Test1() throws InterruptedException {

        //System.out.println(redisService);
        redisService.set(AccessKey.withExpire(100), "123", 5);
        //boolean delete = redisService.delete(GoodsKey.getMiaoshaGoodsStock, "123");
        //System.out.println(delete);

        Integer integer = redisService.get(AccessKey.withExpire(100), "123", Integer.class);
        System.out.println(integer);

        //for (int i = 0; i < 5; i++) {
        //    //预减库存
        //    long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock, "123");//10
        //    System.out.println(stock);
        //}

        List<Future> futureList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            int finalI = i;
            Future future = CommonThreadPool.submit(() -> {
                for (int index = 0; index < 5; index++) {
                    //预减库存
                    long stock = redisService.decr(AccessKey.withExpire(100), "123");//10
                    if (stock < 0) {
                        System.out.println("抢没了" + finalI + "_" + index + "_" + stock);
                    } else {
                        System.out.println("抢购成功" + finalI + "_" + index + "_" + stock);
                    }
                }
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

