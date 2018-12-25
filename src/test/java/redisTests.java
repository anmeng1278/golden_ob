import com.jsj.member.ob.App;
import com.jsj.member.ob.dto.api.stock.StockDto;
import com.jsj.member.ob.enums.SingletonMap;
import com.jsj.member.ob.redis.AccessKey;
import com.jsj.member.ob.redis.RedisService;
import com.jsj.member.ob.redis.StockKey;
import com.jsj.member.ob.utils.ThreadPoolUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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
            Future future = ThreadPoolUtils.submit(() -> {
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


    @Test
    public void Test12() throws InterruptedException {

        SingletonMap instance = SingletonMap.INSTANCE;
        System.out.println(instance);

        try {

            List<Future> futureList = new ArrayList<>();
            for (int i = 0; i < 1000; i++) {

                int finalI = i;
                Future future = ThreadPoolUtils.submit(() -> {

                    Thread.sleep(10000);
                    //for (int index = 0; index < 5; index++) {
                    boolean result = instance.Put("1", "1");
                    //}
                    if (result) {
                        return result + "/" + finalI;
                    }
                    return "";
                });
                futureList.add(future);
            }

            for (Future future : futureList) {
                System.out.println(future.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("主线程干完了");

    }


    @Test
    public void Test13() throws InterruptedException {


        CopyOnWriteArraySet<String> copyOnWriteArraySet = new CopyOnWriteArraySet<String>();


        try {

            List<Future> futureList = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                int finalI = i;
                Future future = ThreadPoolUtils.submit(() -> {

                    Thread.sleep(100);
                    //for (int index = 0; index < 5; index++) {
                    if (copyOnWriteArraySet.contains("1")) {
                        return false;
                    }
                    copyOnWriteArraySet.add("1");
                    return true;

                });
                futureList.add(future);
            }

            for (Future future : futureList) {
                System.out.println(future.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println(copyOnWriteArraySet);
        System.out.println("主线程干完了");

    }


    /**
     * 保存数据
     *
     * @param jedis
     * @param key
     * @param value
     */
    public static void saveData(Jedis jedis, String key, String value) {
        Transaction transaction = jedis.multi();
        // 向List头部追加记录
        transaction.lpush(key, value);
        // 仅保留指定区间内的记录数，删除区间外的记录。
        transaction.ltrim(key, 0, maxCount);

        transaction.exec();
    }

    /**
     * 获取数据
     *
     * @param jedis
     * @param key
     * @return
     */
    public static List getData(Jedis jedis, String key) {
        List list = jedis.lrange(key, 0, -1);// end 为 -1
        // 表示到末尾。因为前面插入操作时，限定了存在的记录数
        if (list == null || list.size() == 0) {
            list = new ArrayList();
        }
        return list;
    }

    public static int maxCount = 9;
    public static String key = "shareniu";


    @Autowired
    JedisPool jedisPool;

    @Test
    public void testSaveData() {

        Jedis jedis = jedisPool.getResource();

        String k = "hasBoughtSetKey";

        Boolean sismember = jedis.sismember(k, "123");
        System.out.println(sismember);

        Long sadd = jedis.sadd(k, "123");
        System.out.println(sadd);
    }


    @Test
    public void testKey() {

        List<StockDto> stockDtos = new ArrayList<>();
        StockDto st = new StockDto();
        st.setNumber(1);
        stockDtos.add(st);
        redisService.set(StockKey.token, "UseStock", stockDtos);
        //
        //redisService.get(StockKey.token, "UseStock", List<StockDto>.getClass());
        //redisService.delete(StockKey.token, "UseStock");

        //redisService.get()


        String useStock = redisService.get(StockKey.token, "UseStock", String.class);
        System.out.println(useStock);
    }

}



