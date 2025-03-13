package com.challenge.infrastructure.data;

import com.challenge.domain.services.CacheService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisCacheService implements CacheService {

    @Value("${orders.service.cache.duplicate.check.ttl.hours}")
    private Integer duplicateCheckHoursTTL;
    private final StringRedisTemplate redisTemplate;

    public RedisCacheService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean exists(String key) {
        Boolean hasKey = redisTemplate.hasKey(key);
        return Boolean.TRUE.equals(hasKey);
    }

    @Override
    public void set(String key, Integer duplicateCheckHoursTTL) {
        redisTemplate.opsForValue().set(key, "", Duration.ofHours(duplicateCheckHoursTTL));
    }
}
