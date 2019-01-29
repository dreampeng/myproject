package com.noadd.myapp.redis;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * redis接口
 */
public interface RedisManager {

    //新增
    void update(String key, Object value);

    void update(String key, Object value, Long num, TimeUnit unit);

    //获取
    Object select(String key);

    //删除
    void delete(String key);

    List<Object> selects(String key);
}
