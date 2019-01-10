package com.noadd.myapp.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * redis管理实现类
 **/
@Service
public class RedisManagerImpl implements RedisManager {
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public void update(String key, Object value) {
        if (redisTemplate.hasKey(key)) {
            delete(key);
        }
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void update(String key, Object value, Long num, TimeUnit unit) {
        if (redisTemplate.hasKey(key)) {
            delete(key);
        }
        redisTemplate.opsForValue().set(key, value, num, unit);
    }

    @Override
    public Object select(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void delete(String key) {
        if (redisTemplate.hasKey(key)) {
            redisTemplate.delete(key);
        }
    }
}
