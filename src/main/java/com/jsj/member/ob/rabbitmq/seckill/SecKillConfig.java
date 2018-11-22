package com.jsj.member.ob.rabbitmq.seckill;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecKillConfig {

    /**
     * 正常队列
     */
    public static final String SECKILL_NORMAL_QUEUE = "ob.seckill.normal.queue";

    /**
     * 错误队列
     */
    public static final String SECKILL_ERROR_QUEUE = "ob.seckill.error.queue";

    @Bean
    public Queue NormalQueue() {
        return new Queue(SECKILL_NORMAL_QUEUE, true);
    }

    @Bean
    public Queue ErrorQueue() {
        return new Queue(SECKILL_ERROR_QUEUE, true);
    }

}
