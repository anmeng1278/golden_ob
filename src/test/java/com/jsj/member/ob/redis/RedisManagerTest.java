package com.jsj.member.ob.redis;

import com.jsj.member.ob.App;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
public class RedisManagerTest {

    @Autowired
    RedisManager redisManager;

    @Test
    public void opsForValue() {

        ValueOperations valueOperations = redisManager.opsForValue();
        valueOperations.set("str", "str");
        valueOperations.set("str1", "str1", 10, TimeUnit.SECONDS);

    }


    @Test
    public void opsForHash() {

        HashOperations hashOperations = redisManager.opsForHash();

        hashOperations.put("hash01", "key01", "value01");
        hashOperations.put("hash01", "key01", "value02");
        hashOperations.put("hash01", "key02", "value01");
        hashOperations.put("hash01", "key03", "value01");
        hashOperations.put("hash01", "key04", "value01");

        Object o1 = hashOperations.get("hash01", "key04");
        System.out.println(o1);

        Object o = hashOperations.get("hash01", "key01");
        System.out.println(o.toString());

        Map hash01 = hashOperations.entries("hash01");
        Map hash02 = hashOperations.entries("hash02");

        System.out.println(hash01.isEmpty());
        System.out.println(hash02.isEmpty());
    }

    @Test
    public void opsForList() {

        List<String> list = new ArrayList<>();

        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        list.add("e");

        ListOperations listOperations = redisManager.opsForList();

        Long goods2 = listOperations.size("goods");
        Long goods3 = listOperations.size("goods3");

        System.out.println("Goods2 >> " + goods2);
        System.out.println("Goods3 >> " + goods3);

        int i = listOperations.rightPushAll("goods", list).intValue();
        System.out.println(i);

        Object value = listOperations.index("goods", 1);
        System.out.println(value);

        int i1 = listOperations.remove("goods", 0, "b").intValue();
        System.out.println(i1);

        List<String> goods = listOperations.range("goods", 0, -1);
        System.out.println(goods);

        for (int index = 0; index <= 10; index++) {
            Object goods1 = listOperations.leftPop("goods");
            System.out.println("goods >>" + goods1);
        }


    }

    @Test
    public void opsForZSet() {

        ZSetOperations zSetOperations = redisManager.opsForZSet();

        System.out.println(zSetOperations.add("zSetValue", "A", 1));
        System.out.println(zSetOperations.add("zSetValue", "B", 3));
        System.out.println(zSetOperations.add("zSetValue", "C", 2));
        System.out.println(zSetOperations.add("zSetValue", "D", 5));


        Set zSetValue1 = zSetOperations.rangeByScore("zSetValue", 2, 3);
        System.out.println("zSetValue1" + zSetValue1);


        Set zSetValue = zSetOperations.range("zSetValue", 0, -1);
        System.out.println("通过range(K key, long start, long end)方法获取指定区间的元素:" + zSetValue);

        Object collect = zSetValue.stream().collect(Collectors.toList());
        System.out.println(collect);

        RedisZSetCommands.Range range = new RedisZSetCommands.Range();
        range.lt("D");
        zSetValue = zSetOperations.rangeByLex("zSetValue", range);
        System.out.println("通过rangeByLex(K key, RedisZSetCommands.Range range)方法获取满足非score的排序取值元素:" + zSetValue);

    }


    @Test
    public void opsForSet() {


        SetOperations setOperations = redisManager.opsForSet();

    }
}