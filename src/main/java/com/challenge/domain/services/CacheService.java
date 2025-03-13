package com.challenge.domain.services;

public interface CacheService {
    boolean exists(String key);

    void set(String key);
}
