package org.example.enrollmentservice.services.helper;

import lombok.RequiredArgsConstructor;
import org.example.enrollmentservice.dtos.DiscountCacheDTO;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
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

    public long atomicDecrementMembers(Long courseId) {
        String key = PREFIX + courseId;

        List<Object> results = redisTemplate.execute(new SessionCallback<>() {
            @Override
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                operations.watch(key);
                DiscountCacheDTO discount = (DiscountCacheDTO) operations.opsForValue().get(key);

                if (discount == null) {
                    operations.unwatch();
                    return List.of(-1L);
                }

                long current = Optional.ofNullable(discount.getDiscountNumberOfMembers()).orElse(0L);
                if (current <= 0) {
                    operations.multi();
                    operations.delete(key);
                    operations.exec();
                    return List.of(0L);
                }

                discount.setDiscountNumberOfMembers(current - 1);

                operations.multi();
                operations.opsForValue().set(key, discount);
                return operations.exec();
            }
        });

        // If WATCH was triggered (conflict), results == null
        if (results == null) return -2L;

        // Otherwise, return updated count or 0 if expired
        Object result = results.get(results.size() - 1);
        if (result instanceof DiscountCacheDTO dto) {
            return dto.getDiscountNumberOfMembers();
        }

        return 0L;
    }
}
