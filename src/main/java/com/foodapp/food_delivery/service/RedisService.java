package com.foodapp.food_delivery.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private final RedisTemplate redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    public RedisService(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void set(String key,Object o, long ttl){
        try {
            String json = objectMapper.writeValueAsString(o);
            redisTemplate.opsForValue().set(key,  json, ttl, TimeUnit.SECONDS);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T get(String key, Class<T> entityClass){

        try {
            Object o = redisTemplate.opsForValue().get(key);
            if(o == null) return null;
            return objectMapper.readValue(o.toString(), entityClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String key){
        redisTemplate.delete(key);
    }

    //delete by pattern
    public void deleteByPattern(String pattern){
        redisTemplate.delete(redisTemplate.keys(pattern));
    }
}
