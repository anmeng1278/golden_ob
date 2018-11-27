import com.jsj.member.ob.enums.SingletonMap;
import com.jsj.member.ob.redis.AccessKey;
import com.jsj.member.ob.redis.RedisService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = App.class)
//@WebAppConfiguration
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


    @Test
    public void Test12() throws InterruptedException {

        SingletonMap instance = SingletonMap.INSTANCE;
        System.out.println(instance);

        try {

            List<Future> futureList = new ArrayList<>();
            for (int i = 0; i < 1000; i++) {

                int finalI = i;
                Future future = CommonThreadPool.submit(() -> {

                    Thread.sleep(10000);
                    //for (int index = 0; index < 5; index++) {
                    boolean result = instance.Put("1", "1");
                    //}
                    if(result) {
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
                Future future = CommonThreadPool.submit(() -> {

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
    public void testDecr() {

        Jedis jedis = jedisPool.getResource();

        jedis.set("abc", "10");
        jedis.set("money", "100");


        Transaction transaction = jedis.multi();
        Response<Long> abc = transaction.decr("abc");

        transaction.exec();


        System.out.println(abc.get());
        transaction.discard();

        //transaction.discard();

        //jedis.watch("abc");
        //
        //Long abc = jedis.decr("abc");
        //System.out.println("abc >>" + abc);
        //
        //Transaction transaction = jedis.multi();
        //
        ////
        ////Response<Long> abc1 = transaction.decr("abc");
        ////Response<Long> money = transaction.decrBy("money", 100);
        ////
        ////System.out.println("abc1 >>" + abc1);
        ////System.out.println("money >>" + money);
        //
        //transaction.exec();
        //List<Object> exec = transaction.exec();
        //System.out.println(exec);
        //
        //System.out.println(exec.size());

    }


    @Test
    public void testHGetSet() {

        Jedis jedis = jedisPool.getResource();

        String hget = jedis.hget("aaa", "bbb");
        System.out.println(hget);

        Long hset = jedis.hset("aaa", "bbb", "ccc");
        System.out.println(hset);

        hget = jedis.hget("aaa", "bbb");
        System.out.println(hget);

        hset = jedis.hset("aaa", "bbb", "ddd");
        System.out.println(hset);

        hget = jedis.hget("aaa", "bbb");
        System.out.println(hget);


    }


    @Test
    public void testHGetSetX() {

        Jedis jedis = jedisPool.getResource();

        String hget = jedis.hget("aaa1", "bbb");
        System.out.println(hget);

        Long hset = jedis.hsetnx("aaa1", "bbb", "ccc");
        System.out.println(hset);

        hget = jedis.hget("aaa1", "bbb");
        System.out.println(hget);

        hset = jedis.hsetnx("aaa", "bbb", "ddd");
        System.out.println(hset);

        hget = jedis.hget("aaa", "bbb");
        System.out.println(hget);
    }


    @Test
    public void testHMGetSet() {


        Jedis jedis = jedisPool.getResource();
        Long sadd = jedis.sadd("abc123", "abc123");
        System.out.println(sadd);

        sadd = jedis.sadd("abc123", "abc123");
        System.out.println(sadd);

        sadd = jedis.sadd("abc123", "abc124");
        System.out.println(sadd);

        jedis.srem("abc123", "abc123");

        sadd = jedis.sadd("abc123", "abc123");
        System.out.println(sadd);

        sadd = jedis.sadd("abc123", "abc123");
        System.out.println(sadd);

    }

    //public static void main(String[] args) {
    //
    //    Jedis jedis = new Jedis("127.0.0.1");// Redis的ip端口
    ///*for (int i = 0; i < 15; i++) {
    //	saveData(jedis, "shareniu", ""+i);
    //}*/
    //    List data = getData(jedis, key);
    //    System.err.println(data);
    //}
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

