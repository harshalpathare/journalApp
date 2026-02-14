package net.engineeringdigest.journalApp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // =============================
    // 🔍 GET DATA FROM REDIS
    // =============================
    public <T> T get(String key, Class<T> clazz) throws JsonProcessingException {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        String value = ops.get(key);

        if (value == null) {
            return null;
        }

        return objectMapper.readValue(value, clazz);
    }

    // =============================
    // 💾 SET DATA INTO REDIS
    // =============================
    public void set(String key, Object value, Long ttlSeconds) throws JsonProcessingException {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        if (value == null) {
            return;
        }

        String jsonValue = objectMapper.writeValueAsString(value);

        ops.set(key, jsonValue, ttlSeconds, TimeUnit.SECONDS);
    }
}
