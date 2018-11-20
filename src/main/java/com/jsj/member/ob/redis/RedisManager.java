package com.jsj.member.ob.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

@Component
public class RedisManager {

    @Autowired
    RedisTemplate redisTemplate;

    public ValueOperations opsForValue() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        return valueOperations;
    }

    public HashOperations opsForHash() {
        HashOperations hashOperations = redisTemplate.opsForHash();
        return hashOperations;
    }

    public ListOperations opsForList() {
        ListOperations listOperations = redisTemplate.opsForList();
        return listOperations;
    }

    public ZSetOperations opsForZSet() {
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
        return zSetOperations;
    }

    public SetOperations opsForSet() {
        SetOperations setOperations = redisTemplate.opsForSet();
        return setOperations;
    }

}
