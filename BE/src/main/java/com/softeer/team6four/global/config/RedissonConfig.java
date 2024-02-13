package com.softeer.team6four.global.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {
    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    private static final String REDISSON_HOST_PREFIX = "redis://";

    @Bean
    public RedissonClient redissonClient() {
        RedissonClient redisson = null;
        Config config = new Config();
        config.useSingleServer()
            .setIdleConnectionTimeout(10000)
            .setConnectTimeout(10000)
            .setAddress(REDISSON_HOST_PREFIX + redisHost + ":" + redisPort)
            .setSubscriptionsPerConnection(5)
            .setSubscriptionConnectionMinimumIdleSize(1)
            .setSubscriptionConnectionPoolSize(50)
            .setConnectionMinimumIdleSize(24)
            .setConnectionPoolSize(64)
            .setDatabase(1);
        redisson = Redisson.create(config);
        return redisson;
    }
}