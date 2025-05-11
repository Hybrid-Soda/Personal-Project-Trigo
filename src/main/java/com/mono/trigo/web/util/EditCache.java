package com.mono.trigo.web.util;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

public class EditCache {

    private static RedisTemplate<String, Object> redisTemplate;

    public static Object getCache(String keyName, Long keyNumber) {

        String redisKey = keyName + "::" + keyNumber;
        return redisTemplate.opsForValue().get(redisKey);
    }

    public static void setCache(String keyName, Long keyNumber, Object object, int time, String unit) {

        String redisKey = keyName + "::" + keyNumber;
        switch (unit) {

            case "HOUR":
                redisTemplate.opsForValue().set(redisKey, object, time, TimeUnit.HOURS);
                break;
            case "MINUTE":
                redisTemplate.opsForValue().set(redisKey, object, time, TimeUnit.MINUTES);
                break;
            case "SECOND":
                redisTemplate.opsForValue().set(redisKey, object, time, TimeUnit.SECONDS);
                break;
        }
    }

    public static void deleteCache(String keyName, Long keyNumber) {

        String redisKey = keyName + "::" + keyNumber;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(redisKey))) {
            redisTemplate.delete(redisKey);
        }
    }
}
