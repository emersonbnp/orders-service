package com.challenge.infrastructure.data;

import com.challenge.domain.services.CacheService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class RedisCacheService implements CacheService {

    @Value("${orders.service.cache.duplicate.check.ttl.hours}")
    private Integer duplicateCheckHoursTTL;
    @NonNull
    private final StringRedisTemplate redisTemplate;

    @Override
    public boolean exists(String key) {
        Boolean hasKey = redisTemplate.hasKey(key);
        return Boolean.TRUE.equals(hasKey);
    }

    @Override
    public void set(String key) {
        redisTemplate.opsForValue().set(key, "", Duration.ofHours(duplicateCheckHoursTTL));
    }

    @Override
    public void remove(String key) {
        redisTemplate.opsForValue().getAndDelete(key);
    }
}
