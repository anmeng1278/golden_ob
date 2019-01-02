package com.jsj.member.ob.config;

import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@EnableRedisHttpSession
public class HttpSessionConfig {

    @Inject
    private RedisOperationsSessionRepository sessionRepository;

    @PostConstruct
    private void afterPropertiesSet() {
        sessionRepository.setDefaultMaxInactiveInterval(60 * 60 * 24 * 30);
    }
}