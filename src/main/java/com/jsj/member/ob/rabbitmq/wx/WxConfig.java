package com.jsj.member.ob.rabbitmq.wx;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WxConfig {

    /**
     * 正常队列
     */
    public static final String WX_NORMAL_QUEUE = "ob.wx.normal.queue";

    /**
     * 错误队列
     */
    public static final String WX_ERROR_QUEUE = "ob.wx.error.queue";

    @Bean
    public Queue WxNormalQueue() {
        return new Queue(WX_NORMAL_QUEUE, true);
    }

    @Bean
    public Queue WxErrorQueue() {
        return new Queue(WX_ERROR_QUEUE, true);
    }

}
