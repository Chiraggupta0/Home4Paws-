package com.home4paws.home4paws.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;
import java.util.Map;

@Configuration
@EnableCaching
public class RedisConfig {
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory cf) {
        var base = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .disableCachingNullValues();

        Map<String, RedisCacheConfiguration> caches = Map.of(
                "availablePets", base.entryTtl(Duration.ofMinutes(5)),
                "petById", base.entryTtl(Duration.ofMinutes(10)),
                "adminStats", base.entryTtl(Duration.ofSeconds(30)),
                "subStatus", base.entryTtl(Duration.ofMinutes(2)));
        return RedisCacheManager.builder(cf).cacheDefaults(base)
                .withInitialCacheConfigurations(caches).build();
    }
}