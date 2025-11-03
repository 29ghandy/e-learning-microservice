package org.example.courseservice.services.helper;

import lombok.RequiredArgsConstructor;
import org.example.courseservice.dtos.DiscountCacheDTO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, DiscountCacheDTO> redisTemplate;

    private static final String PREFIX = "discount:";

    public void saveDiscount(Long courseId, DiscountCacheDTO discount, Duration ttl) {
        String key = PREFIX + courseId;
        redisTemplate.opsForValue().set(key, discount, ttl);
    }

    public Optional<DiscountCacheDTO> getDiscount(Long courseId) {
        String key = PREFIX + courseId;
        DiscountCacheDTO discount = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(discount);
    }

    public void deleteDiscount(Long courseId) {
        redisTemplate.delete(PREFIX + courseId);
    }

    public boolean exists(Long courseId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(PREFIX + courseId));
    }
}
