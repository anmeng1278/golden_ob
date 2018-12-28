package com.jsj.member.ob.rabbitmq.card;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoldenConfig {

    /**
     * 正常队列
     */
    public static final String GOLDEN_NORMAL_QUEUE = "ob.gl.normal.queue";

    /**
     * 错误队列
     */
    public static final String GOLDEN_ERROR_QUEUE = "ob.gl.error.queue";

    @Bean
    public Queue GlNormalQueue() {
        return new Queue(GOLDEN_NORMAL_QUEUE, true);
    }

    @Bean
    public Queue GlErrorQueue() {
        return new Queue(GOLDEN_ERROR_QUEUE, true);
    }

}
