package com.qqspeed.utils;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis工具类（封装常用操作）
 */
@Component
public class RedisUtils {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 缓存前缀（避免key冲突）
    private static final String CAR_CACHE_PREFIX = "feiche:car:";
    private static final String HOT_CAR_CACHE_KEY = "feiche:car:hot";

    /**
     * 设置缓存（带过期时间）
     */
    public void set(String key, Object value, long timeout, TimeUnit unit) {
        if (StrUtil.isBlank(key) || value == null) {
            return;
        }
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        operations.set(key, value, timeout, unit);
    }

    /**
     * 获取缓存
     */
    public Object get(String key) {
        if (StrUtil.isBlank(key)) {
            return null;
        }
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        return operations.get(key);
    }

    /**
     * 删除缓存
     */
    public void delete(String key) {
        if (StrUtil.isBlank(key)) {
            return;
        }
        redisTemplate.delete(key);
    }

    // 赛车详情缓存key
    public String getCarDetailKey(String carName) {
        return CAR_CACHE_PREFIX + "detail:" + carName;
    }

//    // 热门赛车缓存key
//    public String getHotCarKey() {
//        return HOT_CAR_CACHE_KEY;
//    }
}
